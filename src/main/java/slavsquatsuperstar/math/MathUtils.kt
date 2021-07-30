package slavsquatsuperstar.math

import kotlin.math.abs
import kotlin.math.pow

// TODO less precise sqrt, sin, cos
/**
 * A library of common math operations designed for float using a precision of six decimal places.
 *
 * @author SlavSquatSuperstar
 */
object MathUtils {

    /**
     * The maximum precision a float should have (equal to 10^-6).
     */
    const val EPSILON = 1e-6F

    /**
     * The number π (pi) in float precision.
     */
    const val PI = 3.1415927F

    /**
     * The conversion between radians and degrees in float precision.
     */
    private const val TO_DEGREES = 57.2957795F

    /**
     * The conversion between degrees and radians in float precision.
     */
    private const val TO_RADIANS = 0.0174532F

    // Accumulator Methods

    @JvmStatic
    fun avg(vararg values: Float): Float = sum(*values) / values.size

    @JvmStatic
    fun avg(vararg values: Int): Int = sum(*values) / values.size

    @JvmStatic
    fun sum(vararg values: Float): Float {
        var sum = 0F
        for (v in values)
            sum += v
        return sum
    }

    @JvmStatic
    fun sum(vararg values: Int): Int {
        var sum = 0
        for (v in values)
            sum += v
        return sum
    }

    // Find Extreme Methods

    @JvmStatic
    fun min(vararg values: Float): Float {
        if (values.isEmpty())
            return 0F
        var min = values[0]
        for (i in 1 until values.size)
            if (values[i] < min)
                min = values[i]
        return min
    }

    @JvmStatic
    fun min(vararg values: Int): Int {
        if (values.isEmpty())
            return 0
        var min = values[0]
        for (i in 1 until values.size)
            if (values[i] < min)
                min = values[i]
        return min
    }

    @JvmStatic
    fun max(vararg values: Float): Float {
        if (values.isEmpty())
            return 0F
        var max = values[0]
        for (i in 1 until values.size)
            if (values[i] > max)
                max = values[i]
        return max
    }

    @JvmStatic
    fun max(vararg values: Int): Int {
        if (values.isEmpty())
            return 0
        var max = values[0]
        for (i in 1 until values.size)
            if (values[i] > max)
                max = values[i]
        return max
    }

    // Clamp / Range Methods

    /**
     * Restricts a float's value within a provided range.
     *
     * @param value any real number
     * @param min   the lower bound, inclusive
     * @param max   the upper bound, inclusive
     * @return a number within the bounds
     */
    @JvmStatic
    fun clamp(value: Float, min: Float, max: Float): Float {
        val range = Range(min, max)
        return range.min.coerceAtLeast(value).coerceAtMost(range.max)
    }

    /**
     * Restricts an integer's value within a provided range.
     *
     * @param value any real number
     * @param min   the lower bound, inclusive
     * @param max   the upper bound, inclusive
     * @return a number within the bounds
     */
    @JvmStatic
    fun clamp(value: Int, min: Int, max: Int): Int = clamp(value.toFloat(), min.toFloat(), max.toFloat()).toInt()

    /**
     * Checks whether a number is within a provided range, including the bounds.
     *
     * @param value any real number
     * @param min   the lower bound, inclusive
     * @param max   the upper bound, inclusive
     * @return if the value is within range
     */
    @JvmStatic
    fun inRange(value: Float, min: Float, max: Float): Boolean = value in Range(min, max)

    // Random Number Methods

    /**
     * Generates a random integer between the two provided bounds.
     *
     * @param min the lower bound (inclusive)
     * @param max the maximum bound (inclusive)
     * @return the random float
     */
    @JvmStatic
    fun random(min: Float, max: Float): Float {
        val range = Range(min, max)
        return (Math.random() * (range.max - range.min + Float.MIN_VALUE)).toFloat() + range.min
    }

    /**
     * Generates a random integer between the two provided bounds.
     *
     * @param min the lower bound (inclusive)
     * @param max the maximum bound (inclusive)
     * @return The random integer.
     */
    @JvmStatic
    fun random(min: Int, max: Int): Int {
        val range = Range(min.toFloat(), max.toFloat())
        return (Math.random() * (range.max - range.min + 1) + range.min).toInt()
    }

    // Rounding Methods

    @JvmStatic
    fun round(value: Float, decimalPlaces: Int): Float {
        var temp = value * 10F.pow(decimalPlaces)
        temp = kotlin.math.round(temp)
        return temp / 10F.pow(decimalPlaces)
    }

    @JvmStatic
    fun truncate(value: Float, decimalPlaces: Int): Float {
        var temp = value * 10F.pow(decimalPlaces)
        temp = kotlin.math.truncate(temp)
        return temp / 10F.pow(decimalPlaces)
    }

    // Trig / Angle Methods

    @JvmStatic
    fun toDegrees(radians: Float): Float = radians * TO_DEGREES

    @JvmStatic
    fun toRadians(degrees: Float): Float = degrees * TO_RADIANS

    // Equality Methods

    /**
     * Determines whether two floats are approximately equal within 6 decimal places.
     *
     * @param num1 a number
     * @param num2 another number
     * @return if they are equal
     */
    @JvmStatic
    fun equals(num1: Float, num2: Float): Boolean =
        abs(num1 - num2) <= EPSILON * 1.0.coerceAtLeast(abs(num1).coerceAtLeast(abs(num2)).toDouble())


    // Helper Class

    /**
     * Stores a minimum and maximum value and ensures they are int the correct order.
     */
    class Range(
        min: Float, max: Float
    ) {
        @JvmField
        var min = min.coerceAtMost(max)

        @JvmField
        var max = min.coerceAtLeast(max)

        operator fun contains(value: Float): Boolean = (min <= value) && (value <= max)
    }

}