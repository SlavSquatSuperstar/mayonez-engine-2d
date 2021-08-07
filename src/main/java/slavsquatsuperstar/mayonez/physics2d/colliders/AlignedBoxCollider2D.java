package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;

// TODO scale with transform

/**
 * An axis-aligned bounding box (AABB), a rectangle that is never rotated. The sides will always align with the x and y
 * axes.
 *
 * @author SlavSquatSuperstar
 */
public class AlignedBoxCollider2D extends BoxCollider2D {

    // Need to lock rotation somehow

    public AlignedBoxCollider2D(Vec2 size) {
        super(size);
    }

    // Shape Properties

    // min in world space
    public Vec2 min() {
        return center().sub(size().div(2));
    }

    // max in world space
    public Vec2 max() {
        return center().add(size().div(2));
    }

    @Override
    public float getRotation() {
        return 0f;
    }

    @Override
    public Vec2[] getVertices() {
        return new Vec2[]{
                new Vec2(min()), new Vec2(min().x, max().y), new Vec2(max()), new Vec2(max().x, min().y)
        };
    }

    @Override
    public Vec2[] getNormals() {
        return new Vec2[]{new Vec2(1, 0), new Vec2(0, 1)};
    }

    @Override
    public AlignedBoxCollider2D getMinBounds() {
        return new AlignedBoxCollider2D(localSize()).setTransform(transform).setRigidBody(rb);
    }

    // AABB vs Point

    @Override
    public boolean contains(Vec2 point) {
        return MathUtils.inRange(point.x, min().x, max().x) && MathUtils.inRange(point.y, min().y, max().y);
    }

    @Override
    public Vec2 nearestPoint(Vec2 position) {
        if (contains(position))
            return position;
        return position.clampInbounds(min(), max());
    }

    // AABB vs Line

    @Override
    public boolean intersects(Edge2D edge) {
        if (contains(edge.start) || contains(edge.end))
            return true;
        // Make rays from the line that goes both ways
        return raycast(new Ray2D(edge), null, edge.getLength());
    }

    @Override
    public boolean raycast(Ray2D ray, RaycastResult result, float limit) {
        RaycastResult.reset(result);

        // Parametric distance to min/max x and y axes of box
        Vec2 tNear = min().sub(ray.origin).div(ray.direction);
        Vec2 tFar = max().sub(ray.origin).div(ray.direction);

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

        // If ray starts inside shape, use far for contact
        float distToBox = (tHitNear < 0) ? tHitFar : tHitNear;

        // If enabled, check if contact point is past the ray limit
        if (limit > 0 && distToBox > limit)
            return false;

        Vec2 normal = new Vec2(); // Use (0, 0) for diagonal collision
        if (tNear.x > tNear.y) // Horizontal collision
            normal = (ray.direction.x < 0) ? new Vec2(1, 0) : new Vec2(-1, 0);
        else if (tNear.x < tNear.y) // Vertical collision
            normal = (ray.direction.y < 0) ? new Vec2(0, 1) : new Vec2(0, -1);

        if (result != null)
            result.set(ray.getPoint(distToBox), normal, distToBox);

        return true;
    }

}
