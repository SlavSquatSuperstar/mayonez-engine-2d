package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold;

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

    @Override
    public Vec2 toLocal(Vec2 world) {
        return world.sub(center()).div(radius());
    }

    @Override
    public Vec2 toWorld(Vec2 local) {
        return local.mul(radius()).add(center());
    }

    // Collision Methods

    @Override
    public boolean contains(Vec2 point) {
        return point.distanceSquared(center()) <= radius() * radius();
    }

    @Override
    public Vec2 nearestPoint(Vec2 position) {
        if (contains(position))
            return position;
        return center().add(position.sub(center()).clampLength(radius()));
    }

    @Override
    public boolean intersects(Edge2D edge) {
        if (contains(edge.start) || contains(edge.end))
            return true;
        return contains(edge.nearestPoint(center())); // Contains point on line nearest to circle
    }

    @Override
    public RaycastResult raycast(Ray2D ray, float limit) {
        // Trace the ray's origin to the circle's center
        Vec2 originToCenter = center().sub(ray.origin);
        float radiusSq = radius() * radius();

        // Project originToCenter onto the ray and get length
        /*
         * v = originToCenter
         * d = ray.direction (unit vector)
         * l = length of projected vector
         *
         * v•d = |v|*|d|*cos(theta)
         * v•d = |v|*|d|*l/|v|
         * l = v•d
         */
        float projLength = originToCenter.dot(ray.direction);
        float distNearestSq = originToCenter.lenSquared() - projLength * projLength; // Closest distance from center to extended ray
        if (distNearestSq > radiusSq) // Nearest point on ray is outside the circle
            return null;

        float contactToNearest = MathUtils.sqrt(radiusSq - distNearestSq);
        // Distance along ray to contact, depends if ray starts in circle
        float hitDist = contains(ray.origin) ? projLength + contactToNearest : projLength - contactToNearest;

        if (Math.abs(limit) > 0 && hitDist > Math.abs(limit)) // Ray exceeds limit
            return null;

        if (hitDist < 0) // Contact point is behind ray
            return null;

        Vec2 point = ray.getPoint(hitDist);
        return new RaycastResult(point, point.sub(center()), hitDist);
    }

    @Override
    public boolean detectCollision(Collider2D collider) {
        if (collider instanceof CircleCollider)
            return getCollisionInfo(collider) != null;
        else if (collider instanceof BoxCollider2D)
            return getCollisionInfo(collider) != null;
        return false;
    }

    @Override
    public CollisionManifold getCollisionInfo(Collider2D collider) {
        if (collider instanceof CircleCollider)
            return getCollisionInfo((CircleCollider) collider);
        if (collider instanceof BoxCollider2D)
            return getCollisionInfo((BoxCollider2D) collider);
        return null;
    }

    private CollisionManifold getCollisionInfo(CircleCollider circle) {
        float sumRadii = this.radius() + circle.radius();
        Vec2 distance = circle.center().sub(this.center());

        if (distance.lenSquared() >= sumRadii * sumRadii) // Circles too far away
            return null;

        float depth = sumRadii - distance.len();
        Vec2 normal = distance.unitVector();

        CollisionManifold result = new CollisionManifold(this, circle, normal, depth);
        result.addContactPoint(this.center().add(normal.mul(this.radius() - depth)));
        return result;
    }

    private CollisionManifold getCollisionInfo(BoxCollider2D box) {
        Vec2 closestToCircle = box.nearestPoint(center()); // point from box deepest in circle
        if (!contains(closestToCircle))
            return null;

        float depth = radius() - closestToCircle.distance(center());
        CollisionManifold result = new CollisionManifold(this, box, closestToCircle.sub(center()), depth);
        result.addContactPoint(closestToCircle);
        return result;
    }

}
