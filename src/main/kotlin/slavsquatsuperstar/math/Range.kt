package slavsquatsuperstar.math

/**
 * Stores a pair of floats which represent the minimum and maximum values of an interval.
 *
 * @author SlavSquatSuperstar
 */
class Range(
    min: Float, max: Float
) {
    @JvmField
    val min = min.coerceAtMost(max)

    @JvmField
    val max = min.coerceAtLeast(max)

    operator fun contains(value: Float): Boolean = (min <= value) && (value <= max)

    override fun equals(other: Any?): Boolean {
        if (other is Range)
            return MathUtils.equals(this.min, other.min) && MathUtils.equals(this.max, other.max)
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return min.hashCode() * 31 + max.hashCode()
    }

    override fun toString(): String {
        return "Min: $min Max: $max"
    }
}