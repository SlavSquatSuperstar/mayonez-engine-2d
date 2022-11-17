package slavsquatsuperstar.mayonez.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static slavsquatsuperstar.test.TestUtils.assertFloatEquals;

/**
 * Unit tests for the {@link MathUtils} class.
 *
 * @author SlavSquatSuperstar
 */
public class MathUtilsTests {

    // Equality
    @Test
    public void floatEqualsSuccess() {
        assertTrue(MathUtils.equals(0.0000010f, 0.0000019f));
        assertTrue(MathUtils.equals(-0.0000005f, 0.0000005f));
        assertTrue(MathUtils.equals(0.0000010f, 0.0000020f));
        assertTrue(MathUtils.equals(0.0001f, 0.0002f, 0.0001f));
    }

    @Test
    public void floatEqualsFail() {
        assertFalse(MathUtils.equals(0.0000010f, 0.0000021f));
        assertFalse(MathUtils.equals(-0.0000005f, 0.0000006f));
        assertFalse(MathUtils.equals(0.0001f, 0.0002f, 0.00005f));
    }

    @Test
    public void floatEpsilonEqualsZero() {
        assertFloatEquals(0f, Float.MIN_VALUE);
    }

    // Pythagorean Theorem

    @Test
    public void squaredSuccess() {
        assertFloatEquals(4, MathUtils.squared(2));
        assertFloatEquals(3.14f * 3.14f, MathUtils.squared(3.14f));
    }

    @Test
    public void sqrtSuccess() {
        assertFloatEquals(2, MathUtils.sqrt(4));
        assertFloatEquals(3.14f, MathUtils.sqrt(3.14f * 3.14f));
    }

    @Test
    public void hypotSuccess() {
        assertFloatEquals(5, MathUtils.hypot(3, 4));
        assertFloatEquals(MathUtils.sqrt(2), MathUtils.hypot(1, 1));
    }

    @Test
    public void invHypotSuccess() {
        assertFloatEquals(4, MathUtils.invHypot(5, 3));
        assertFloatEquals(1, MathUtils.invHypot(MathUtils.sqrt(2), 1));
    }

    // Accumulator

    @Test
    public void findAverageAndSumSuccess() {
        assertEquals(MathUtils.avg(1, 2, 3, 4, 5), 3);
        assertFloatEquals(MathUtils.avg(1f, 2f, 3f, 4f), 2.5f);

        assertEquals(MathUtils.sum(1, 2, 3, 4), 10);
        assertFloatEquals(MathUtils.sum(0.5f, 1f, 1.5f, 2f, 2.5f), 7.5f);
    }

    // Find Extreme
    @Test
    public void findMinAndMaxSuccess() {
        assertEquals(MathUtils.min(-6, 1, 2, 3, 5), -6);
        assertFloatEquals(MathUtils.min(-6.5f, 1, 2, 3, 5.5f), -6.5f);

        assertEquals(MathUtils.max(-6, 1, 2, 3, 5), 5);
        assertFloatEquals(MathUtils.max(-6.5f, 1, 2, 3, 5.5f), 5.5f);
    }

    // Clamp / Range

    @Test
    public void clampUpSuccess() {
        assertEquals(0, MathUtils.clamp(-1, 0, 5));
        assertFloatEquals(-5f, MathUtils.clamp(-6f, -5f, 0f));
        assertFloatEquals(2.5f, MathUtils.clamp(0f, 2.5f, 7.5f));
    }

    @Test
    public void clampDownSuccess() {
        assertEquals(5, MathUtils.clamp(6, 0, 5));
        assertFloatEquals(0f, MathUtils.clamp(1f, -5f, 0f));
        assertFloatEquals(7.5f, MathUtils.clamp(10f, 2.5f, 7.5f));
    }

    @Test
    public void clampNoneSuccess() {
        assertEquals(1, MathUtils.clamp(1, 0, 5));
        assertFloatEquals(-1f, MathUtils.clamp(-1f, -5f, 0f));
    }

    @Test
    public void inRangeSuccess() {
        assertTrue(MathUtils.inRange(5, 1, 10));
        assertTrue(MathUtils.inRange(-5, -10, -1));
        assertTrue(MathUtils.inRange(0, -5, 5));
    }

    @Test
    public void inRangeFailTooLow() {
        assertFalse(MathUtils.inRange(0, 1, 10));
        assertFalse(MathUtils.inRange(-11, -10, -1));
        assertFalse(MathUtils.inRange(-6, -5, 5));
    }

    @Test
    public void inRangeFailTooHigh() {
        assertFalse(MathUtils.inRange(11, 1, 10));
        assertFalse(MathUtils.inRange(0, -10, -1));
        assertFalse(MathUtils.inRange(6, -5, 5));
    }

    // Random

    @Test
    public void randomPositiveIntsSuccess() {
        int[] nums = new int[100];
        int min = 0;
        int max = 20;
        for (int i = 0; i < nums.length; i++)
            nums[i] = MathUtils.random(min, max);
        assertTrue(min(nums) >= min);
        assertTrue(max(nums) <= max);
    }

    @Test
    public void randomNegativeIntsSuccess() {
        int[] nums = new int[100];
        int min = -20;
        int max = 0;
        for (int i = 0; i < nums.length; i++)
            nums[i] = MathUtils.random(min, max);
        assertTrue(min(nums) >= min);
        assertTrue(max(nums) <= max);
    }

    @Test
    public void randomMixedIntsSuccess() {
        int[] nums = new int[100];
        int min = -10;
        int max = 10;
        for (int i = 0; i < nums.length; i++)
            nums[i] = MathUtils.random(min, max);
        assertTrue(min(nums) >= min);
        assertTrue(max(nums) <= max);
    }

    // Rounding

    @Test
    public void randomFloatsSuccess() {
        float[] nums = new float[100];
        float min = 0f;
        float max = 20f;
        for (int i = 0; i < nums.length; i++)
            nums[i] = MathUtils.random(min, max);

        assertTrue(MathUtils.min(nums) >= min);
        assertTrue(MathUtils.max(nums) <= max);
    }

    @Test
    public void roundUpSuccess() {
        assertFloatEquals(0.07f, MathUtils.round(0.069f, 2));
        assertFloatEquals(0.007f, MathUtils.round(0.0069f, 3));
    }

    @Test
    public void roundDownSuccess() {
        assertFloatEquals(0.04f, MathUtils.round(0.0420f, 2));
        assertFloatEquals(0.004f, MathUtils.round(0.00420f, 3));
        assertFloatEquals(0.06f, MathUtils.truncate(0.069f, 2));
    }

    // Trig

    @Test
    public void angleConversionSuccess() {
        assertFloatEquals(180, MathUtils.toDegrees(MathUtils.PI));
        assertFloatEquals(MathUtils.PI, MathUtils.toRadians(180));
    }

    @Test
    public void hypotenuseNDimensionsSuccess() {
        assertFloatEquals(10, MathUtils.hypot(6, 8));
        assertFloatEquals(13, MathUtils.hypot(3, 4, 12));
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
