package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2

/**
 * A rectangle whose base and height have the same length.
 *
 * @author SlavSquatSuperstar
 */
class Square(
    center: Vec2,
    /**
     * The side length of the square, s
     */
    @JvmField val length: Float, angle: Float
) : Rectangle(center, Vec2(length), angle) {

    // Square Properties

    override fun perimeter(): Float = 4f * length

    /**
     * The rectangle's centroidal moment of inertia, equal to 1/6*m(s^2).
     *
     * Second moment of area: I_z = 1/12*(hb^3 + bh^3) = 1/12*bh(b^2 + h^2) = 1/12*bh(2s^2) = 1/6*As^2
     */
    override fun angularMass(mass: Float): Float = (1 / 6f) * mass * length * length

    // Transformations

    override fun translate(direction: Vec2): Square = Square(center + direction, length, angle)

    override fun rotate(angle: Float, origin: Vec2?): Square =
        Square(center.rotate(angle, origin ?: center), length, this.angle + angle)

    /**
     * Scales this circle uniformly using the given vector's x-component as the scale factor.
     * To scale a square non-uniformly, use the Rectangle class.
     *
     * @param factor how much to scale the side length by
     * @param centered whether to scale the square from its center and not the origin (0, 0)
     * @return the scaled square
     */
    override fun scale(factor: Vec2, centered: Boolean): Square =
        Square(if (centered) center else center * factor, length * factor.x, angle)

    // Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Square) && (this.center == other.center) && MathUtils.equals(this.length, other.length)
    }

    /**
     * A description of the rectangle in the form Square (x, y), side=s, rotation=theta
     */
    override fun toString(): String = String.format("Square $center, side=$length, rotation=%.2f°", angle);

}