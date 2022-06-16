package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2

/**
 * A four-sided polygon with four perpendicular edges and defined by a width and height.
 * Two opposing sides will always have the same length.
 */
// TODO quad class
open class Rectangle(protected val center: Vec2, val size: Vec2, protected val angle: Float) :
    Polygon(*getVertices(center, size, angle)) {

    constructor(center: Vec2, width: Float, height: Float, angle: Float) : this(center, Vec2(width, height), angle)

    companion object {
        fun getVertices(center: Vec2, size: Vec2, angle: Float): Array<Vec2> {
            val min = center - size * 0.5f
            val max = center + size * 0.5f
            return arrayOf(Vec2(min), Vec2(max.x, min.y), Vec2(max), Vec2(min.x, max.y)).rotate(angle, center)
        }
    }

    // Rectangle Properties

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

    // Transformations

    override fun translate(direction: Vec2): Rectangle = Rectangle(center + direction, size, angle)

    override fun rotate(angle: Float): Rectangle = Rectangle(center, size, this.angle + angle)

    override fun scale(factor: Vec2): Rectangle = Rectangle(center, size * factor, angle)

    // Overrides

    /**
     * Whether a point is inside the rectangle, meaning it lies within all four corners
     */
    override fun contains(point: Vec2): Boolean =
        point.inRange(center - halfSize, center + halfSize)

    override fun equals(other: Any?): Boolean {
        return (other is Rectangle) && (this.center == other.center) && (this.size == other.size)
    }

    /**
     * A description of the rectangle in the form "Rectangle (x, y), size=(b, h), rotation=theta"
     */
    override fun toString(): String = String.format("Rectangle $center, size=$size, rotation=%.2f°", angle)

}