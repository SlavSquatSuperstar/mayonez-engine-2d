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
        return new Vector2(center().x - radius, center().y - radius);
    }

    public Vector2 max() {
        return new Vector2(center().x + radius, center().y + radius);
    }

    public float area() {
        return MathUtils.PI * radius * radius;
    }

    @Override
    public AlignedBoxCollider2D getMinBounds() {
        return new AlignedBoxCollider2D(new Vector2(radius * 2, radius * 2)).setTransform(transform).setRigidBody(rb);
    }

    // Collision Methods

    @Override
    public boolean contains(Vector2 point) {
        return point.sub(center()).lengthSquared() <= radius * radius;
    }

    @Override
    public Vector2 nearestPoint(Vector2 position) {
        if (contains(position))
            return position;
        return position.sub(center()).clampLength(radius);
    }

    @Override
    public boolean intersects(Line2D line) {
        if (contains(line.start) || contains(line.end)) // Check if contains endpoints
            return true;

        // TODO Do circle and line interval overlap?

        Vector2 startToCenter = center().sub(line.start);
        Vector2 projected = startToCenter.project(line.toVector());
        // Is nearest point outside of actual line segment?
        if (!MathUtils.inRange(projected.lengthSquared() / line.toVector().lengthSquared(), 0, 1))
            return false;
        Vector2 nearestPointOnLine = line.start.add(projected);
        return contains(nearestPointOnLine);
    }

    @Override
    public boolean raycast(Ray2D ray, RaycastResult result, float limit) {
        RaycastResult.reset(result);
        limit = Math.abs(limit);

        // Trace the ray's origin to the circle's center
        Vector2 originToCenter = center().sub(ray.origin);
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

        // is limit too far?
        if (limit > 0 && distToCircle > limit)
            return false;

        if (result != null) {
            Vector2 point = ray.getPoint(distToCircle);
            Vector2 normal = point.sub(center()).unitVector();
            result.set(point, normal, distToCircle);
        }
        return true;
    }

    @Override
    public boolean detectCollision(Collider2D collider) {
        if (collider == this) // If same object ignore
            return false;

        if (collider instanceof CircleCollider) {
            float distSquared = this.center().distanceSquared(collider.center());
            float radiiSum = this.radius + ((CircleCollider) collider).radius;
            return distSquared <= radiiSum * radiiSum;
        } else if (collider instanceof AlignedBoxCollider2D)
            return contains(collider.nearestPoint(this.center()));
        else if (collider instanceof BoxCollider2D)
            return contains(collider.nearestPoint(this.center()));

        return false;
    }

    @Override
    public CollisionManifold getCollisionInfo(Collider2D collider) {
        if (collider instanceof CircleCollider)
            return getCollisionInfo((CircleCollider) collider);
        if (collider instanceof AlignedBoxCollider2D)
            return getCollisionInfo((AlignedBoxCollider2D) collider);
        return null;
    }

    private CollisionManifold getCollisionInfo(CircleCollider circle) {
        float sumRadii = this.radius + circle.radius;
        Vector2 distance = circle.center().sub(this.center());
        if (distance.lengthSquared() > sumRadii * sumRadii) // No intersection
            return null;

        // Separate circles factoring in mass
        // TODO bad idea if no rb, just use overlap instead
        float massProportion = this.rb.getMass() / (this.rb.getMass() + circle.rb.getMass());
        float depth = Math.abs(distance.length() - sumRadii);
        Vector2 normal = distance.unitVector(); // direction of displacement

        // Simulate real physics, where circles only contact at one point
        Vector2 contactPoint = this.center().add(normal.mul(this.radius - depth * massProportion));

        CollisionManifold result = new CollisionManifold(normal, depth);
        result.addContactPoint(contactPoint);
        return result;
    }

    private CollisionManifold getCollisionInfo(AlignedBoxCollider2D box) {
        if (!detectCollision(box))
            return null;

        Vector2 closestToCircle = box.nearestPoint(this.center());
        float overlap = radius - closestToCircle.distance(this.center());
        CollisionManifold result = new CollisionManifold(closestToCircle.sub(this.center()), overlap);
        result.addContactPoint(closestToCircle);
        return result;
    }

}
