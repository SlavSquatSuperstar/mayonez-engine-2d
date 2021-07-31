package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold;
import slavsquatsuperstar.math.MathUtils;

// TODO scale with transform
// TODO local instead of world space?

/**
 * A circle with a center and radius.
 *
 * @author SlavSquatSuperstar
 */
public class CircleCollider extends Collider2D {

    private final float radius;

    public CircleCollider(float radius) {
        this.radius = radius;
    }

    // Properties

    /**
     * Calculates the radius of this circle factoring in the object's scale.
     *
     * @return the true radius
     */
    public float radius() {
        return radius * MathUtils.max(transform.scale.x, transform.scale.y);
    }


    @Override
    public AlignedBoxCollider2D getMinBounds() {
        return new AlignedBoxCollider2D(new Vec2(radius() * 2, radius() * 2)).setTransform(transform).setRigidBody(rb);
    }

    // Collision Methods

    @Override
    public boolean contains(Vec2 point) {
        return point.sub(center()).lenSquared() <= radius() * radius();
    }

    @Override
    public Vec2 nearestPoint(Vec2 position) {
        if (contains(position))
            return position;
        return position.sub(center()).clampLength(radius());
    }

    @Override
    public boolean intersects(Edge2D edge) {
        if (contains(edge.start) || contains(edge.end)) // Check if contains endpoints
            return true;

        // TODO Do circle and line interval overlap?

        Vec2 startToCenter = center().sub(edge.start);
        Vec2 projected = startToCenter.project(edge.toVector());
        // Is nearest point outside of actual line segment?
        if (!MathUtils.inRange(projected.lenSquared() / edge.toVector().lenSquared(), 0, 1))
            return false;
        Vec2 nearestPointOnEdge = edge.start.add(projected); // TODO use edge.nearest ?
        return contains(nearestPointOnEdge);
    }

    @Override
    public boolean raycast(Ray2D ray, RaycastResult result, float limit) {
        RaycastResult.reset(result);
        limit = Math.abs(limit);

        // Trace the ray's origin to the circle's center
        Vec2 originToCenter = center().sub(ray.origin);
        float radiusSquared = radius() * radius();
        float lengthSquared = originToCenter.lenSquared();

        // Project originToCenter onto the ray
        float projLength = originToCenter.dot(ray.direction);
        float distNearestSquared = lengthSquared - projLength * projLength; // Closest distance from center to extended ray
        if (radiusSquared - distNearestSquared < 0f) // dot result is negative, don't want imaginary
            return false;

        float f = (float) Math.sqrt(radiusSquared - distNearestSquared);
        // unit lengths along projected vector
        float distToCircle = contains(ray.origin) ? projLength + f : projLength - f;

        // Is limit too far?
        if (limit > 0 && distToCircle > limit)
            return false;

        if (result != null) {
            Vec2 point = ray.getPoint(distToCircle);
            Vec2 normal = point.sub(center()).unitVector();
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
            float radiiSum = this.radius() + ((CircleCollider) collider).radius();
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
        float sumRadii = this.radius() + circle.radius();
        Vec2 distance = circle.center().sub(this.center());
        if (distance.lenSquared() > sumRadii * sumRadii) // No intersection
            return null;

        // Separate circles factoring in mass
        // TODO bad idea if no rb, just use overlap instead
        float massProportion = this.rb.getMass() / (this.rb.getMass() + circle.rb.getMass());
        float depth = Math.abs(distance.len() - sumRadii);
        Vec2 normal = distance.unitVector(); // direction of displacement

        // Simulate real physics, where circles only contact at one point
        Vec2 contactPoint = this.center().add(normal.mul(this.radius() - depth * massProportion));

        CollisionManifold result = new CollisionManifold(normal, depth);
        result.addContactPoint(contactPoint);
        return result;
    }

    private CollisionManifold getCollisionInfo(AlignedBoxCollider2D box) {
        if (!detectCollision(box))
            return null;

        Vec2 closestToCircle = box.nearestPoint(this.center());
        float overlap = radius() - closestToCircle.distance(this.center());
        CollisionManifold result = new CollisionManifold(closestToCircle.sub(this.center()), overlap);
        result.addContactPoint(closestToCircle);
        return result;
    }

}
