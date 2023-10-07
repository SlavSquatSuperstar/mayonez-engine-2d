package mayonez.math

import kotlin.math.*

/**
 * A library of common math operations designed to work with float values
 * but to avoid float imprecision. A supplement and alternative to the
 * methods found in [java.lang.Math].
 *
 * @author SlavSquatSuperstar
 */
object FloatMath {

    /**
     * The maximum difference two floats can have to still be considered equal
     * by the engine (equal to 0.000001).
     */
    const val FLOAT_EPSILON = 1e-6f

    /** The number π (pi) in float precision. */
    const val PI = 3.1415927f

    // Comparison Methods

    /**
     * Determines whether two floats are approximately equal within 6 decimal
     * places.
     *
     * @param num1 a floating-point number
     * @param num2 another floating-point number
     * @return if they are roughly equal
     */
    @JvmStatic
    fun equals(num1: Float, num2: Float): Boolean = equals(num1, num2, FLOAT_EPSILON)

    /**
     * Determines whether two floats are approximately equal within a given
     * error.
     *
     * @param num1 a floating-point
     * @param num2 another floating-point number
     * @param error the max difference between the two numbers
     * @return if they are equal within error
     */
    @JvmStatic
    fun equals(num1: Float, num2: Float, error: Float): Boolean = abs(num1 - num2) <= abs(error)

    // Exponents

    /**
     * Squares a value. Shorthand for x * x or x^2.
     *
     * @param value a real number
     * @return the number squared
     */
    @JvmStatic
    fun squared(value: Float): Float = value * value

    /**
     * Takes the principal (positive) square root of a number.
     *
     * @param value a non-negative number
     * @return the square root, in float precision.
     */
    @JvmStatic
    fun sqrt(value: Float): Float = sqrt(value.toDouble()).toFloat()

    // Pythagorean Theorem

    /**
     * Calculates the length of the hypotenuse of a right triangle from two
     * legs (or the diagonal of a rectangle from two sides), equal to √(a^2 +
     * b^2).
     *
     * @param a the length of the first leg
     * @param b the length of the second leg
     * @return the length of the hypotenuse
     */
    @JvmStatic
    fun hypot(a: Float, b: Float): Float = sqrt(hypotSq(a, b))

    /**
     * Calculates the length of the diagonal of an n-dimensional figure with
     * perpendicular edges (rectangle, cuboid, hyper-cuboid, etc.).
     *
     * @param sides the lengths of the figure's sides
     * @return the hypotenuse (pythagorean theorem) in n-dimensions
     */
    @JvmStatic
    fun hypot(vararg sides: Float): Float = sqrt(hypotSq(*sides))

    /**
     * Calculates the length squared of the hypotenuse of a right triangle from
     * two legs (or the diagonal of a rectangle from two sides), equal to a^2 +
     * b^2.
     *
     * @param a the length of the first leg
     * @param b the length of the second leg
     * @return the length of the hypotenuse squared
     */
    @JvmStatic
    fun hypotSq(a: Float, b: Float): Float = (a * a) + (b * b)

    /**
     * Calculates the length squared of the diagonal of an n-dimensional figure
     * with perpendicular edges (rectangle, cuboid, hyper-cuboid, etc.).
     *
     * @param sides the lengths of the figure's sides
     * @return the diagonal in n-dimensions, squared
     */
    @JvmStatic
    fun hypotSq(vararg sides: Float): Float {
        return sum(*FloatArray(sides.size) { sides[it] * sides[it] })
    }

    /**
     * Calculates the length of a leg of a right triangle from the hypotenuse
     * and the other leg.
     *
     * @param hypot the length of the hypotenuse
     * @param leg the length of a leg
     * @return the length of the other leg
     */
    @JvmStatic
    fun invHypot(hypot: Float, leg: Float): Float = sqrt(hypot * hypot - leg * leg)

    // Accumulator Methods

    @JvmStatic
    fun avg(vararg values: Float): Float = sum(*values) / values.size

    @JvmStatic
    fun sum(vararg values: Float): Float {
        var sum = 0f
        for (v in values) sum += v
        return sum
    }

    // Find Extreme Methods

    @JvmStatic
    fun min(vararg values: Float): Float {
        if (values.isEmpty()) return 0f
        var min = Float.POSITIVE_INFINITY
        for (n in values) {
            if (n < min) min = n
        }
        return min
    }

    @JvmStatic
    fun minIndex(vararg values: Float): Int {
        if (values.isEmpty()) return -1
        var minIndex = 0
        for (i in 1..<values.size) {
            if (values[i] < values[minIndex]) minIndex = i
        }
        return minIndex
    }

    @JvmStatic
    fun max(vararg values: Float): Float {
        if (values.isEmpty()) return 0f
        var max = Float.NEGATIVE_INFINITY
        for (n in values) {
            if (n > max) max = n
        }
        return max
    }

    @JvmStatic
    fun maxIndex(vararg values: Float): Int {
        if (values.isEmpty()) return -1
        var maxIndex = 0
        for (i in 1..<values.size) {
            if (values[i] > values[maxIndex]) maxIndex = i
        }
        return maxIndex
    }

    // Clamp / Range Methods

    /**
     * Restricts a float's value within a provided range.
     *
     * @param value a float
     * @param min the lower bound, inclusive
     * @param max the upper bound, inclusive
     * @return a number within the bounds
     */
    @JvmStatic
    fun clamp(value: Float, min: Float, max: Float): Float = Interval(min, max).clamp(value)

    /**
     * Checks whether a number is within a provided range, including the
     * bounds.
     *
     * @param value a number
     * @param min the lower bound, inclusive
     * @param max the upper bound, inclusive
     * @return if the value is within range
     */
    @JvmStatic
    fun inRange(value: Float, min: Float, max: Float): Boolean = value in Interval(min, max)

    // Rounding Methods

    /**
     * Rounds up the given number according to the specified precision.
     *
     * @param value a floating-point number
     * @param decimalPlaces the number of decimal places to round
     * @return the rounded number
     */
    @JvmStatic
    fun round(value: Float, decimalPlaces: Int): Float {
        val temp = value * 10f.pow(decimalPlaces)
        return round(temp) / 10f.pow(decimalPlaces)
    }

    /**
     * Rounds down the given number according to the specified precision.
     *
     * @param value a floating-point number
     * @param decimalPlaces the number of decimal places to round
     * @return the truncated number
     */
    @JvmStatic
    fun truncate(value: Float, decimalPlaces: Int): Float {
        val temp = value * 10f.pow(decimalPlaces)
        return truncate(temp) / 10f.pow(decimalPlaces)
    }

    // Trig / Angle Methods

    /**
     * Takes the sine of an angle in degrees.
     *
     * @param degrees the measure of the angle, in degrees
     * @return the sine of the angle
     */
    @JvmStatic
    fun sin(degrees: Float): Float = sin(Math.toRadians(degrees.toDouble())).toFloat()

    /**
     * Takes the cosine of an angle in degrees.
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

}