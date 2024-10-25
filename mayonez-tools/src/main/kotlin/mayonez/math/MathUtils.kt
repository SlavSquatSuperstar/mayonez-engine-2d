package mayonez.math

import kotlin.math.*
import kotlin.math.hypot as kHypot
import kotlin.math.sqrt as kSqrt

/**
 * A library of common math operations designed to work with floats and
 * integers. The methods are a supplement to or wrapper around the methods
 * found in [java.lang.Math]. Some attempts have been made to mitigate
 * float imprecision, although some error should still be expected.
 *
 * @author SlavSquatSuperstar
 */
object MathUtils {

    /**
     * The maximum difference two floats can have to still be considered equal
     * by the engine (equal to 0.000001 = 10^-6).
     */
    const val FLOAT_EPSILON = 1e-6f

    /** The number π (pi) in float precision, approximately 3.1416. */
    const val PI = 3.1415927f

    /** The number τ (tau) in float precision, equal to 2π ≈ 6.2832. */
    const val TWO_PI = 2f * 6.2831855f

    // Equality Methods

    /**
     * Determines whether two floats are approximately equal within 6 decimal
     * places.
     *
     * @param num1 the first float
     * @param num2 the second float
     * @return if the floats are roughly equal
     */
    @JvmStatic
    fun equals(num1: Float, num2: Float): Boolean = equals(num1, num2, FLOAT_EPSILON)

    /**
     * Determines whether two floats are approximately equal within a given
     * error.
     *
     * @param num1 the first float
     * @param num2 the second float
     * @param error the max allowed difference between the two floats
     * @return if the floats are equal within error
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
     * Takes the positive (principal) square root of a number.
     *
     * @param value a non-negative number
     * @return the square root, in float precision.
     */
    @JvmStatic
    fun sqrt(value: Float): Float = kSqrt(value)

    // Hypotenuse

    /**
     * Calculates √(a^2 + b^2) from the given a and b, equal to the hypotenuse
     * length of a right triangle given the two legs (or the diagonal length of
     * a rectangle given the length and width).
     *
     * @param a the first length
     * @param b the second length
     * @return the length of the hypotenuse
     */
    @JvmStatic
    fun hypot(a: Float, b: Float): Float = kHypot(a, b)
    // Use JVM hypot() for better performance/accuracy

    /**
     * Calculates √(a_1^2 + a_2^2 + … + a_n^2) from the given values, equal to
     * the diagonal length of an n-dimensional figure with perpendicular edges
     * (rectangle, cuboid, hyper-cuboid, etc.).
     *
     * @param sides the side lengths
     * @return the length of the diagonal
     */
    @JvmStatic
    fun hypot(vararg sides: Float): Float = kSqrt(hypotSq(*sides))

    /**
     * Calculates a^2 + b^2 from the given a and b, equal to the hypotenuse
     * length squared of a right triangle given the two legs (or the
     * diagonal length squared of a rectangle given the length and width).
     *
     * @param a the first length
     * @param b the second length
     * @return the length of the hypotenuse, squared
     */
    @JvmStatic
    fun hypotSq(a: Float, b: Float): Float = (a * a) + (b * b)

    /**
     * Calculates a_1^2 + a_2^2 + … + a_n^2 from the given values, equal to the
     * diagonal length squared of an n-dimensional figure with perpendicular
     * edges (rectangle, cuboid, hyper-cuboid, etc.).
     *
     * @param sides the side lengths
     * @return the length of the diagonal, squared
     */
    @JvmStatic
    fun hypotSq(vararg sides: Float): Float {
        return sum(*FloatArray(sides.size) { sides[it] * sides[it] })
    }

    /**
     * Calculates √(x^2 - y^2) from the given x and y, equal to the length of
     * one leg of a right triangle given the length of the hypotenuse and other
     * the leg. To avoid taking the square root of a negative number, x ≥ y
     * must be true.
     *
     * @param x the length of the hypotenuse
     * @param y the length of the first leg
     * @return the length of the second leg
     */
    @JvmStatic
    fun invHypot(x: Float, y: Float): Float = kSqrt(x * x - y * y)

    // Average/Sum Methods

    /**
     * Averages (finds the arithmetic mean) any number of given float values.
     *
     * @param values the float array
     * @return the average
     */
    @JvmStatic
    fun avg(vararg values: Float): Float = sum(*values) / values.size

    /**
     * Averages (finds the arithmetic mean) any number of given int values.
     *
     * @param values the int array
     * @return the average
     */
    @JvmStatic
    fun avg(vararg values: Int): Int = sum(*values) / values.size

    /**
     * Sums any number of given float values.
     *
     * @param values the float array
     * @return the sum
     */
    @JvmStatic
    fun sum(vararg values: Float): Float {
        var sum = 0f
        for (v in values) sum += v
        return sum
    }

    /**
     * Sums any number of given int values.
     *
     * @param values the int array
     * @return the sum
     */
    @JvmStatic
    fun sum(vararg values: Int): Int {
        var sum = 0
        for (v in values) sum += v
        return sum
    }

    // Find Min/Max Methods

    /**
     * Finds the min of any number of given float values. Returns zero if the
     * array is empty.
     *
     * @param values the float array
     * @return the min
     */
    @JvmStatic
    fun min(vararg values: Float): Float {
        if (values.isEmpty()) return 0f
        var min = Float.POSITIVE_INFINITY
        for (n in values) {
            if (n < min) min = n
        }
        return min
    }

    /**
     * Finds the min of any number of given int values. Returns zero if the
     * array is empty.
     *
     * @param values the int array
     * @return the min
     */
    @JvmStatic
    fun min(vararg values: Int): Int {
        if (values.isEmpty()) return 0
        var min = Int.MAX_VALUE
        for (n in values) {
            if (n < min) min = n
        }
        return min
    }

    /**
     * Finds the index of the min value in an array of float values. Returns -1
     * if the array is empty.
     *
     * @param values the float array
     * @return the min index
     */
    @JvmStatic
    fun minIndex(vararg values: Float): Int {
        if (values.isEmpty()) return -1
        var minIdx = 0
        for (i in 1..<values.size) {
            if (values[i] < values[minIdx]) minIdx = i
        }
        return minIdx
    }

    /**
     * Finds the max of any number of given float values. Returns zero if the
     * array is empty.
     *
     * @param values the float array
     * @return the max
     */
    @JvmStatic
    fun max(vararg values: Float): Float {
        if (values.isEmpty()) return 0f
        var max = Float.NEGATIVE_INFINITY
        for (n in values) {
            if (n > max) max = n
        }
        return max
    }

    /**
     * Finds the max of any number of given int values. Returns zero if the
     * array is empty.
     *
     * @param values the int array
     * @return the max
     */
    @JvmStatic
    fun max(vararg values: Int): Int {
        if (values.isEmpty()) return 0
        var max = Int.MIN_VALUE
        for (n in values) {
            if (n > max) max = n
        }
        return max
    }

    /**
     * Finds the index of the max value in an array of float values. Returns -1
     * if the array is empty.
     *
     * @param values the float array
     * @return the max index
     */
    @JvmStatic
    fun maxIndex(vararg values: Float): Int {
        if (values.isEmpty()) return -1
        var maxIdx = 0
        for (i in 1..<values.size) {
            if (values[i] > values[maxIdx]) maxIdx = i
        }
        return maxIdx
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
     * Restricts an integer's value within a provided range.
     *
     * @param value a integer
     * @param min the lower bound, inclusive
     * @param max the upper bound, inclusive
     * @return a number within the bounds
     */
    @JvmStatic
    fun clamp(value: Int, min: Int, max: Int): Int = Interval(min, max).clamp(value.toFloat()).toInt()

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
    fun inRange(value: Int, min: Int, max: Int): Boolean = value in Interval(min, max)

    // Rounding Methods

    /**
     * Rounds up the given number according to the specified precision.
     *
     * @param value a float
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
     * @param value a float
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