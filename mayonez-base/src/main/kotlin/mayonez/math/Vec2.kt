package mayonez.math

import mayonez.annotations.*
import mayonez.math.FloatMath.equals
import java.lang.Math
import java.util.*
import kotlin.math.*

/**
 * An ordered pair of two floats, x and y, that represents a point in the
 * xy-plane or a 2D vector with magnitude and direction.
 *
 * @param x the x-component
 * @param y the y-component
 * @constructor Initialize this vector from an x and y value, to (x, y). *
 * @author SlavSquatSuperstar
 */
class Vec2(
    /** The vector's x component. */
    @JvmField
    var x: Float,

    /** The vector's y component. */
    @JvmField
    var y: Float
) {

    // Convenience Constructors

    /** Initialize this vector to (0, 0). */
    constructor() : this(0f)

    /**
     * Initialize this vector to have the same x and y components, to (n, a).
     *
     * @param num the value for both x and y
     */
    constructor(num: Float) : this(num, num)

    /**
     * Initialize this vector to copy another vector's x and y values, as (v_x,
     * v_y)
     *
     * @param v the vector to copy
     */
    constructor(v: Vec2) : this(v.x, v.y)

    // Mutators

    /**
     * Sets this vector's components to the provided x and y coordinates.
     *
     * @param x the new x-component
     * @param y the new y-component
     */
    @Mutating
    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    /**
     * Sets this vector's components to the given vector's components. Note:
     * this method is a mutating method!
     *
     * @param v another 2D vector
     */
    @Mutating
    fun set(v: Vec2) = set(v.x, v.y)

    // Arithmetic Operations

    /**
     * Adds another vector to this vector.
     *
     * @param v another 2D vector
     * @return the vector sum
     */
    fun add(v: Vec2): Vec2 = this + v
    operator fun plus(v: Vec2): Vec2 = Vec2(this.x + v.x, this.y + v.y)

    /**
     * Subtracts another vector from this vector.
     *
     * @param v another 2D vector
     * @return the vector difference
     */
    fun sub(v: Vec2): Vec2 = this - v
    operator fun minus(v: Vec2): Vec2 = Vec2(this.x - v.x, this.y - v.y)

    /**
     * Multiplies both components vector by a number.
     *
     * @param scalar any number
     * @return the multiplied vector
     */
    fun mul(scalar: Float): Vec2 = this * scalar
    operator fun times(scalar: Float): Vec2 = Vec2(this.x * scalar, this.y * scalar)

    /**
     * Multiplies the components of this vector by the corresponding components
     * of another vector.
     *
     * @param v another vector
     * @return the multiplied vector
     */
    fun mul(v: Vec2): Vec2 = this * v
    operator fun times(v: Vec2) = Vec2(this.x * v.x, this.y * v.y)

    /**
     * Divides both components vector by a number, or returns (0, 0) if the
     * number is 0.
     *
     * @param scalar a non-zero number
     * @return the divided vector
     */
    operator fun div(scalar: Float): Vec2 = if (scalar == 0f) Vec2() else this * (1f / scalar)

    /**
     * Divides the components of this vector by the corresponding components
     * of another vector. If any components in the second vector is 0, the
     * corresponding quotient component is 0.
     *
     * @param v another vector with non-zero components
     * @return the divided vector
     */
    operator fun div(v: Vec2): Vec2 = Vec2(this.x.safeDivide(v.x), this.y.safeDivide(v.y))

    /**
     * Divides two numbers and returns zero if the divisor (denominator) is
     * zero.
     */
    private fun Float.safeDivide(f: Float): Float = if (equals(f, 0f)) 0f else this / f

    /**
     * Negates this vector.
     *
     * @return a new vector with this vector's components times -1
     */
    operator fun unaryMinus(): Vec2 = Vec2(-x, -y)

    // Special Vector Operations

    /**
     * Multiplies the corresponding components of this vector and another
     * vector.
     *
     * @param v another vector
     * @return the dot product
     */
    fun dot(v: Vec2): Float = (this.x * v.x) + (this.y * v.y)

    /**
     * Calculates the z-component of the cross product between this vector and
     * another. The resulting z-component equals a.x * b.y - b.x * a.y, or the
     * determinant of the matrix with this vector and the other as columns.
     *
     * @param v another vector
     * @return the cross product z-component
     */
    fun cross(v: Vec2): Float = (this.x * v.y) - (v.x * this.y)

    /**
     * Returns the 2D vector of the cross product between this vector and
     * z-component. The resulting vector equals (y*z, -x*z), or this vector's
     * clockwise normal scaled by the given z-component.
     *
     * @param z a vector's z-component
     * @return the 2D cross product vector
     */
    fun cross(z: Float): Vec2 = Vec2(this.y * z, -this.x * z)

    companion object {

        // Float Operations
        operator fun Float.times(v: Vec2): Vec2 = Vec2(this * v.x, this * v.y)
        fun Float.cross(v: Vec2): Vec2 = Vec2(-v.y * this, v.x * this)

        // Static Vector Methods

        /**
         * Calculates the vector triple product between three vectors. The vector
         * triple product is defined as (a × b) × c = b(a · c) - a(b · c).
         *
         * @param v1 the first vector, a
         * @param v2 the second vector, b
         * @param v3 the third vector, c
         * @return the vector triple product
         */
        @JvmStatic
        fun tripleProduct(v1: Vec2, v2: Vec2, v3: Vec2): Vec2 {
            return v2 * (v1.dot(v3)) - v1 * (v2.dot(v3))
        }
    }

    /**
     * Projects this vector onto another vector, returning the components of
     * this vector in the direction of another.
     *
     * @param vOnto another vector
     * @return the vector projection
     */
    fun project(vOnto: Vec2): Vec2 = vOnto * (this.dot(vOnto) / vOnto.lenSq())

    /**
     * Calculates the project length, or component, of this vector onto another
     * vector.
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
     * Calculates the magnitude squared of this vector (less CPU expensive than
     * square root).
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
     * Calculates the distance squared between the tips of this vector and
     * another.
     *
     * @param v a 2D vector
     * @return the distance squared
     */
    fun distanceSq(v: Vec2): Float = (v - this).lenSq()

    /**
     * Creates a vector with the same direction as this but with a length
     * (magnitude) of 1. Returns (0, 0) if this vector is (0, 0).
     *
     * @return the unit vector
     */
    fun unit(): Vec2 {
        return if (equals(lenSq(), 1f) || equals(lenSq(), 0f)) Vec2(this)
        else this / len()
    }

    // Angle Methods

    /**
     * Calculates the signed angle in degrees between this vector and the
     * positive x-axis (1, 0), between -180 and 180.
     *
     * @return this vector's polar angle
     */
    fun angle(): Float = Math.toDegrees(atan2(y.toDouble(), x.toDouble())).toFloat()

    /**
     * Calculates the unsigned (positive) counterclockwise angle in degrees
     * between this vector and the positive x-axis (1, 0), between 0 and 360.
     *
     * @return this vector's polar angle
     */
    fun posAngle(): Float {
        val angle = this.angle()
        return if (angle < 0f) angle + 360f else angle
    }

    /**
     * Calculates the smallest positive angle in degrees between this vector
     * and another, between 0 and 180 degrees.
     *
     * @param v another vector
     * @return the angle between the two vectors
     */
    fun angle(v: Vec2): Float {
        val cos = this.dot(v) / (this.len() * v.len())
        return if (cos > 1f) 0f
        else if (cos < -1f) 180f
        else FloatMath.toDegrees(acos(cos))
    }

    /**
     * Calculates the positive counterclockwise angle in degrees between this
     * vector and another, between 0 and 360 degrees.
     *
     * @param v another vector
     * @return the angle between the two vectors
     */
    fun posAngle(v: Vec2): Float {
        val ang = this.posAngle() - v.posAngle()
        return if (ang < 0f) ang + 360f else ang
    }

    /**
     * Rotates this vector counterclockwise by a given angle around the origin (0, 0).
     *
     * @param degrees the angle, in degrees counterclockwise
     * @return the rotated vector
     */
    fun rotate(degrees: Float): Vec2 = rotate(degrees, Vec2())

    /**
     * Rotates this vector counterclockwise by a given angle around the given origin point.
     *
     * @param degrees the angle, in degrees counterclockwise
     * @param origin the point to rotate around
     * @return the rotated vector
     */
    fun rotate(degrees: Float, origin: Vec2): Vec2 {
        if (this == origin || equals(degrees % 360, 0f)) return Vec2(this) // Trivial
        val localPos = this - origin // Translate the vector space to the origin
        val rot = Mat22(degrees) // Rotate the point around the new origin
        return (rot * localPos) + origin // Revert the vector space to the old point
    }

    /**
     * Creates a vector perpendicular (rotated 90 degrees counterclockwise) to
     * this one with a length of 1.
     *
     * @return a perpendicular vector
     */
    fun normal(): Vec2 = Vec2(-y, x)

    // Math Utils Methods

    /**
     * Clamps the components of this vector within the bounding box created by
     * the given vectors.
     *
     * @param min the minimum value for each of the components
     * @param max the maximum value for each of the components
     * @return the clamped vector
     */
    fun clampInbounds(min: Vec2, max: Vec2): Vec2 {
        return Vec2(
            FloatMath.clamp(x, min.x, max.x),
            FloatMath.clamp(y, min.y, max.y)
        )
    }

    /**
     * Clamps the length of this vector if it exceeds the provided value, while
     * keeping the same direction. Useful for movement scripts.
     *
     * @param length any number
     * @return the clamped vector
     */
    fun clampLength(length: Float): Vec2 {
        return if (lenSq() > length * length) this * (length / len())
        else Vec2(this)
    }

    /**
     * Rounds the components of this vector up and returns them in a new
     * vector.
     *
     * @return the ceiling vector
     */
    fun ceil(): Vec2 = Vec2(ceil(x), ceil(y))

    /**
     * Rounds the components of this vector down and returns them in a new
     * vector.
     *
     * @return the floored vector
     */
    fun floor(): Vec2 = Vec2(floor(x), floor(y))

    fun inRange(min: Vec2, max: Vec2): Boolean {
        return FloatMath.inRange(this.x, min.x, max.x)
                && FloatMath.inRange(this.y, min.y, max.y)
    }

    /**
     * Calculates the average position of this vector and another.
     *
     * @param v a 2D vector
     * @return the midpoint
     */
    fun midpoint(v: Vec2): Vec2 = (this + v) * 0.5f

    // Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Vec2) && equals(x, other.x) && equals(y, other.y)
    }

    override fun hashCode(): Int = Objects.hash(x, y)

    override fun toString(): String = String.format("(%.4f, %.4f)", x, y)

}