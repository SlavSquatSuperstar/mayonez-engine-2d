package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2

/**
 * A rectangle whose base and height have the same length.
 *
 * @param length the side length of the square, s
 */
class Square(center: Vec2, val length: Float, angle: Float) : Rectangle(center, Vec2(length), angle) {

    // Square Properties

    override fun perimeter(): Float = 4f * length

    /**
     * The rectangle's centroidal moment of inertia, equal to 1/6*m(s^2).
     *
     * Second moment of area: I_z = 1/12*(hb^3 + bh^3) = 1/12*2A(s^2)
     */
    override fun angMass(mass: Float): Float = (1 / 6f) * mass * length * length

    // Transformations

    override fun translate(direction: Vec2): Square = Square(center + direction, length, angle)

    override fun rotate(angle: Float): Square = Square(center, length, this.angle + angle)

    /**
     * Scales this square uniformly by the given scale factor.
     * To scale a square non-uniformly, use the Rectangle class.
     *
     * @param factor how much to scale the side length by
     * @return the scaled circle
     */
    override fun scale(factor: Float): Square = Square(center, length * factor, angle)

    override fun scale(factor: Vec2): Rectangle {
        throw UnsupportedOperationException("Must be a non-regular shape")
    }

    // Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Square) && (this.center == other.center) && MathUtils.equals(this.length, other.length)
    }

    /**
     * A description of the rectangle in the form Square (x, y), side=s, rotation=theta
     */
    override fun toString(): String = String.format("Square $center, side=$length, rotation=%.2f°", angle);

}