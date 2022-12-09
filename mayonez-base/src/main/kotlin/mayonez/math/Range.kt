package mayonez.math

import java.util.*

/**
 * Stores a pair of floats which represent the minimum and maximum values of an interval.
 *
 * @author SlavSquatSuperstar
 */
class Range(min: Float, max: Float) {
    @JvmField
    val min = min.coerceAtMost(max)

    @JvmField
    val max = min.coerceAtLeast(max)

    // Range Functions

    fun clamp(value: Float): Float = min.coerceAtLeast(value).coerceAtMost(max)

    /**
     * Get the difference between the max and min values.
     *
     * @return the difference
     */
    fun difference(): Float = max - min

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
     * @param range another range
     * @return if the other range is in this one
     */
    operator fun contains(range: Range): Boolean = (this.min <= range.min) && (this.max >= range.max)

    override fun equals(other: Any?): Boolean {
        return (other is Range) && FloatMath.equals(this.min, other.min) && FloatMath.equals(this.max, other.max)
    }

    override fun hashCode(): Int = Objects.hash(min, max)

    override fun toString(): String = "Range ($min, $max)"
}