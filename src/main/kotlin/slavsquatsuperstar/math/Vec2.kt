package slavsquatsuperstar.math

import slavsquatsuperstar.math.MathUtils.equals
import slavsquatsuperstar.mayonez.Logger
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * A Vec2 represents an object in 2D space that has magnitude and direction (arrow), the location of something in the
 * xy-plane (point), or even an ordered pair of two numbers (list).
 **
 * @param x the new x-component
 * @param y the new y-component
 *
 * @constructor Initialize this vector from an x and y value.
 *
 * @author SlavSquatSuperstar
 */
class Vec2 constructor(
    /**
     * The vector's x component.
     */
    @JvmField var x: Float,
    /**
     * The vector's y component.
     */
    @JvmField var y: Float
) {

    // Convenience Constructors

    /**
     * Initialize this vector to (0, 0).
     */
    constructor() : this(0f, 0f)

    /**
     * Initialize this vector to copy another vector's x and y values.
     *
     * @param v the vector to copy
     */
    constructor(v: Vec2) : this(v.x, v.y)

    /**
     * Copies this vector.
     * @return a new vector with the same components
     */
    operator fun unaryPlus() = Vec2(this)

    /**
     * Negates this vector.
     * @return a new vector with this vector's components times -1
     */
    operator fun unaryMinus() = this * -1f

    // Mutators

    /**
     * Sets this vector's components to the provided x and y coordinates. Note: this method is a mutating method!
     *
     * @param x the new x-component
     * @param y the new y-component
     */
    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    /**
     * Sets this vector's components to the given vector's components. Note: this method is a mutating method!
     *
     * @param v another 2D vector
     */
    fun set(v: Vec2) = set(v.x, v.y)

    // Arithmetic Operations

    /**
     * Adds another vector to this vector.
     *
     * @param v another 2D vector
     * @return the vector sum
     */
    fun add(v: Vec2): Vec2 = this + v
    operator fun plus(v: Vec2) = Vec2(this.x + v.x, this.y + v.y)

    /**
     * Subtracts another vector from this vector.
     *
     * @param v another 2D vector
     * @return the vector difference
     */
    fun sub(v: Vec2): Vec2 = this - v
    operator fun minus(v: Vec2) = Vec2(this.x - v.x, this.y - v.y)

    /**
     * Multiplies both components vector by a number.
     *
     * @param scalar any number
     * @return the multiplied vector
     */
    fun mul(scalar: Float): Vec2 = this * scalar
    operator fun times(scalar: Float) = Vec2(this.x * scalar, this.y * scalar)

    /**
     * Multiplies the components of this vector by the corresponding components of another vector.
     *
     * @param v another vector
     * @return the multiplied vector
     */
    fun mul(v: Vec2): Vec2 = this * v
    operator fun times(v: Vec2) = Vec2(this.x * v.x, this.y * v.y)

    /**
     * Divides both components vector by a number.
     *
     * @param scalar a non-zero number
     * @return the divided vector
     */
    operator fun div(scalar: Float): Vec2 {
        if (scalar == 0f) {
            Logger.warn("Vector2: Attempted division by 0")
            return Vec2()
        }
        return this * (1 / scalar)
    }

    /**
     * Divides the components of this vector by the corresponding components of another vector.
     *
     * @param v another vector with non-zero components
     * @return the divided vector
     */
    operator fun div(v: Vec2): Vec2 = Vec2(this.x / v.x, this.y / v.y)

    // Special Vector Operations

    /**
     * Multiplies the corresponding components of this vector and another vector.
     *
     * @param v another vector
     * @return the dot product
     */
    fun dot(v: Vec2): Float = (this.x * v.x) + (this.y * v.y)

    /**
     * Returns the z-component of the cross product between this vector and another.
     *
     * @param v another vector
     * @return the 2D cross product
     */
    fun cross(v: Vec2): Float = Mat22(this, v).determinant()

    /**
     * Projects this vector onto another vector, returning the components of this vector in the direction of another.
     *
     * @param vOnto another vector
     * @return the projected vector
     */
    fun project(vOnto: Vec2): Vec2 = vOnto * (this.dot(vOnto) / vOnto.lenSq())

    // Pythagorean Theorem / Length Methods

    /**
     * Calculates the magnitude of this vector.
     *
     * @return this vector's magnitude
     */
    fun len(): Float = sqrt(lenSq())

    fun mag(): Float = len()

    /**
     * Calculates the magnitude squared of this vector (less CPU expensive than square root).
     *
     * @return this vector's magnitude squared
     */
    fun lenSq(): Float = (x * x) + (y * y)

    fun magSq(): Float = lenSq()

    fun distance(v: Vec2): Float = sqrt(distanceSquared(v))

    fun distanceSquared(v: Vec2): Float = MathUtils.pythagoreanSquared((x - v.x), (y - v.y))

    /**
     * Calculates the vector with the same direction as this vector and a magnitude of 1. Returns (0, 0) if this vector
     * is (0, 0).
     *
     * @return the unit vector
     */
    fun unit(): Vec2 {
        return if (equals(lenSq(), 1f) || equals(lenSq(), 0f)) +this
        else this / len()
    }

    /**
     * Calculates the length this vector's projection onto another vector.
     *
     * @param vOnto another vector
     * @return the projected length
     */
    fun projectedLength(vOnto: Vec2): Float = this.dot(vOnto) / vOnto.len()

    // Angle Methods

    /**
     * Calculates the angle in degrees between this vector and the x-axis.
     *
     * @return this vector's angle in the x-y plane
     */
    fun angle(): Float = Math.toDegrees(atan2(y.toDouble(), x.toDouble())).toFloat()
    // fun angle(): Float = this.angle(Vec2(1f, 0f))

    /**
     * Calculates the angle in degrees between this vector and another.
     *
     * @param v another 2D vector
     * @return the angle between the two vectors
     */
    fun angle(v: Vec2): Float = MathUtils.toDegrees(acos((this.dot(v) / this.len() / v.len())))

    /**
     * Rotates this vector by an angle around some origin.
     *
     * @param degrees the angle, in degrees clockwise
     * @param origin  the point to rotate around
     * @return the rotated vector
     */
    @JvmOverloads
    fun rotate(degrees: Float, origin: Vec2 = Vec2()): Vec2 {
        if (equals(degrees % 360, 0f)) return +this
        val localPos = this - origin // Translate the vector space to the origin
        val rot = Mat22(degrees) // Rotate the point around the new origin
        return (rot * localPos) + origin // Revert the vector space to the old point
    }

    /**
     * Calculates this vector's normal by rotating this vector by 90 degrees and normalizing the result.
     *
     * @return a perpendicular unit vector
     */
    fun getNormal(): Vec2 = Vec2(-y, x).unit()

    // Other Methods

    /**
     * Clamps the components of this vector within the bounding box created by the given vectors.
     *
     * @param min the minimum value for each of the components
     * @param max the maximum value for each of the components
     * @return the clamped vector
     */
    fun clampInbounds(min: Vec2, max: Vec2): Vec2 =
        Vec2(MathUtils.clamp(x, min.x, max.x), MathUtils.clamp(y, min.y, max.y))

    /**
     * Clamps the length of this vector if it exceeds the provided value, while keeping the same direction. Useful for
     * movement scripts.
     *
     * @param length any number
     * @return the clamped vector
     */
    fun clampLength(length: Float): Vec2 {
        return if (lenSq() > length * length)
            this * (length / len())
        else +this
    }

    /**
     * Calculates the average position of this vector and another.
     *
     * @param v a 2D vector
     * @return the midpoint
     */
    fun midpoint(v: Vec2): Vec2 {
        return (this + v) / 2f
    }

    // Overrides

    operator fun component1() = x
    operator fun component2() = y

    override fun hashCode(): Int = 31 * x.hashCode() + y.hashCode()

    override fun equals(other: Any?): Boolean {
        return if (other is Vec2)
            equals(x, other.x) && equals(y, other.y)
        else super.equals(other)
    }

    override fun toString(): String = String.format("(%.4f, %.4f)", x, y)

}