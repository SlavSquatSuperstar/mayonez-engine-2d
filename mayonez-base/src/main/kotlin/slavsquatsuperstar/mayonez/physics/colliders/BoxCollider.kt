package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.shapes.Polygon

/**
 * A rotatable rectangle with a width and height. The sides will align with the object's rotation angle.
 *
 * @author SlavSquatSuperstar
 */
class BoxCollider(private val size: Vec2) : PolygonCollider(Polygon.rectangle(Vec2(), size)) {

    // Shape Properties

    /**
     * Calculates the dimensions of this box factoring in the object's scale.
     *
     * @return the size in world space
     */
    fun size(): Vec2 = size * (transform.scale)

    val width: Float
        @JvmName("width")
        get() = size().x

    val height: Float
        @JvmName("height")
        get() = size().y

    override fun getAngMass(mass: Float): Float = mass * MathUtils.hypotSq(width, height) / 12f

    // unrotated top left in local space
    private fun localMin(): Vec2 = size * -0.5f

    // unrotated bottom right in local space
    private fun localMax(): Vec2 = size * 0.5f

    // Box vs Point

    override fun contains(point: Vec2): Boolean {
        // Rotate the point into the box's local space then compare to local min and max
        return transform.toLocal(point).inRange(localMin(), localMax())
    }

    override fun nearestPoint(position: Vec2): Vec2 {
        // Transform the point into local space, clamp it, and then transform it back
        return if (position in this) position
        else transform.toWorld(transform.toLocal(position).clampInbounds(localMin(), localMax()))
    }

}