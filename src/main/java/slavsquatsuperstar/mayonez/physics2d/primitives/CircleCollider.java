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

    public Vector2 min() {
        return new Vector2(getCenter().x - radius, getCenter().y - radius);
    }

    public Vector2 max() {
        return new Vector2(getCenter().x + radius, getCenter().y + radius);
    }

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
        Vector2 closestToCircle = new Vector2(getCenter());
        closestToCircle.x = MathUtils.clamp(closestToCircle.x, min.x, max.x);
        closestToCircle.y = MathUtils.clamp(closestToCircle.y, min.y, max.y);

        return contains(closestToCircle);
    }

    boolean intersects(BoxCollider2D box) {
        Vector2 min = box.min();
        Vector2 max = box.max();

        // Create a circle in the box's local space
        Vector2 localCirclePos = getCenter().rotate(box.getRotation(), box.getCenter());
//        Vector2 localCenter = getCenter().sub(box.getCenter());
//        localCenter = localCenter.rotate(box.getRotation(), new Vector2());
//
//        Vector2f r = new Vector2f(circle.getCenter()).sub(box.getRigidbody().getPosition());
//        JMath.rotate(r, -box.getRigidbody().getRotation(), new Vector2f());
//        Vector2f localCirclePos = new Vector2f(r).add(box.getHalfSize());

        Vector2 closest = new Vector2(localCirclePos);
        closest.x = MathUtils.clamp(closest.x, min.x, max.x);
        closest.y = MathUtils.clamp(closest.y, min.y, max.y);

        return contains(localCirclePos);
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

    public CollisionManifold getCollisionInfo(CircleCollider circle) {
        CollisionManifold result = new CollisionManifold();

        float sumRadii = this.radius + circle.radius;
        Vector2 distance = circle.getCenter().sub(this.getCenter());
        if (distance.lengthSquared() > sumRadii * sumRadii) // No intersection
            return result;

        // Divide by 2 to separate each circle evenly
        // TODO factor collider mass and velocity
        float massProportion = this.rb.mass / (this.rb.mass + circle.rb.mass);
        float depth = Math.abs(distance.length() - sumRadii) * 0.5f;
        Vector2 normal = distance.unitVector(); // direction of displacement

        // Simulate real physics, where circles only contact at one point
        Vector2 contactPoint = this.getCenter().add(normal.mul(this.radius - depth));

        result = new CollisionManifold(normal, depth);
        result.addContactPoint(contactPoint);
        return result;
    }

}
