package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.math.Vec2
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


    // Circle vs Point

    override fun contains(point: Vec2): Boolean {
        return point.distanceSq(center()) <= radius * radius
    }

    override fun nearestPoint(position: Vec2): Vec2 {
        return if (position in this) position
        else center() + (position - center()).clampLength(radius)
    }

}