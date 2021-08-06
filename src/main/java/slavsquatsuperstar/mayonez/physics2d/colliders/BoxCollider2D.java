package slavsquatsuperstar.mayonez.physics2d.colliders;

import org.apache.commons.lang3.ArrayUtils;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold;

/**
 * An oriented bounding box (OBB), a rectangle that can be rotated. The sides will align with the object's rotation
 * angle.
 *
 * @author SlavSquatSuperstar
 */
public class BoxCollider2D extends PolygonCollider2D {

    private final Vec2 size;

    private BoxCollider2D(Vec2 min, Vec2 max) {
        super(new Vec2(min), new Vec2(max.x, min.y), new Vec2(max), new Vec2(min.x, max.y));
        this.size = max.sub(min);
    }

    public BoxCollider2D(Vec2 size) {
        this(size.div(-2), size.div(2));
    }

    // Shape Properties

    /**
     * Calculates the dimensions of this box factoring in the object's scale.
     *
     * @return the size in the world
     */
    public Vec2 size() {
        return size.mul(transform.scale);
    }

    public float width() {
        return size().x;
    }

    public float height() {
        return size().y;
    }

    /**
     * Returns the unscaled size of this box.
     *
     * @return the size in local space
     */
    // TODO parent send scale event to modify size directly
    protected Vec2 localSize() {
        return size;
    }

    // unrotated top left in local space
    protected Vec2 localMin() {
        return size.div(-2);
    }

    // unrotated bottom right in local space
    protected Vec2 localMax() {
        return size.div(2);
    }

    public float getRotation() {
        return transform.rotation;
    }

    @Override
    public Vec2[] getNormals() {
        return new Vec2[]{new Vec2(1, 0).rotate(-getRotation()), new Vec2(0, 1).rotate(-getRotation())};
    }

    // Shape vs Point

    @Override
    public boolean contains(Vec2 point) {
        Vec2 pointLocal = transform.toLocal(point); // Rotate the point into the box's local space
        return MathUtils.inRange(pointLocal.x, localMin().x, localMax().x) && MathUtils.inRange(pointLocal.y, localMin().y, localMax().y);
    }

    @Override
    public Vec2 nearestPoint(Vec2 position) {
        if (contains(position))
            return position;
        // Transform the point into local space, clamp it, and then transform it back
        return transform.toWorld(transform.toLocal(position).clampInbounds(localMin(), localMax()));
    }

    // Shape vs Line

    @Override
    public boolean intersects(Edge2D edge) {
        if (contains(edge.start) || contains(edge.end))
            return true;

        // No need to rotate the line because raycast will do that automatically
        return raycast(new Ray2D(edge), null, edge.getLength());
    }

    @Override
    public boolean raycast(Ray2D ray, RaycastResult result, float limit) {
        RaycastResult.reset(result);

        // Transform the ray to local space (but just rotate direction)
        Ray2D localRay = new Ray2D(transform.toLocal(ray.origin), ray.direction.rotate(-getRotation()));

        // Parametric distance to min/max x and y axes of box
        Vec2 tNear = localMin().sub(localRay.origin).div(localRay.direction);
        Vec2 tFar = localMax().sub(localRay.origin).div(localRay.direction);

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

        if (tHitFar < 0 || tHitNear > tHitFar) // Ray is pointing away
            return false;

        // If ray starts inside shape, use far for contact
        float distToBox = (tHitNear < 0) ? tHitFar : tHitNear;

        // Is the contact point past the ray limit?
        if (limit > 0 && distToBox > limit)
            return false;

        Vec2 normal = new Vec2(); // Use (0, 0) for diagonal collision
        if (tNear.x > tNear.y) // Horizontal collision
            normal = (localRay.direction.x < 0) ? new Vec2(1, 0) : new Vec2(-1, 0);
        else if (tNear.x < tNear.y) // Vertical collision
            normal = (localRay.direction.y < 0) ? new Vec2(0, 1) : new Vec2(0, -1);

        if (result != null) {
            Vec2 contact = transform.toWorld(localRay.getPoint(distToBox));
            result.set(contact, normal.rotate(getRotation()), contact.sub(ray.origin).len());
        }

        return true;
    }

    // Shape vs Shape

    @Override
    public boolean detectCollision(Collider2D collider) {
        if (collider instanceof CircleCollider)
            return getCollisionInfo(collider) != null;
        return super.detectCollision(collider);
    }

    @Override
    public CollisionManifold getCollisionInfo(Collider2D collider) {
        if (collider instanceof CircleCollider)
            return getCollisionInfo((CircleCollider) collider);
        else if (collider instanceof BoxCollider2D)
            return getCollisionInfo((BoxCollider2D) collider);
        return null;
    }

    private CollisionManifold getCollisionInfo(CircleCollider circle) {
        Vec2 closestToCircle = this.nearestPoint(circle.center());
        if (!circle.contains(closestToCircle))
            return null;

        float depth = circle.radius() - closestToCircle.distance(circle.center());
        Vec2 normal = circle.center().sub(closestToCircle).unitVector();
        CollisionManifold result = new CollisionManifold(normal, depth);
        result.addContactPoint(closestToCircle.sub(normal.mul(depth)));
        return result;
    }

    private CollisionManifold getCollisionInfo(BoxCollider2D box) {
        Vec2 centerA = this.center();
        Vec2 centerB = box.center();

        Vec2 dist = centerB.sub(centerA);

        if (!detectCollision(box))
            return null;

        // Axis with minimum overlap
        Vec2[] axes = ArrayUtils.addAll(this.getNormals(), box.getNormals());
        float[] overlaps = new float[axes.length];
        for (int i = 0; i < overlaps.length; i++)
            overlaps[i] = getAxisOverlap(box, axes[i]);
        int minIndex = MathUtils.minIndex(overlaps);

        float overlap = overlaps[minIndex];
        Vec2 axis = axes[minIndex];
        Vec2 normal = dist.project(axis).unitVector();

        CollisionManifold collision = new CollisionManifold(normal, overlap);
        collision.addContactPoint(centerA);
        collision.addContactPoint(centerB);
        return collision;
    }
}
