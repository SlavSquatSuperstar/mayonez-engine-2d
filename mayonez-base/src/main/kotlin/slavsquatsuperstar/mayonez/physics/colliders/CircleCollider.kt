package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo
import slavsquatsuperstar.mayonez.physics.collision.RaycastResult
import slavsquatsuperstar.mayonez.physics.shapes.BoundingRectangle
import slavsquatsuperstar.mayonez.physics.shapes.Circle
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * A circle with a radius, centered at the object's position.
 *
 * @author SlavSquatSuperstar
 */
class CircleCollider(radius: Float) : Collider(Circle(Vec2(), radius)) {

    /**
     * Calculates the radius of this circle factoring in the object's scale.
     *
     * @return the radius in world space
     */
    val radius: Float
        get() = (transformToWorld() as Circle).radius

    // Properties
    override fun getMinBounds(): BoundingRectangle = BoundingRectangle(center(), Vec2(radius * 2f))

//    override fun toLocal(world: Vec2): Vec2 = (world - center()) / radius

//    override fun toWorld(local: Vec2): Vec2 = (local * radius) + center()

    // Circle vs Point

    override fun contains(point: Vec2): Boolean {
        return point.distanceSq(center()) <= radius * radius
    }

    override fun nearestPoint(position: Vec2): Vec2 {
        return if (position in this) position
        else center() + (position - center()).clampLength(radius)
    }

    // Circle vs Line

    override fun intersects(edge: Edge2D): Boolean {
        return if (edge.start in this || edge.end in this) true
        else contains(edge.nearestPoint(center())) // Contains point on line nearest to circle
    }

    override fun raycast(ray: Ray, limit: Float): RaycastResult? {
        // Trace the ray's origin to the circle's center
        val originToCenter = center() - (ray.origin)
        val radiusSq = radius * radius

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
            originToCenter.lenSq() - projLength * projLength // Closest distance from center to extended ray
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

    override fun getCollisionInfo(collider: Collider?): CollisionInfo? {
        return when (collider) {
            is CircleCollider -> getCollisionInfo(collider)
            is PolygonCollider -> getCollisionInfo(collider)
            else -> null
        }
    }

    // Circle vs Circle: 1 contact point
    private fun getCollisionInfo(circle: CircleCollider): CollisionInfo? {
        val sumRadii = this.radius + circle.radius
        val distance = circle.center() - this.center()
        if (distance.lenSq() >= sumRadii * sumRadii) // Circles too far away
            return null

        val depth = sumRadii - distance.len()
        val normal = distance.unit()
        val result = CollisionInfo(this, circle, normal, depth)
        result.addContact(center() + (normal * (radius - depth)))
        return result
    }

    // Circle vs Polygon: 1 contact point
    private fun getCollisionInfo(polygon: PolygonCollider): CollisionInfo? {
        val closestToCircle = polygon.nearestPoint(center()) // Point from shape deepest in circle
        if (!contains(closestToCircle!!))
            return null

        val depth = radius - closestToCircle.distance(center())
        val result = CollisionInfo(this, polygon, closestToCircle - center(), depth)
        result.addContact(closestToCircle)
        return result
    }

}