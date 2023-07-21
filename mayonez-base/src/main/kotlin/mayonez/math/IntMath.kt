package mayonez.math

/**
 * A library of common math operations designed to work with integers. A
 * supplement to the methods found in [java.lang.Math].
 *
 * @author SlavSquatSuperstar
 */
object IntMath {

    // Accumulator Functions

    @JvmStatic
    fun avg(vararg values: Int): Int = sum(*values) / values.size

    @JvmStatic
    fun sum(vararg values: Int): Int {
        var sum = 0
        for (v in values) sum += v
        return sum
    }

    // Find Extreme Functions

    @JvmStatic
    fun min(vararg values: Int): Int {
        if (values.isEmpty()) return 0
        var min = Int.MAX_VALUE
        for (n in values) {
            if (n < min) min = n
        }
        return min
    }

    @JvmStatic
    fun max(vararg values: Int): Int {
        if (values.isEmpty()) return 0
        var max = Int.MIN_VALUE
        for (n in values) {
            if (n > max) max = n
        }
        return max
    }

    // Range Functions

    /**
     * Restricts an integer's value within a provided range.
     *
     * @param value a integer
     * @param min the lower bound, inclusive
     * @param max the upper bound, inclusive
     * @return a number within the bounds
     */
    @JvmStatic
    fun clamp(value: Int, min: Int, max: Int): Int {
        return Interval(min.toFloat(), max.toFloat()).clamp(value.toFloat()).toInt()
    }


}