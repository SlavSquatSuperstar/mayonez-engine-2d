package slavsquatsuperstar.math

import slavsquatsuperstar.mayonez.Logger
import slavsquatsuperstar.math.MathUtils.clamp
import slavsquatsuperstar.math.MathUtils.equals
import kotlin.math.*

/**
 * A Vector2 represents an object in 2D space that has magnitude and direction (arrow), the location of something in the
 * xy-plane (point), or even an ordered pair of two numbers (list).
 **
 * @param x the new x-component
 * @param y the new y-component
 *
 * @constructor Initialize this vector from an x and y value.
 *
 * @author SlavSquatSuperstar
 */
open class Vec2 constructor(
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
    constructor() : this(0F, 0F)

    /**
     * Initialize this vector to copy another vector's x and y values.
     *
     * @param v the vector to copy
     */
    constructor(v: Vec2) : this(v.x, v.y)

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

    // Operator Overloads

    operator fun plus(v: Vec2) = Vec2(this.x + v.x, this.y + v.y)
    operator fun minus(v: Vec2) = Vec2(this.x - v.x, this.y - v.y)
    operator fun times(scalar: Float) = Vec2(this.x * scalar, this.y * scalar)
    operator fun times(v: Vec2) = Vec2(this.x * v.x, this.y * v.y)

    /**
     * Copies this vector.
     * @return a new vector with the same components
     */
    operator fun unaryPlus() = Vec2(this)

    /**
     * Negates this vector.
     * @return a new vector with this vector's components times -1
     */
    operator fun unaryMinus() = this * -1F

    // Arithmetic Operations

    /**
     * Adds another vector to this vector.
     *
     * @param v another 2D vector
     * @return the vector sum
     */
    fun add(v: Vec2): Vec2 = this + v

    /**
     * Subtracts another vector from this vector.
     *
     * @param v another 2D vector
     * @return the vector difference
     */
    fun sub(v: Vec2): Vec2 = this - v

    /**
     * Multiplies both components vector by a number.
     *
     * @param scalar any number
     * @return the multiplied vector
     */
    fun mul(scalar: Float): Vec2 = this * scalar

    /**
     * Multiplies the components of this vector by the corresponding components of another vector.
     *
     * @param v another vector
     * @return the multiplied vector
     */
    fun mul(v: Vec2): Vec2 = this * v

    /**
     * Divides both components vector by a number.
     *
     * @param scalar a non-zero number
     * @return the divided vector
     */
    operator fun div(scalar: Float): Vec2 {
        if (scalar == 0F) {
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

    // Vector Operations

    /**
     * Multiplies the corresponding components of this vector and another vector.
     *
     * @param v another vector
     * @return the dot product
     */
    fun dot(v: Vec2): Float = this.x * v.x + this.y * v.y

    /**
     * Returns the z-component of the cross product between this vector and another.
     *
     * @param v another vector
     * @return the 2D cross product
     */
    fun cross(v: Vec2): Float = this.x * v.y - this.y * v.x

    /**
     * Projects this vector onto another vector, returning the components of this vector in the direction of another.
     *
     * @param vOnto another vector
     * @return the projected vector
     */
    fun project(vOnto: Vec2) = vOnto * (this.dot(vOnto) / vOnto.lenSquared())

    // Other Operations

    /**
     * Clamps the components of this vector within the bounding box created by the given vectors.
     *
     * @param min the minimum value for each of the components
     * @param max the maximum value for each of the components
     * @return the clamped vector
     */
    fun clampInbounds(min: Vec2, max: Vec2): Vec2 = Vec2(clamp(x, min.x, max.x), clamp(y, min.y, max.y))

    /**
     * Clamps the length of this vector if it exceeds the provided value, while keeping the same direction. Useful for
     * movement scripts.
     *
     * @param length any number
     * @return the clamped vector
     */
    fun clampLength(length: Float): Vec2 {
        return if (lenSquared() > length * length)
            this * (length / len())
        else +this
    }

    // Pythagorean Theorem Methods

    fun distance(v: Vec2): Float = sqrt(distanceSquared(v))

    fun distanceSquared(v: Vec2): Float = (x - v.x) * (x - v.x) + (y - v.y) * (y - v.y)

    /**
     * Calculates the length of this vector.
     *
     * @return this vector's length
     */
    fun len(): Float = sqrt(lenSquared())

    /**
     * Calculates the length squared of this vector (less CPU expensive than square root).
     *
     * @return this vector's length squared
     */
    fun lenSquared(): Float = (x * x) + (y * y)

    /**
     * Calculates the vector with the same direction as this vector and a magnitude of 1. Returns (0, 0) if this vector
     * is (0, 0).
     *
     * @return the unit vector
     */
    fun unitVector(): Vec2 {
        return if (equals(lenSquared(), 0F) || equals(lenSquared(), 1F))
            Vec2(this)
        else this / len()
    }

    // Angle Methods

    /**
     * Calculates the angle in degrees between this vector and the x-axis.
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
    fun angle(v: Vec2): Float = Math.toDegrees(acos((dot(v) / len() / v.len()).toDouble())).toFloat()

    /**
     * Rotates this vector by an angle around some origin.
     *
     * @param degrees the angle, in degrees
     * @param origin  the point to rotate around
     * @return the rotated vector
     */
    fun rotate(degrees: Float, origin: Vec2): Vec2 {
        if (degrees % 360 == 0F)
            return +this

        // Translate the vector space to the origin (0, 0)
        val x = x - origin.x
        val y = y - origin.y

        // Rotate the point around the new origin
        val cosTheta = cos(Math.toRadians(degrees.toDouble()).toFloat())
        val sinTheta = sin(Math.toRadians(degrees.toDouble()).toFloat())
        val newX = x * cosTheta - y * sinTheta
        val newY = x * sinTheta + y * cosTheta

        // Revert the vector space to the old point
        return Vec2(newX, newY) + origin
    }

    // Overrides

    override fun equals(other: Any?): Boolean {
        return if (other is Vec2)
            equals(x, other.x) && equals(y, other.y)
        else super.equals(other)
    }

    override fun toString(): String = String.format("(%.4f, %.4f)", x, y)

    override fun hashCode(): Int = 31 * x.hashCode() + y.hashCode()


}