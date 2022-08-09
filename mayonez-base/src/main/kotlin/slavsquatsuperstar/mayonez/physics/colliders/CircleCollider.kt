package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo
import slavsquatsuperstar.mayonez.physics.shapes.Circle
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle

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
    override fun getMinBounds(): Rectangle = transformToWorld().boundingRectangle()

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