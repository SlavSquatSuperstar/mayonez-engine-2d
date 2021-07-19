package slavsquatsuperstar.mayonez.physics2d.primitives;

import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold;
import slavsquatsuperstar.util.MathUtils;

// TODO scale with transform

/**
 * Represents an axis-aligned bounding box, a rectangle that is is never rotated. The sides will always align with the x
 * and y axes.
 */
public class AlignedBoxCollider2D extends AbstractBoxCollider2D {

    public AlignedBoxCollider2D(Vector2 size) {
        super(size);
    }

    // Properties

    @Override
    public Vector2[] getVertices() {
        return new Vector2[]{
                new Vector2(min()), new Vector2(min().x, max().y), new Vector2(max().x, min().y), new Vector2(max())
        };
    }

    @Override
    public AlignedBoxCollider2D getMinBounds() {
        return this;
    }

    // AABB vs Point

    @Override
    public boolean contains(Vector2 point) {
        return MathUtils.inRange(point.x, min().x, max().x) && MathUtils.inRange(point.y, min().y, max().y);
    }

    @Override
    public Vector2 nearestPoint(Vector2 position) {
        if (contains(position))
            return position;
        return position.clampInbounds(min(), max());
    }

    // AABB vs Line

    @Override
    public boolean intersects(Line2D line) {
        if (contains(line.start) || contains(line.end))
            return true;
        // Make rays from the line that goes both ways
        return raycast(new Ray2D(line), null, line.toVector().length()) || raycast(new Ray2D(new Line2D(line.end, line.start)), null, line.toVector().length());
    }

    boolean intersects(BoxCollider2D box) {
        // rotate around box center, or origin?
        Vector2[] axes = {
                new Vector2(0, 1), new Vector2(1, 0),
                new Vector2(0, 1).rotate(box.getRotation(), new Vector2()),
                new Vector2(1, 0).rotate(box.getRotation(), new Vector2())
        };

        // top right - min, bottom left - min
        for (Vector2 axis : axes)
            if (!overlapOnAxis(box, axis))
                return false;

        return true;
    }

    @Override
    public boolean raycast(Ray2D ray, RaycastResult result, float limit) {
        RaycastResult.reset(result);
        limit = Math.abs(limit);

        // Parametric distance to x and y axes of box
        Vector2 tNear = min().sub(ray.origin).div(ray.direction);
        Vector2 tFar = max().sub(ray.origin).div(ray.direction);

        // If parallel and not intersecting
        if (Float.isNaN(tNear.x) || Float.isNaN(tNear.y) || Float.isNaN(tFar.x) || Float.isNaN(tFar.y))
            return false;

        // Swap near and far components if they're out of order
        if (tNear.x > tFar.x) {
            float temp = tNear.x;
            tNear.x = tFar.x;
            tFar.x = temp;
        }
        if (tNear.y > tFar.y) {
            float temp = tNear.y;
            tNear.y = tFar.y;
            tFar.y = temp;
        }

        if (tNear.x > tFar.y || tNear.y > tFar.x) // No intersection
            return false;

        // Parametric distances to near and far contact
        float tHitNear = Math.max(tNear.x, tNear.y);
        float tHitFar = Math.min(tFar.x, tFar.y);
//        float tHitNear = Math.max(Math.min(tNear.x, tFar.x), Math.min(tNear.y, tFar.y));
//        float tHitFar = Math.min(Math.max(tNear.x, tFar.x), Math.max(tNear.y, tFar.y));

        if (tHitFar < 0 || tHitNear > tHitFar) // Ray is pointing away
            return false;

        // If ray starts inside shape, swap near and far
        float distToBox = (tHitNear < 0f) ? tHitFar : tHitNear;
        if (tHitNear < 0) {
            float temp = tHitNear;
            tHitNear = tHitFar;
            tHitFar = temp;
        }

        // Is the contact point past the ray limit?
        if (limit > 0 && distToBox > limit)
            return false;

        Vector2 contactNear = ray.getPoint(tHitNear);
        Vector2 contactFar = ray.getPoint(tHitFar);

        Vector2 normal = new Vector2(); // Use (0, 0) for diagonal collision
        if (tNear.x > tNear.y) // Horizontal collision
            normal = (ray.direction.x < 0) ? new Vector2(1, 0) : new Vector2(-1, 0);
        else if (tNear.x < tNear.y) // Vertical collision
            normal = (ray.direction.y < 0) ? new Vector2(0, 1) : new Vector2(0, -1);

        // TODO make normal orthogonal or use contact - center?
        if (result != null)
            result.set(ray.getPoint(distToBox), normal, tHitNear);

        return true;
    }

    // AABB vs Shape

    @Override
    public boolean detectCollision(Collider2D collider) {
        if (collider == this)
            return false;

        if (collider instanceof CircleCollider)
            return collider.detectCollision(this);
        else if (collider instanceof AlignedBoxCollider2D)
            return overlapOnAxis((AlignedBoxCollider2D) collider, new Vector2(0, 1)) &&
                    overlapOnAxis((AlignedBoxCollider2D) collider, new Vector2(1, 0));
        else if (collider instanceof BoxCollider2D)
            return intersects((BoxCollider2D) collider);

        return false;
    }

    @Override
    public CollisionManifold getCollisionInfo(Collider2D collider) {
        if (collider instanceof CircleCollider)
            return getCollisionInfo((CircleCollider) collider);
        else if (collider instanceof AlignedBoxCollider2D)
            return getCollisionInfo((AlignedBoxCollider2D) collider);
        return null;
    }

    private CollisionManifold getCollisionInfo(CircleCollider circle) {
        if (!detectCollision(circle))
            return null;

        Vector2 closestToCircle = this.nearestPoint(circle.center());
        float overlap = circle.radius - closestToCircle.distance(circle.center());
        CollisionManifold result = new CollisionManifold(closestToCircle.sub(this.center()), overlap);
        result.addContactPoint(closestToCircle);
        return result;
    }

    private CollisionManifold getCollisionInfo(AlignedBoxCollider2D aabb) {
        if (!detectCollision(aabb))
            return null;

        float xOverlap = getAxisOverlap(aabb, new Vector2(1, 0));
        float yOverlap = getAxisOverlap(aabb, new Vector2(0, 1));
        Vector2 distance = aabb.center().sub(this.center());

        CollisionManifold collision;
        if (xOverlap < yOverlap)
            collision = new CollisionManifold(distance.project(new Vector2(1, 0)).unitVector(), xOverlap);
        else if (yOverlap < xOverlap)
            collision = new CollisionManifold(distance.project(new Vector2(0, 1)).unitVector(), yOverlap);
        else
            collision = new CollisionManifold(distance.unitVector(), new Vector2(xOverlap, yOverlap).length());
        collision.addContactPoint(this.center());
        return collision;
    }

}
