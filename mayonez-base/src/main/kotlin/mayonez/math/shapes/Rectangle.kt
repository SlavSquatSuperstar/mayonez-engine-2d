package mayonez.math.shapes

import mayonez.math.*
import java.util.*

/**
 * A rotatable box shape (OBB) defined by a width and height. Rectangles
 * are four-sided polygons with two pairs of parallel edges and containing
 * all right angles
 *
 * @author SlavSquatSuperstar
 */
open class Rectangle(private val center: Vec2, private val size: Vec2, val angle: Float) :
    Polygon(false, *rectangleVertices(center, size, angle)) {

    constructor(center: Vec2, size: Vec2) : this(center, size, 0f)

    // Rectangle Properties

    open val isAxisAligned: Boolean
        get() = MathUtils.equals(angle % 360f, 0f)

    /** The rectangle's width (base), b. */
    @JvmField
    val width: Float = size.x

    /** The rectangle's height, h. */
    @JvmField
    val height: Float = size.y

    /** The rectangle's area, equal to b*h */
    override fun area(): Float = width * height

    /** The center of the rectangle, equal to its position. */
    override fun center(): Vec2 = center

    /**
     * The bottom left corner of the rectangle, or the point with the smallest
     * (most negative) coordinates, assuming the rectangle is axis-aligned.
     *
     * @return the rectangle's min vertex
     */
    fun min(): Vec2 = center - size * 0.5f

    /**
     * The top right corner of the rectangle, or the point with the largest
     * (most positive) coordinates, assuming the rectangle is axis-aligned.
     *
     * @return the rectangle's max vertex
     */
    fun max(): Vec2 = center + size * 0.5f

    // Physical Properties

    /**
     * The rectangle's centroidal moment of inertia, equal to 1/12*m(b^2 +
     * h^2).
     *
     * Second moment of area: I_z = 1/12*(hb^3 + bh^3) = 1/12*bh(b^2 + h^2) =
     * 1/12*A(b^2 + h^2)
     */
    override fun angularMass(mass: Float): Float = mass / 12f * MathUtils.hypotSq(width, height)

    // Transformations

    override fun translate(direction: Vec2): Rectangle {
        return Rectangle(center + direction, size, angle)
    }

    override fun rotate(angle: Float, origin: Vec2?): Rectangle {
        return Rectangle(
            center.rotate(angle, origin ?: center),
            size, this.angle + angle
        )
    }

    override fun scale(factor: Vec2, origin: Vec2?): Rectangle {
        return Rectangle(
            if (origin == null) center else center.scale(factor, origin),
            size * factor, angle
        )
    }

    // Rectangle vs Point

    override fun contains(point: Vec2): Boolean {
        return point.rotate(-angle, center)
            .inRange(center - size * 0.5f, center + size * 0.5f)
    }

    // Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Rectangle) && (this.center == other.center)
                && (this.size == other.size || this.size == other.size.normal())
                && (MathUtils.equals(this.angle, other.angle))
    }

    override fun hashCode(): Int = Objects.hash(center, size, angle)

    /**
     * A description of the rectangle in the form "Rectangle (x, y), Size: (b,
     * h), Rotation: theta"
     */
    override fun toString(): String = String.format("Rectangle $center, Size: $size, Rotation: %.2f°", angle)

    // Helper Methods

    companion object {
        /**
         * Returns an array of counterclockwise rectangle vertices from the center
         * and size, starting with the vertex with the most negative coordinates
         * (before rotation).
         *
         * @param center the rectangle center
         * @param size the rectangle dimensions
         * @param angle the rectangle rotation
         * @return the array of vertices
         */
        @JvmOverloads
        @JvmStatic
        fun rectangleVertices(center: Vec2, size: Vec2, angle: Float = 0f): Array<Vec2> {
            val halfSize = size * 0.5f
            return rectangleVerticesMinMax(center - halfSize, center + halfSize, angle)
        }

        /**
         * Returns an array of counterclockwise rectangle vertices from the min and
         * max corners, starting with the min vertex.
         *
         * @param min the bottom left corner, before rotation
         * @param max the top right corner, after rotation
         * @param angle the rectangle rotation
         * @return the array of vertices
         */
        @JvmOverloads
        @JvmStatic
        fun rectangleVerticesMinMax(min: Vec2, max: Vec2, angle: Float = 0f): Array<Vec2> {
            return arrayOf(Vec2(min), Vec2(max.x, min.y), Vec2(max), Vec2(min.x, max.y)).rotate(angle, (min + max) * 0.5f)
        }

        /**
         * Constructs a rectangle from the min and max corners.
         *
         * @param min the bottom left corner, before rotation
         * @param max the top right corner, after rotation
         * @param angle the rectangle rotation
         * @return the rectangle
         */
        @JvmOverloads
        @JvmStatic
        fun fromMinAndMax(min: Vec2, max: Vec2, angle: Float = 0f): Rectangle {
            return Rectangle((min + max) * 0.5f, max - min, angle)
        }

//        /**
//         * Rotates an imaginary rectangle the same size as this rectangle, then
//         * resizes the bounding box to cover the new rectangle.
//         */
//        fun rotatedDimensions(size: Vec2, angle: Float): Vec2 {
//            // w' = w*cos(theta) + h*sin(theta)
//            // h' = w*sin(theta) + h*cos(theta)
//            val cos = abs(cos(angle))
//            val sin = abs(sin(angle))
//            return Vec2(size.dot(Vec2(cos, sin)), size.dot(Vec2(sin, cos)))
//        }
    }

}