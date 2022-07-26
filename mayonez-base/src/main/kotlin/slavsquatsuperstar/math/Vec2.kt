package slavsquatsuperstar.math

import org.joml.Vector2f
import slavsquatsuperstar.math.MathUtils.equals
import kotlin.math.*

/**
 * A Vec2 represents an object in 2D space that has magnitude and direction (arrow), the location of something in the
 * xy-plane (point), or even an ordered pair of two numbers (list).
 **
 * @param x the vector's x-component
 * @param y the vector's y-component
 *
 * @constructor Initialize this vector from an x and y value, as (x, y).
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
    constructor() : this(0f)

    /**
     * Initialize this vector to have the same x and y components, as (a, a).
     *
     * @param num the value for both x and y
     */
    constructor(num: Float) : this(num, num)

    /**
     * Initialize this vector to copy another vector's x and y values, as (vx, vy)
     *
     * @param v the vector to copy
     */
    constructor(v: Vec2) : this(v.x, v.y)

    /**
     * Initialize this vector with the x and y values from a JOML [Vector2f] object, as (vx, vy).
     *
     * @param v the JOML vector to copy
     */
    constructor(v: Vector2f) : this(v.x, v.y)

    /**
     * Copies this vector.
     * @return a new vector with the same components
     */
    operator fun unaryPlus() = Vec2(this)

    /**
     * Negates this vector.
     * @return a new vector with this vector's components times -1
     */
    operator fun unaryMinus() = Vec2(-x, -y)

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
    operator fun div(scalar: Float): Vec2 = Vec2(x.safeDivide(scalar), y.safeDivide(scalar))
//        = if (scalar == 0f) Vec2() else this * (1f / scalar)

    /**
     * Divides the components of this vector by the corresponding components of another vector.
     *
     * @param v another vector with non-zero components
     * @return the divided vector
     */
    operator fun div(v: Vec2): Vec2 = Vec2(this.x.safeDivide(v.x), this.y.safeDivide(v.y))

    /**
     * Divides two numbers and returns zero if the divisor (denominator) is zero.
     */
    private fun Float.safeDivide(f: Float): Float = if (equals(f, 0f)) 0f else this / f

    // Special Vector Operations

    /**
     * Multiplies the corresponding components of this vector and another vector.
     *
     * @param v another vector
     * @return the dot product
     */
    fun dot(v: Vec2): Float = (this.x * v.x) + (this.y * v.y)

    /**
     * Returns the z-component of the cross product between this vector and another,
     * equal to the determinant of the matrix with this vector and the other as columns
     *
     * @param v another vector
     * @return the 2D cross product
     */
    fun cross(v: Vec2): Float = (this.x * v.y) - (v.x * this.y)

    /**
     * Projects this vector onto another vector, returning the components of this vector in the direction of another.
     *
     * @param vOnto another vector
     * @return the vector projection
     */
    fun project(vOnto: Vec2): Vec2 = vOnto * (this.dot(vOnto) / vOnto.lenSq())

    /**
     * Calculates the project length, or component, of this vector onto another vector.
     *
     * @param vOnto another vector
     * @return the scalar projection
     */
    fun component(vOnto: Vec2): Float = this.dot(vOnto) / vOnto.len()

    // Pythagorean Theorem / Length Methods

    /**
     * Calculates the magnitude of this vector.
     *
     * @return this vector's magnitude
     */
    fun len(): Float = sqrt(lenSq())

    /**
     * Calculates the magnitude squared of this vector (less CPU expensive than square root).
     *
     * @return this vector's magnitude squared
     */
    fun lenSq(): Float = (x * x) + (y * y)

    /**
     * Calculates the distance between the tips of this vector and another.
     *
     * @param v a 2D vector
     * @return the distance
     */
    fun distance(v: Vec2): Float = sqrt(distanceSq(v))

    /**
     * Calculates the distance squared between the tips of this vector and another.
     *
     * @param v a 2D vector
     * @return the distance squared
     */
    fun distanceSq(v: Vec2): Float = (v - this).lenSq()

    /**
     * Creates a vector with the same direction as this but with a length (magnitude) of 1. Returns (0, 0) if this
     * vector is (0, 0).
     *
     * @return the unit vector
     */
    fun unit(): Vec2 {
        return if (equals(lenSq(), 1f) || equals(lenSq(), 0f)) +this
        else this / len()
    }

    // Angle Methods

    /**
     * Calculates the angle in degrees between this vector and the positive x-axis (1, 0).
     *
     * @return this vector's angle in the x-y plane
     */
    fun angle(): Float = Math.toDegrees(atan2(y.toDouble(), x.toDouble())).toFloat()

    /**
     * Calculates the angle in degrees between this vector and another.
     *
     * @param v another 2D vector
     * @return the angle between the two vectors
     */
    fun angle(v: Vec2): Float = MathUtils.toDegrees(acos(this.dot(v) / (this.len() * v.len())))

    /**
     * Rotates this vector by an angle around the origin (0, 0).
     *
     * @param degrees the angle, in degrees clockwise
     * @return the rotated vector
     */
    fun rotate(degrees: Float): Vec2 = rotate(degrees, Vec2())

    /**
     * Rotates this vector by an angle around the given origin point.
     *
     * @param degrees the angle, in degrees clockwise
     * @param origin  the point to rotate around
     * @return the rotated vector
     */
    fun rotate(degrees: Float, origin: Vec2): Vec2 {
        if (this == origin || equals(degrees % 360, 0f)) return +this // Trivial
        val localPos = this - origin // Translate the vector space to the origin
        val rot = Mat22(degrees) // Rotate the point around the new origin
        return (rot * localPos) + origin // Revert the vector space to the old point
    }

    /**
     * Creates a vector perpendicular (rotated 90 degrees counterclockwise)  to this one with a length of 1.
     *
     * @return a perpendicular vector
     */
    fun normal(): Vec2 = Vec2(-y, x)

    // Math Utils Methods

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
        return if (lenSq() > length * length) this * (length / len())
        else +this
    }

    /**
     * Rounds the components of this vector up and returns them in a new vector.
     *
     * @return the ceiling vector
     */
    fun ceiling(): Vec2 = Vec2(ceil(x), ceil(y))

    /**
     * Rounds the components of this vector down and returns them in a new vector.
     *
     * @return the floored vector
     */
    fun floor(): Vec2 = Vec2(floor(x), floor(y))

    fun inRange(min: Vec2, max: Vec2): Boolean =
        MathUtils.inRange(this.x, min.x, max.x) && MathUtils.inRange(this.y, min.y, max.y)

    /**
     * Calculates the average position of this vector and another.
     *
     * @param v a 2D vector
     * @return the midpoint
     */
    fun midpoint(v: Vec2): Vec2 = (this + v) * 0.5f

    /**
     * Convert this vector to a JOML [Vector2f] with the same x and y values
     */
    fun toJOML(): Vector2f = Vector2f(x, y)

    /**
     * Returns a vector whose components are the reciprocals of this vector, equal to (1/x, 1/y).
     */
    fun reciprocal(): Vec2 = Vec2(1f) / this

    // Overrides

    operator fun get(i: Int): Float {
        return when (i) {
            0 -> x
            1 -> y
            else -> 0f
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true // same object
            other is Vec2 -> equals(x, other.x) && equals(y, other.y) // equivalent
            else -> false // null or not a vector
        }
    }

    override fun hashCode(): Int = 31 * x.hashCode() + y.hashCode()

    override fun toString(): String = String.format("(%.4f, %.4f)", x, y)

}