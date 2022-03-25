package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2

open class Rectangle(private val position: Vec2, val size: Vec2) : Shape() {

    constructor(center: Vec2, width: Float, height: Float) : this(center, Vec2(width, height))

    /**
     * The rectangle's width (base), b.
     */
    val width: Float = size.x

    /**
     * The rectangle's height, h.
     */
    val height: Float = size.y

    /**
     * The rectangle's half-size, equal to (b/2, h/2).
     */
    private val halfSize: Vec2 = size * 0.5f

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
    override fun center(): Vec2 = position

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
        point.inRange(position - halfSize, position + halfSize)

    override fun equals(other: Any?): Boolean {
        return (other is Rectangle) && (this.position == other.position) && (this.size == other.size)
    }

    /**
     * A description of the rectangle in the form Rectangle (x, y), size=(b, h)
     */
    override fun toString(): String = "Rectangle $position, size=$size"

}