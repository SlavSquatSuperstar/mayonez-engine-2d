package mayonez.math;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.Random} class.
 *
 * @author SlavSquatSuperstar
 */
public class RandomTests {

    @Test
    public void randomPositiveIntsSuccess() {
        int[] nums = new int[100];
        int min = 0;
        int max = 20;
        for (var i = 0; i < nums.length; i++)
            nums[i] = Random.randomInt(min, max);
        assertTrue(IntMath.min(nums) >= min);
        assertTrue(IntMath.max(nums) <= max);
    }

    @Test
    public void randomNegativeIntsSuccess() {
        int[] nums = new int[100];
        int min = -20;
        int max = 0;
        for (var i = 0; i < nums.length; i++)
            nums[i] = Random.randomInt(min, max);
        assertTrue(IntMath.min(nums) >= min);
        assertTrue(IntMath.max(nums) <= max);
    }

    @Test
    public void randomMixedIntsSuccess() {
        int[] nums = new int[100];
        int min = -10;
        int max = 10;
        for (var i = 0; i < nums.length; i++)
            nums[i] = Random.randomInt(min, max);
        assertTrue(IntMath.min(nums) >= min);
        assertTrue(IntMath.max(nums) <= max);
    }

    @Test
    public void randomFloatsSuccess() {
        float[] nums = new float[100];
        float min = 0f;
        float max = 20f;
        for (var i = 0; i < nums.length; i++)
            nums[i] = Random.randomFloat(min, max);
        assertTrue(FloatMath.min(nums) >= min);
        assertTrue(FloatMath.max(nums) <= max);
    }

}
