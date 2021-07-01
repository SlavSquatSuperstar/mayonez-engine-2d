package slavsquatsuperstar.mayonez.physics2d.primitives;

import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold;
import slavsquatsuperstar.util.MathUtils;

// TODO scale with transform
// TODO local instead of world space?
public class CircleCollider extends Collider2D {

    public final float radius;

    public CircleCollider(float radius) {
        this.radius = radius;
    }

    // Properties

    @Override
    public AlignedBoxCollider2D getMinBounds() {
        AlignedBoxCollider2D aabb = new AlignedBoxCollider2D(new Vector2(radius * 2, radius * 2));
        aabb.transform = this.transform;
        aabb.rb = this.rb;
        return aabb;
    }

    // Collision Methods

    @Override
    public boolean contains(Vector2 point) {
        return point.sub(getCenter()).lengthSquared() <= radius * radius;
    }

    @Override
    public boolean intersects(Line2D line) {
        if (contains(line.start) || contains(line.end)) // Check if contains endpoints
            return true;

        // TODO Do circle and line interval overlap?

        Vector2 startToCenter = getCenter().sub(line.start);
        Vector2 projected = startToCenter.project(line.toVector());
        // Is nearest point outside of actual line segment?
        if (!MathUtils.inRange(projected.lengthSquared() / line.toVector().lengthSquared(), 0, 1))
            return false;
        Vector2 nearestPointOnLine = line.start.add(projected);
        return contains(nearestPointOnLine);
    }

    @Override
    public boolean detectCollision(Collider2D collider) {
        if (collider == this) // If same object ignore
            return false;

        if (collider instanceof CircleCollider) {
            return intersects((CircleCollider) collider);
        } else if (collider instanceof AlignedBoxCollider2D) {
            return intersects((AlignedBoxCollider2D) collider);
        } else if (collider instanceof BoxCollider2D) {
            return intersects((BoxCollider2D) collider);
        } else {
            return false;
        }
    }

    // TODO intersects or touches (< or <=)
    boolean intersects(CircleCollider circle) {
        float distSquared = this.getCenter().sub(circle.getCenter()).lengthSquared();
        float radiiSum = this.radius + circle.radius;
        return distSquared <= radiiSum * radiiSum;
    }

    boolean intersects(AlignedBoxCollider2D aabb) {
        Vector2 min = aabb.min();
        Vector2 max = aabb.max();

        // Closest point on box to circle
        Vector2 closest = new Vector2(getCenter());
        closest.x = MathUtils.clamp(closest.x, min.x, max.x);
        closest.y = MathUtils.clamp(closest.y, min.y, max.y);

        float distanceSquared = getCenter().sub(closest).lengthSquared();
        return distanceSquared <= radius * radius;
    }

    @Override
    public boolean raycast(Ray2D ray, RaycastResult result) {
        RaycastResult.reset(result);

        // Trace the ray's origin to the circle's center
        Vector2 originToCenter = getCenter().sub(ray.origin);
        float radiusSquared = radius * radius;
        float lengthSquared = originToCenter.lengthSquared();

        // TODO create nearestToPoint() method
        // Project originToCenter onto the ray
        float projLength = originToCenter.dot(ray.direction);
        float distNearestSquared = lengthSquared - projLength * projLength; // Closest distance from center to extended ray
        if (radiusSquared - distNearestSquared < 0f) // dot result is negative, don't want imaginary
            return false;

        float f = (float) Math.sqrt(radiusSquared - distNearestSquared);
        // unit lengths along projected vector
        float distToCircle = contains(ray.origin) ? projLength + f : projLength - f;

        boolean hit = ray.getLimit() <= -1 || distToCircle <= ray.getLimit(); // limit ray if constructed from line

        if (result != null) {
            Vector2 point = ray.origin.add(ray.direction.mul(distToCircle));
            Vector2 normal = point.sub(getCenter()).unitVector();
            result.set(point, normal, distToCircle, hit);
        }

        return hit;
    }

    boolean intersects(BoxCollider2D box) {
        Vector2 min = box.min();
        Vector2 max = box.max();

        Vector2 localRadius = getCenter().sub(box.getCenter());
        localRadius = localRadius.rotate(box.rb.getRotation(), new Vector2());

        Vector2 closest = new Vector2(getCenter());
        closest.x = MathUtils.clamp(closest.x, min.x, max.x);
        closest.y = MathUtils.clamp(closest.y, min.y, max.y);

        float distanceSquared = getCenter().sub(closest).lengthSquared();
        return distanceSquared <= radius * radius;
    }

}
