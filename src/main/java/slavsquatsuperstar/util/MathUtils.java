package slavsquatsuperstar.util;

// TODO less precise sqrt, sin, cos

/**
 * A library of common math operations designed for float using a precision of six decimal places.
 *
 * @author SlavSquatSuperstar
 */
public final class MathUtils {

    /**
     * The maximum precision a float should have (equal to 10^-6).
     */
    public static final float EPSILON = 1e-6f;
    /**
     * The number π (pi) to 6 decimal places.
     */
    public static final float PI = 3.1415927f;

    private MathUtils() {}

    // Accumulator Methods

    public static float average(float[] values) {
        return sum(values) / values.length;
    }

    public static int average(int[] values) {
        return sum(values) / values.length;
    }

    public static float sum(float... values) {
        float sum = 0;
        for (float value : values)
            sum += value;
        return sum;
    }

    public static int sum(int... values) {
        int sum = 0;
        for (int value : values)
            sum += value;
        return sum;
    }

    // Range / Clamp Methods

    /**
     * Checks whether a number is within a provided range, including the bounds.
     *
     * @param value any real number
     * @param min   the lower bound, inclusive
     * @param max   the upper bound, inclusive
     * @return if the value is within range
     */
    public static boolean inRange(float value, float min, float max) {
        Range range = new Range(min, max);
        return range.min <= value && value <= range.max;
    }

    /**
     * Checks whether a number is within a provided range, excluding the bounds.
     *
     * @param value any real number
     * @param min   the lower bound, exclusive
     * @param max   the upper bound, exclusive
     * @return if the value is within range
     */
    public static boolean inRangeUnbounded(float value, float min, float max) {
        Range range = new Range(min, max);
        return range.min < value && value < range.max;
    }

    /**
     * Restricts a float's value within a provided range.
     *
     * @param value any real number
     * @param min   the lower bound, inclusive
     * @param max   the upper bound, inclusive
     * @return a number within the bounds
     */
    public static float clamp(float value, float min, float max) {
        Range range = new Range(min, max);
        return Math.min(Math.max(range.min, value), range.max);
    }

    /**
     * Restricts an integer's value within a provided range.
     *
     * @param value any real number
     * @param min   the lower bound, inclusive
     * @param max   the upper bound, inclusive
     * @return a number within the bounds
     */
    public static int clamp(int value, int min, int max) {
        return (int) clamp((float) value, (float) min, (float) max);
    }

    // Random Number Methods

    /**
     * Generates a random integer between the two provided bounds.
     *
     * @param min the lower bound (inclusive)
     * @param max the maximum bound (inclusive)
     * @return the random float
     */
    public static float random(float min, float max) {
        Range range = new Range(min, max);
        return (float) (Math.random() * (range.max - range.min + Float.MIN_VALUE)) + range.min;
    }

    /**
     * Generates a random integer between the two provided bounds.
     *
     * @param min the lower bound (inclusive)
     * @param max the maximum bound (inclusive)
     * @return The random integer.
     */
    public static int random(int min, int max) {
        Range range = new Range(min, max);
        return (int) ((Math.random() * (range.max - range.min + 1)) + range.min);
    }

    // Rounding Methods

    public static float round(float value, int decimalPlaces) {
        value += 5f / Math.pow(10, decimalPlaces + 1);
        return truncate(value, decimalPlaces);
    }

    public static float truncate(float value, int decimalPlaces) {
        String s = String.valueOf(value);
        return Float.parseFloat(s.substring(0, s.indexOf('.') + decimalPlaces + 1));
    }

    // Equality Methods

    /**
     * Determines whether two floats are approximately equal within 6 decimal places.
     *
     * @param num1 a number
     * @param num2 another number
     * @return if they are equal
     */
    public static boolean equals(float num1, float num2) {
        return Math.abs(num1 - num2) <= EPSILON * Math.max(1.0, Math.max(Math.abs(num1), Math.abs(num2)));
    }

    // Helper Class

    /**
     * Stores a minimum and maximum value and ensures they are int the correct order.
     */
    public static class Range {
        public float min, max;

        public Range(float min, float max) {
            if (min > max) {
                float temp = min;
                min = max;
                max = temp;
            }
            this.min = min;
            this.max = max;
        }
    }

}
