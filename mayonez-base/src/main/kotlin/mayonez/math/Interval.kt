package mayonez.math

import java.util.*

/**
 * Stores an ordered pair of floats which represent the minimum and maximum values of an interval.
 *
 * @author SlavSquatSuperstar
 */
class Interval(min: Float, max: Float) {
    @JvmField
    val min = min.coerceAtMost(max)

    @JvmField
    val max = min.coerceAtLeast(max)

    // Range Functions

    fun clamp(value: Float): Float = min.coerceAtLeast(value).coerceAtMost(max)

    /**
     * The difference between the max and min values.
     *
     * @return the range of this interval
     */
    fun difference(): Float = max - min

    /**
     * Linearly interpolates (lerps) between two floats, or finds the number in this interval
     * that is the given percentage of the distance between the start to the end.
     *
     * @param distance what percent to travel from the start
     * @return the interpolated point
     */
    fun lerp(distance: Float): Float = min + difference() * distance

    /**
     * Takes a number in this interval and calculates its percent distance along this interval.
     *
     * @param amount a number
     * @return the percent distance along the interval
     */
    fun invLerp(amount: Float): Float = (amount - min) / difference()

    // Operator Overrides

    /**
     * If the given number is within this range's bounds.
     *
     * @param value a number
     * @return if the number is in this range
     */
    operator fun contains(value: Float): Boolean = (min <= value) && (value <= max)

    /**
     * If this range contains another range entirely.
     *
     * @param interval another range
     * @return if the other range is in this one
     */
    operator fun contains(interval: Interval): Boolean = (this.min <= interval.min) && (this.max >= interval.max)

    override fun equals(other: Any?): Boolean {
        return (other is Interval) && FloatMath.equals(this.min, other.min) && FloatMath.equals(this.max, other.max)
    }

    override fun hashCode(): Int = Objects.hash(min, max)

    override fun toString(): String = "Range ($min, $max)"
}