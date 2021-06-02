package com.slavsquatsuperstar.util;

import com.slavsquatsuperstar.mayonez.Vector2;

public final class MathUtil {

    private MathUtil() {
    }

    // Average Methods

    public static float average(float[] values) {
        float sum = 0;
        for (float value : values)
            sum += value;
        return sum / values.length;
    }

    public static int average(int[] values) {
        int sum = 0;
        for (int value : values)
            sum += value;
        return sum / values.length;
    }

    // Clamp Methods

    public static float clamp(float value, float lowerBound, float upperBound) {
        return (value > upperBound ? upperBound : (value < lowerBound ? lowerBound : value));
    }

    public static int clamp(int value, int lowerBound, int upperBound) {
        return (value > upperBound ? upperBound : (value < lowerBound ? lowerBound : value));
    }

    // Random Number Methods

    public static float random(float lowerBound, float upperBound) {
        return (float) ((Math.random() * (upperBound - lowerBound)) + lowerBound);
    }

    public static int random(int lowerBound, int upperBound) {
        return (int) ((Math.random() * (upperBound - lowerBound)) + lowerBound - 1);
    }

    // Rounding Methods

    public static float round(float value, int decimalPlaces) {
        value += 5 / Math.pow(10, decimalPlaces + 1);
        return truncate(value, decimalPlaces);
    }

    public static float truncate(float value, int decimalPlaces) {
        return (float) ((value * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces));
    }

    // Vector Methods

    // See: https://www.youtube.com/watch?v=dlUcIGnaAnk
    public static Vector2 rotate(Vector2 v, float degrees, Vector2 origin) {
        // Translate the vector space to the origin (0, 0)
        float x = v.x - origin.x;
        float y = v.y - origin.y;

        // Rotate the point around the new origin
        float cos = (float) Math.cos(Math.toRadians(degrees));
        float sin = (float) Math.sin(Math.toRadians(degrees));

        float xPrime = (x * cos) - (y * sin);
        float yPrime = (y * sin) + (x * sin);

        // Revert the vector space to the old point
        return new Vector2(xPrime, yPrime).add(origin);
    }

    // Comparison Methods

    public static boolean equals(float a, float b) {
        return Math.abs(a - b) <= Float.MIN_VALUE * Math.max(1.0, Math.max(Math.abs(a), Math.abs(b)));
    }

}
