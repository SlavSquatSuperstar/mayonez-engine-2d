package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2

/**
 * A rectangle whose base and height have the same length.
 *
 * @param length the side length of the square, s
 */
class Square(center: Vec2, val length: Float) : Rectangle(center, Vec2(length)) {

    override fun isRegular(): Boolean = true

    override fun perimeter(): Float = 4 * length

    /**
     * The rectangle's centroidal moment of inertia, equal to 1/6*m(s^2).
     *
     * Second moment of area: I_z = 1/12*(hb^3 + bh^3) = 1/12*2A(s^2)
     */
    override fun angMass(mass: Float): Float = (1 / 6f) * mass * length * length

    override fun equals(other: Any?): Boolean {
        return (other is Square) && (this.center == other.center) && MathUtils.equals(this.length, other.length)
    }

    /**
     * A description of the rectangle in the form Rectangle (x, y), side=s
     */
    override fun toString(): String = "Square $center, side=$length"

}