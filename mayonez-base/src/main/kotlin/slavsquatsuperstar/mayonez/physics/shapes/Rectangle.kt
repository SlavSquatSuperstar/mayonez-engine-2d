package slavsquatsuperstar.mayonez.physics.shapes

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2

/**
 * A non-rotatable axis-oriented bounding box (AABB) with four edges and defined by a width and height. Not to be used
 * with a collider. For rotatable rectangle, use the [Polygon.rectangle] method.
 *
 * @author SlavSquatSuperstar
 */
open class Rectangle(private val center: Vec2, private val size: Vec2) :
    Polygon(*rectangleVertices(center, size)) {
    constructor(center: Vec2, width: Float, height: Float) : this(center, Vec2(width, height))

    companion object {
        // Angle only used in polygon template
        fun rectangleVertices(center: Vec2, size: Vec2, angle: Float = 0f): Array<Vec2> {
            val min = center - size * 0.5f
            val max = center + size * 0.5f
            return arrayOf(Vec2(min), Vec2(max.x, min.y), Vec2(max), Vec2(min.x, max.y)).rotate(angle, center)
        }

//        /**
//         * Rotates an imaginary rectangle the same size as this rectangle, then resizes the bounding box to cover the
//         * new rectangle.
//         */
//        fun rotatedDimensions(size: Vec2, angle: Float): Vec2 {
//            // w' = w*cos(theta) + h*sin(theta)
//            // h' = w*sin(theta) + h*cos(theta)
//            val cos = abs(cos(angle))
//            val sin = abs(sin(angle))
//            return Vec2(size.dot(Vec2(cos, sin)), size.dot(Vec2(sin, cos)))
//        }
    }

    // Rectangle Properties

    /**
     * The rectangle's width (base), b.
     */
    @JvmField
    val width: Float = size.x

    /**
     * The rectangle's height, h.
     */
    @JvmField
    val height: Float = size.y

    /**
     * The rectangle's area, equal to b*h
     */
    override fun area(): Float = width * height

    /**
     * The center of the rectangle, equal to its position.
     */
    override fun center(): Vec2 = center

    /**
     * The bottom left corner of the rectangle, which has the smallest (most negative) coordinates.
     *
     * @return the rectangle's min vertex
     */
    fun min(): Vec2 = center - size * 0.5f

    /**
     * The top right corner of the rectangle, which has the largest (most positive) coordinates.
     *
     * @return the rectangle's max vertex
     */
    fun max(): Vec2 = center + size * 0.5f

    // Physical Properties

    /**
     * The rectangle's centroidal moment of inertia, equal to 1/12*m(b^2 + h^2).
     *
     * Second moment of area: I_z = 1/12*(hb^3 + bh^3) = 1/12*bh(b^2 + h^2) = 1/12*A(b^2 + h^2)
     */
    override fun angularMass(mass: Float): Float = mass / 12f * MathUtils.hypotSq(width, height)

    // Transformations

    override fun translate(direction: Vec2): Rectangle = Rectangle(center + direction, size)

    override fun rotate(angle: Float, origin: Vec2?): Polygon =
        throw UnsupportedOperationException("Bounding Rectangles cannot be rotated")

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

    override fun scale(factor: Vec2, origin: Vec2?): Rectangle {
        return Rectangle(if (origin == null) center else center.scale(factor, origin), size * factor)
    }

    // Overrides

    override fun contains(point: Vec2): Boolean = point.inRange(center - size * 0.5f, center + size * 0.5f)

    override fun equals(other: Any?): Boolean {
        return (other is Rectangle) && (this.center == other.center) && (this.size == other.size)
    }

    /**
     * A description of the rectangle in the form "Rectangle (x, y), Size: (b, h)
     */
    override fun toString(): String = String.format("Rectangle $center, Size: $size")

}