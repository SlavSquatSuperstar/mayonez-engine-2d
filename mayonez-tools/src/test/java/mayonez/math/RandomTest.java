package mayonez.math;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.Random} class.
 *
 * @author SlavSquatSuperstar
 */
class RandomTest {

    @Test
    void randomPositiveIntsWithinBounds() {
        var nums = new int[100];
        var min = 0;
        var max = 20;
        for (var i = 0; i < nums.length; i++) {
            nums[i] = Random.randomInt(min, max);
        }
        assertTrue(FloatMath.min(nums) >= min);
        assertTrue(FloatMath.max(nums) <= max);
    }

    @Test
    void randomNegativeIntsWithinBounds() {
        var nums = new int[100];
        var min = -20;
        var max = 0;
        for (var i = 0; i < nums.length; i++) {
            nums[i] = Random.randomInt(min, max);
        }
        assertTrue(FloatMath.min(nums) >= min);
        assertTrue(FloatMath.max(nums) <= max);
    }

    @Test
    void randomMixedIntsWithinBounds() {
        var nums = new int[100];
        var min = -10;
        var max = 10;
        for (var i = 0; i < nums.length; i++) {
            nums[i] = Random.randomInt(min, max);
        }
        assertTrue(FloatMath.min(nums) >= min);
        assertTrue(FloatMath.max(nums) <= max);
    }

    @Test
    void randomFloatsWithinBounds() {
        var nums = new float[100];
        var min = 0f;
        var max = 20f;
        for (var i = 0; i < nums.length; i++) {
            nums[i] = Random.randomFloat(min, max);
        }
        assertTrue(FloatMath.min(nums) >= min);
        assertTrue(FloatMath.max(nums) <= max);
    }

}
