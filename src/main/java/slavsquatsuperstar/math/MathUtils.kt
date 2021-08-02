package slavsquatsuperstar.math

import kotlin.math.*

/**
 * A library of common math operations designed for the float data type and precision.
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

    // Comparison Methods

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

    // Pythagorean Theorem

    /**
     * Takes the principal square root of a number.
     *
     * @param value a positive number
     * @return the square root, in float precision.
     */
    @JvmStatic
    fun sqrt(value: Float): Float = sqrt(value.toDouble()).toFloat()

    /**
     * Calculates the squared length of the diagonal of an n-dimensional figure with perpendicular edges (rectangle, cuboid, hyper-cuboid, etc.).
     *
     * @param sides the lengths of the figure's sides
     * @return the result of the pythagorean theorem in n-dimensions, squared
     */
    @JvmStatic
    fun pythagoreanSquared(vararg sides: Float): Float {
        val sidesSq = sides.copyOf()
        for (i in sidesSq.indices)
            sidesSq[i] = sidesSq[i] * sidesSq[i]
        return sum(*sidesSq)
    }

    /**
     * Calculates the length of the diagonal of an n-dimensional figure with perpendicular edges (rectangle, cuboid, hyper-cuboid, etc.).
     *
     * @param sides the lengths of the figure's sides
     * @return the result of the pythagorean theorem in n-dimensions
     */
    @JvmStatic
    fun pythagorean(vararg sides: Float): Float = sqrt(pythagoreanSquared(*sides))

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

    /**
     * Rounds up the given number according to the specified precision.
     *
     * @param value a decimal number
     * @param decimalPlaces the number of decimal places to round
     *
     * @return the rounded number
     */
    @JvmStatic
    fun round(value: Float, decimalPlaces: Int): Float {
        var temp = value * 10F.pow(decimalPlaces)
        temp = kotlin.math.round(temp)
        return temp / 10F.pow(decimalPlaces)
    }

    /**
     * Rounds down the given number according to the specified precision.
     *
     * @param value a decimal number
     * @param decimalPlaces the number of decimal places to round
     *
     * @return the truncated number
     */
    @JvmStatic
    fun truncate(value: Float, decimalPlaces: Int): Float {
        var temp = value * 10F.pow(decimalPlaces)
        temp = kotlin.math.truncate(temp)
        return temp / 10F.pow(decimalPlaces)
    }

    // Trig / Angle Methods

    /**
     * Takes the sine of an angle.
     *
     * @param degrees the measure of the angle, in degrees
     * @return the sine of the angle
     */
    @JvmStatic
    fun sin(degrees: Float): Float = sin(Math.toRadians(degrees.toDouble())).toFloat()

    /**
     * Takes the cosine of an angle.
     *
     * @param degrees the measure of the angle, in degrees
     * @return the cosine of the angle
     */
    @JvmStatic
    fun cos(degrees: Float): Float = cos(Math.toRadians(degrees.toDouble())).toFloat()

    /**
     * Converts an angle from radians to degrees.
     *
     * @param radians the angle measure in radians
     * @return the angle measure in degrees
     */
    @JvmStatic
    fun toDegrees(radians: Float): Float = Math.toDegrees(radians.toDouble()).toFloat()

    /**
     * Converts an angle from degrees to radians.
     *
     * @param degrees the angle measure in degrees
     * @return the angle measure in radians
     */
    @JvmStatic
    fun toRadians(degrees: Float): Float = Math.toRadians(degrees.toDouble()).toFloat()

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