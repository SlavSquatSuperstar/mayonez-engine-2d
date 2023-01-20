package mayonez.physics.shapes

import mayonez.math.Interval
import mayonez.math.Vec2

/**
 * A non-rotatable, axis-oriented bounding box (AABB) with four edges and defined by a width and height. Not to be used
 * with a collider. For rotatable rectangle, use the [Rectangle] class.
 *
 * @author SlavSquatSuperstar
 */
open class BoundingBox(private val center: Vec2, private val size: Vec2) :
    Rectangle(center, size) {

    companion object {
//        /**
//         * Rotates a copy of this rectangle, then resizes the bounding box to cover the
//         * new rectangle.
//         */
//        private fun rotatedDimensions(size: Vec2, angle: Float): Vec2 {
//            // w' = w*cos(theta) + h*sin(theta)
//            // h' = w*sin(theta) + h*cos(theta)
//            val cos = abs(cos(angle))
//            val sin = abs(sin(angle))
//            return Vec2(size.dot(Vec2(cos, sin)), size.dot(Vec2(sin, cos)))
//        }
    }

    // Box Properties

    override val isAxisAligned: Boolean
        get() = true

    val xInterval: Interval
        get() = Interval(min().x, max().x)

    val yInterval: Interval
        get() = Interval(min().y, max().y)

    // Transformations

    override fun translate(direction: Vec2): BoundingBox = BoundingBox(center + direction, size)

    override fun rotate(angle: Float, origin: Vec2?): BoundingBox =
        throw UnsupportedOperationException("Bounding boxes cannot be rotated")

//    /**
//     * Calculates the bounding box of a rectangle with the same size as this, rotated by the given angle.
//     *
//     * @param angle  the counterclockwise angle
//     * @param origin The point to rotate around. Pass in null to rotate around the centroid.
//     * @return the rotated rectangle's bounding box
//     */
//    override fun rotate(angle: Float, origin: Vec2?): BoundingRectangle {
//        return BoundingRectangle(center.rotate(angle, origin ?: center), rotatedDimensions(size, angle))
//    }

    override fun scale(factor: Vec2, origin: Vec2?): BoundingBox {
        return BoundingBox(if (origin == null) center else center.scale(factor, origin), size * factor)
    }

    // Rectangle vs Point

    override fun nearestPoint(position: Vec2): Vec2 {
        return if (position in this) position else position.clampInbounds(min(), max())
    }

    override fun contains(point: Vec2): Boolean = point.inRange(center - size * 0.5f, center + size * 0.5f)

    // Overrides

    override fun equals(other: Any?): Boolean {
        return (other is BoundingBox) && (this.center == other.center) && (this.size == other.size)
    }

    /**
     * A description of the rectangle in the form "Bounding Box (x, y), Size: (b, h)
     */
    override fun toString(): String = String.format("Bounding Box $center, Size: $size")

}