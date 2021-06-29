import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.util.MathUtils;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class MathTests {

    @Test
    public void sumIntsIsCorrect() {
        int[] nums = {1, 2, 3, 4};
        assertEquals(MathUtils.sum(nums), 10);
    }

    @Test
    public void sumFloatsIsCorrect() {
        float[] nums = {1f, 2f, 3f, 4f};
        assertEquals(MathUtils.sum(nums), 10f, MathUtils.EPSILON);
    }

    @Test
    public void averageIntsIsCorrect() {
        int[] nums = {1, 2, 3, 4, 5};
        assertEquals(MathUtils.average(nums), 3);
    }

    @Test
    public void averageFloatsIsCorrect() {
        float[] nums = {1f, 2f, 3f, 4f};
        assertEquals(MathUtils.average(nums), 2.5f, MathUtils.EPSILON);
    }

    @Test
    public void minFloatEqualsZero() {
        assertTrue(MathUtils.equals(0f, Float.MIN_VALUE));
    }

    @Test
    public void unitVectorOfZero() {
        Vector2 v = new Vector2();
        assertTrue(v.unit().equals(v));
    }

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
        Logger.log("Min: %d, Max: %d", NumberUtils.min(nums), NumberUtils.max(nums));
        assertTrue(NumberUtils.min(nums) >= min);
        assertTrue(NumberUtils.max(nums) <= max);
    }

    @Test
    public void randomFloatsSuccess() {
        float[] nums = new float[100];
        float min = 0f;
        float max = 20f;
        for (int i = 0; i < nums.length; i++)
            nums[i] = MathUtils.random(min, max);
        Logger.log("Min: %f, Max: %f", NumberUtils.min(nums), NumberUtils.max(nums));
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
    }

}
