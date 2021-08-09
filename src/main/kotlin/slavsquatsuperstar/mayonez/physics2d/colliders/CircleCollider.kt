package slavsquatsuperstar.mayonez.physics2d.colliders

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold
import slavsquatsuperstar.mayonez.physics2d.RaycastResult
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

/**
 * A circle with a center and radius.
 *
 * @author SlavSquatSuperstar
 */
class CircleCollider(private val radius: Float) : Collider2D() {

    // Properties

    /**
     * Calculates the radius of this circle factoring in the object's scale.
     *
     * @return the radius in world space
     */
    fun radius(): Float = radius * max(transform!!.scale.x, transform!!.scale.y)

    override fun getMinBounds(): AlignedBoxCollider2D {
        return AlignedBoxCollider2D(Vec2(radius() * 2, radius() * 2))
            .setTransform<Collider2D>(transform).setRigidbody(rb)
    }

    override fun toLocal(world: Vec2): Vec2 = (world - center()) / radius()

    override fun toWorld(local: Vec2): Vec2 = (local * radius()) + center()

    // Circle vs Point

    override fun contains(point: Vec2): Boolean {
        return point.distanceSquared(center()) <= radius() * radius()
    }

    override fun nearestPoint(position: Vec2): Vec2 {
        return if (position in this) position
        else center() + (position - center()).clampLength(radius())
    }

    // Circle vs Line

    override fun intersects(edge: Edge2D): Boolean {
        return if (edge.start in this || edge.end in this) true
        else contains(edge.nearestPoint(center())) // Contains point on line nearest to circle
    }

    override fun raycast(ray: Ray2D?, limit: Float): RaycastResult? {
        // Trace the ray's origin to the circle's center
        val originToCenter = center() - (ray!!.origin)
        val radiusSq = radius() * radius()

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
        val projLength = originToCenter.dot(ray.direction)
        val distNearestSq =
            originToCenter.lenSquared() - projLength * projLength // Closest distance from center to extended ray
        if (distNearestSq > radiusSq) // Nearest point on ray is outside the circle
            return null

        val contactToNearest = sqrt(radiusSq - distNearestSq)
        // Distance along ray to contact, depends if ray starts in circle
        val hitDist = if (contains(ray.origin)) projLength + contactToNearest
        else projLength - contactToNearest

        if (abs(limit) > 0 && hitDist > abs(limit)) // Ray exceeds limit
            return null
        if (hitDist < 0) // Contact point is behind ray
            return null

        val point = ray.getPoint(hitDist)
        return RaycastResult(point, point - center(), hitDist)
    }

    // Circle vs Shape

    override fun getCollisionInfo(collider: Collider2D?): CollisionManifold? {
        return when (collider) {
            is CircleCollider -> getCollisionInfo(collider)
            is PolygonCollider2D -> getCollisionInfo(collider)
            else -> null
        }
    }

    // Collision Helper Methods

    private fun getCollisionInfo(circle: CircleCollider): CollisionManifold? {
        val sumRadii = this.radius() + circle.radius()
        val distance = circle.center() - this.center()
        if (distance.lenSquared() >= sumRadii * sumRadii) // Circles too far away
            return null

        val depth = sumRadii - distance.len()
        val normal = distance.unit()
        val result = CollisionManifold(this, circle, normal, depth)
        result.addContact(center() + (normal * (radius() - depth)))
        return result
    }

    private fun getCollisionInfo(polygon: PolygonCollider2D): CollisionManifold? {
        val closestToCircle = polygon.nearestPoint(center()) // Point from shape deepest in circle
        if (!contains(closestToCircle!!))
            return null

        val depth = radius() - closestToCircle.distance(center())
        val result = CollisionManifold(this, polygon, closestToCircle - center(), depth)
        result.addContact(closestToCircle)
        return result
    }

}