package com.slavsquatsuperstar.util;

public final class MathUtil {

    public static final float EPSILON = 1e-6f;

    private MathUtil() {
    }

    // Accumulator Methods

    public static float sum(float[] values) {
        float sum = 0;
        for (float value : values)
            sum += value;
        return sum;
    }

    public static int sum(int[] values) {
        int sum = 0;
        for (int value : values)
            sum += value;
        return sum;
    }

    public static float average(float[] values) {
        return sum(values) / values.length;
    }

    public static int average(int[] values) {
        return sum(values) / values.length;
    }

    // Clamp Methods

    public static float clamp(float value, float lowerBound, float upperBound) {
        return (value > upperBound ? upperBound : (value < lowerBound ? lowerBound : value));
    }

    public static int clamp(int value, int lowerBound, int upperBound) {
        return (value > upperBound ? upperBound : (value < lowerBound ? lowerBound : value));
    }

    // Random Number Methods

    /**
     * Generates a random integer between the two provided bounds.
     *
     * @param lowerBound The minimum allowed value (inclusive).
     * @param upperBound The maximum allowed value (inclusive).
     * @return The random float.
     */
    public static float random(float lowerBound, float upperBound) {
        return (float) (Math.random() * (upperBound - lowerBound + EPSILON)) + lowerBound;
    }

    /**
     * Generates a random integer between the two provided bounds.
     *
     * @param lowerBound The minimum allowed value (inclusive).
     * @param upperBound The maximum allowed value (inclusive).
     * @return The random integer.
     */
    public static int random(int lowerBound, int upperBound) {
        return (int) (Math.random() * (upperBound - lowerBound + 1)) + lowerBound;
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

    // Comparison Methods

    public static boolean equals(float a, float b) {
        return Math.abs(a - b) <= EPSILON * Math.max(1.0, Math.max(Math.abs(a), Math.abs(b)));
    }

}
