package slavsquatsuperstar.test.mathtests;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.MathUtils;

import static org.junit.jupiter.api.Assertions.*;
import static slavsquatsuperstar.test.TestUtils.assertFloatEquals;

/**
 * Unit tests for {@link MathUtils} class.
 *
 * @author SlavSquatSuperstar
 */
public class MathUtilTests {

    // Equality

    @Test
    public void floatEpsilonEqualsZero() {
        assertFloatEquals(0f, Float.MIN_VALUE);
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
        assertTrue(NumberUtils.min(nums) >= min);
        assertTrue(NumberUtils.max(nums) <= max);
    }

    @Test
    public void randomNegativeIntsSuccess() {
        int[] nums = new int[100];
        int min = -20;
        int max = 0;
        for (int i = 0; i < nums.length; i++)
            nums[i] = MathUtils.random(min, max);
        assertTrue(NumberUtils.min(nums) >= min);
        assertTrue(NumberUtils.max(nums) <= max);
    }

    @Test
    public void randomMixedIntsSuccess() {
        int[] nums = new int[100];
        int min = -10;
        int max = 10;
        for (int i = 0; i < nums.length; i++)
            nums[i] = MathUtils.random(min, max);
        assertTrue(NumberUtils.min(nums) >= min);
        assertTrue(NumberUtils.max(nums) <= max);
    }

    // Rounding

    @Test
    public void randomFloatsSuccess() {
        float[] nums = new float[100];
        float min = 0f;
        float max = 20f;
        for (int i = 0; i < nums.length; i++)
            nums[i] = MathUtils.random(min, max);
        assertTrue(NumberUtils.min(nums) >= min);
        assertTrue(NumberUtils.max(nums) <= max);
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

}
