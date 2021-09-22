package slavsquatsuperstar.test.mathtests;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.math.MathUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MathUtils} class.
 *
 * @author SlavSquatSuperstar
 */
public class MathUtilTests {

    // Equality

    @Test
    public void epsilonEqualsZero() {
        assertEquals(0f, Float.MIN_VALUE, MathUtils.EPSILON);
    }

    // Accumulator

    @Test
    public void findAverageSuccess() {
        assertEquals(MathUtils.avg(1, 2, 3, 4, 5), 3);
        assertEquals(MathUtils.avg(1f, 2f, 3f, 4f), 2.5f, MathUtils.EPSILON);

    }

    @Test
    public void findSumSuccess() {
        assertEquals(MathUtils.sum(1, 2, 3, 4), 10);
        assertEquals(MathUtils.sum(0.5f, 1f, 1.5f, 2f, 2.5f), 7.5f, MathUtils.EPSILON);
    }

    // Find Extreme
    @Test
    public void findMinSuccess() {
        assertEquals(MathUtils.min(-6, 1, 2, 3, 5), -6);
        assertEquals(MathUtils.min(-6.5f, 1, 2, 3, 5.5f), -6.5f, MathUtils.EPSILON);
    }

    @Test
    public void findMaxSuccess() {
        assertEquals(MathUtils.max(-6, 1, 2, 3, 5), 5);
        assertEquals(MathUtils.max(-6.5f, 1, 2, 3, 5.5f), 5.5f, MathUtils.EPSILON);
    }

    // Clamp / Range

    @Test
    public void clampUpSuccess() {
        assertEquals(0, MathUtils.clamp(-1, 0, 5));
        assertEquals(-5f, MathUtils.clamp(-6f, -5f, 0f), MathUtils.EPSILON);
        assertEquals(2.5f, MathUtils.clamp(0f, 2.5f, 7.5f), MathUtils.EPSILON);
    }

    @Test
    public void clampDownSuccess() {
        assertEquals(5, MathUtils.clamp(6, 0, 5));
        assertEquals(0f, MathUtils.clamp(1f, -5f, 0f), MathUtils.EPSILON);
        assertEquals(7.5f, MathUtils.clamp(10f, 2.5f, 7.5f), MathUtils.EPSILON);
    }

    @Test
    public void clampNoneSuccess() {
        assertEquals(1, MathUtils.clamp(1, 0, 5));
        assertEquals(-1f, MathUtils.clamp(-1f, -5f, 0f), MathUtils.EPSILON);
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
        Logger.log("Min: %d, Max: %d", NumberUtils.min(nums), NumberUtils.max(nums));
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
        Logger.log("Min: %d, Max: %d", NumberUtils.min(nums), NumberUtils.max(nums));
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
        assertEquals(0.07f, MathUtils.round(0.069f, 2), MathUtils.EPSILON);
        assertEquals(0.007f, MathUtils.round(0.0069f, 3), MathUtils.EPSILON);
    }

    @Test
    public void roundDownSuccess() {
        assertEquals(0.04f, MathUtils.round(0.0420f, 2), MathUtils.EPSILON);
        assertEquals(0.004f, MathUtils.round(0.00420f, 3), MathUtils.EPSILON);
        assertEquals(0.06f, MathUtils.truncate(0.069f, 2), MathUtils.EPSILON);
    }

    // Trig

    @Test
    public void angleConversionSuccess() {
        assertEquals(180, MathUtils.toDegrees(MathUtils.PI), MathUtils.EPSILON);
        assertEquals(MathUtils.PI, MathUtils.toRadians(180), MathUtils.EPSILON);
    }

    @Test
    public void pythagoreanNDimensionsSuccess() {
        assertEquals(10, MathUtils.pythagorean(6, 8), MathUtils.EPSILON);
        assertEquals(13, MathUtils.pythagorean(3, 4, 12), MathUtils.EPSILON);
    }

}
