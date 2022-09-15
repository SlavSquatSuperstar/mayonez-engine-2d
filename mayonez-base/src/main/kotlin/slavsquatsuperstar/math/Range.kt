package slavsquatsuperstar.math

import slavsquatsuperstar.util.Pair

/**
 * Stores a pair of floats which represent the minimum and maximum values of an interval.
 *
 * @author SlavSquatSuperstar
 */
class Range(min: Float, max: Float): Pair<Float, Float>(min, max) {
    @JvmField
    val min = min.coerceAtMost(max)

    @JvmField
    val max = min.coerceAtLeast(max)

    fun clamp(value: Float): Float {
        return min.coerceAtLeast(value).coerceAtMost(max)
    }

    operator fun contains(value: Float): Boolean = (min <= value) && (value <= max)

    override fun equals(other: Any?): Boolean {
        return  (other is Range) && MathUtils.equals(this.min, other.min) && MathUtils.equals(this.max, other.max)
    }

    override fun hashCode(): Int = min.hashCode() * 31 + max.hashCode()

    override fun toString(): String = "Range ($min, $max)"
}