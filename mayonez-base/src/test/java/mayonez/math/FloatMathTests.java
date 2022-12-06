package mayonez.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static mayonez.test.TestUtils.assertFloatEquals;

/**
 * Unit tests for the {@link mayonez.math.FloatMath} and {@link mayonez.math.IntMath} class.
 *
 * @author SlavSquatSuperstar
 */
public class FloatMathTests {

    // Equality
    @Test
    public void floatEqualsSuccess() {
        assertTrue(FloatMath.equals(0.0000010f, 0.0000019f));
        assertTrue(FloatMath.equals(-0.0000005f, 0.0000005f));
        assertTrue(FloatMath.equals(0.0000010f, 0.0000020f));
        assertTrue(FloatMath.equals(0.0001f, 0.0002f, 0.0001f));
    }

    @Test
    public void floatEqualsFail() {
        assertFalse(FloatMath.equals(0.0000010f, 0.0000021f));
        assertFalse(FloatMath.equals(-0.0000005f, 0.0000006f));
        assertFalse(FloatMath.equals(0.0001f, 0.0002f, 0.00005f));
    }

    @Test
    public void floatEpsilonEqualsZero() {
        assertFloatEquals(0f, Float.MIN_VALUE);
    }

    // Pythagorean Theorem

    @Test
    public void squaredSuccess() {
        assertFloatEquals(4, FloatMath.squared(2));
        assertFloatEquals(3.14f * 3.14f, FloatMath.squared(3.14f));
    }

    @Test
    public void sqrtSuccess() {
        assertFloatEquals(2, FloatMath.sqrt(4));
        assertFloatEquals(3.14f, FloatMath.sqrt(3.14f * 3.14f));
    }

    @Test
    public void hypotSuccess() {
        assertFloatEquals(5, FloatMath.hypot(3, 4));
        assertFloatEquals(FloatMath.sqrt(2), FloatMath.hypot(1, 1));
    }

    @Test
    public void invHypotSuccess() {
        assertFloatEquals(4, FloatMath.invHypot(5, 3));
        assertFloatEquals(1, FloatMath.invHypot(FloatMath.sqrt(2), 1));
    }

    // Accumulator

    @Test
    public void findAverageAndSumSuccess() {
        assertEquals(IntMath.avg(1, 2, 3, 4, 5), 3);
        assertFloatEquals(FloatMath.avg(1f, 2f, 3f, 4f), 2.5f);

        assertEquals(IntMath.sum(1, 2, 3, 4), 10);
        assertFloatEquals(FloatMath.sum(0.5f, 1f, 1.5f, 2f, 2.5f), 7.5f);
    }

    // Find Extreme
    @Test
    public void findMinAndMaxSuccess() {
        assertEquals(IntMath.min(-6, 1, 2, 3, 5), -6);
        assertFloatEquals(FloatMath.min(-6.5f, 1, 2, 3, 5.5f), -6.5f);

        assertEquals(IntMath.max(-6, 1, 2, 3, 5), 5);
        assertFloatEquals(FloatMath.max(-6.5f, 1, 2, 3, 5.5f), 5.5f);
    }

    // Clamp / Range

    @Test
    public void clampUpSuccess() {
        assertEquals(0, FloatMath.clamp(-1, 0, 5));
        assertFloatEquals(-5f, FloatMath.clamp(-6f, -5f, 0f));
        assertFloatEquals(2.5f, FloatMath.clamp(0f, 2.5f, 7.5f));
    }

    @Test
    public void clampDownSuccess() {
        assertEquals(5, FloatMath.clamp(6, 0, 5));
        assertFloatEquals(0f, FloatMath.clamp(1f, -5f, 0f));
        assertFloatEquals(7.5f, FloatMath.clamp(10f, 2.5f, 7.5f));
    }

    @Test
    public void clampNoneSuccess() {
        assertEquals(1, FloatMath.clamp(1, 0, 5));
        assertFloatEquals(-1f, FloatMath.clamp(-1f, -5f, 0f));
    }

    @Test
    public void inRangeSuccess() {
        assertTrue(FloatMath.inRange(5, 1, 10));
        assertTrue(FloatMath.inRange(-5, -10, -1));
        assertTrue(FloatMath.inRange(0, -5, 5));
    }

    @Test
    public void inRangeFailTooLow() {
        assertFalse(FloatMath.inRange(0, 1, 10));
        assertFalse(FloatMath.inRange(-11, -10, -1));
        assertFalse(FloatMath.inRange(-6, -5, 5));
    }

    @Test
    public void inRangeFailTooHigh() {
        assertFalse(FloatMath.inRange(11, 1, 10));
        assertFalse(FloatMath.inRange(0, -10, -1));
        assertFalse(FloatMath.inRange(6, -5, 5));
    }

    // Rounding

    @Test
    public void roundUpSuccess() {
        assertFloatEquals(0.07f, FloatMath.round(0.069f, 2));
        assertFloatEquals(0.007f, FloatMath.round(0.0069f, 3));
    }

    @Test
    public void roundDownSuccess() {
        assertFloatEquals(0.04f, FloatMath.round(0.0420f, 2));
        assertFloatEquals(0.004f, FloatMath.round(0.00420f, 3));
        assertFloatEquals(0.06f, FloatMath.truncate(0.069f, 2));
    }

    // Trig

    @Test
    public void angleConversionSuccess() {
        assertFloatEquals(180, FloatMath.toDegrees(FloatMath.PI));
        assertFloatEquals(FloatMath.PI, FloatMath.toRadians(180));
    }

    @Test
    public void hypotenuseNDimensionsSuccess() {
        assertFloatEquals(10, FloatMath.hypot(6, 8));
        assertFloatEquals(13, FloatMath.hypot(3, 4, 12));
    }

    private static int max(int[] nums) {
        int max = nums[0];
        for (int i = 1; i < nums.length; i++)
            if (nums[i] > max) max = nums[i];
        return max;
    }

    private static int min(int[] nums) {
        int min = nums[0];
        for (int i = 1; i < nums.length; i++)
            if (nums[i] < min) min = nums[i];
        return min;
    }


}
