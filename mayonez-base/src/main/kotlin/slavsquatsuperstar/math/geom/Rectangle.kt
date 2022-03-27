package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2

/**
 * A four-sided polygon with four perpendicular edges and defined by a width and height.
 * Two opposing sides will always have the same length.
 */
open class Rectangle(protected val center: Vec2, val size: Vec2) : Polygon(*getVertices(center, size)) {

    constructor(center: Vec2, width: Float, height: Float) : this(center, Vec2(width, height))

//    constructor(min: Vec2, max: Vec2) : this(min.midpoint(max), max - min)

    companion object {
        fun getVertices(center: Vec2, size: Vec2): Array<Vec2> {
            val halfSize = size * 0.5f
            val min = center - halfSize
            val max = center + halfSize
            return arrayOf(Vec2(min), Vec2(max.x, min.y), Vec2(max), Vec2(min.x, max.y))
        }
    }

    /**
     * The rectangle's width (base), b.
     */
    val width: Float
        get() = size.x

    /**
     * The rectangle's height, h.
     */
    val height: Float
        get() = size.y

    /**
     * The rectangle's half-size, equal to (b/2, h/2).
     */
    private val halfSize: Vec2
        get() = size * 0.5f

    /**
     * The rectangle's area, equal to b*h
     */
    override fun area(): Float = width * height

    /**
     * The rectangle's perimeter equal to 2(b + h)
     */
    override fun perimeter(): Float = 2 * (width + height)

    /**
     * The center of the rectangle, equal to its position.
     */
    override fun center(): Vec2 = center

    /**
     * The rectangle's centroidal moment of inertia, equal to 1/12*m(b^2 + h^2).
     *
     * Second moment of area: I_z = 1/12*(hb^3 + bh^3) = 1/12*A(b^2 + h^2)
     */
    override fun angMass(mass: Float): Float = (1 / 12f) * mass * MathUtils.hypotSq(width, height)

    /**
     * Whether a point is inside the rectangle, meaning it lies within all four corners
     */
    override fun contains(point: Vec2): Boolean =
        point.inRange(center - halfSize, center + halfSize)

    override fun equals(other: Any?): Boolean {
        return (other is Rectangle) && (this.center == other.center) && (this.size == other.size)
    }

    /**
     * A description of the rectangle in the form Rectangle (x, y), size=(b, h)
     */
    override fun toString(): String = "Rectangle $center, size=$size"

}