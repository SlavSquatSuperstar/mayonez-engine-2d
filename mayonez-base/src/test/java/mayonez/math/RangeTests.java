package mayonez.math;

import org.junit.jupiter.api.Test;

import static mayonez.test.TestUtils.assertFloatEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for the {@link mayonez.math.Range} class.
 *
 * @author SlavSquatSuperstar
 */
public class RangeTests {

    @Test
    public void rangeCorrectMinMax() {
        Range r1 = new Range(2, 8);
        Range r2 = new Range(8, 2);
        assertFloatEquals(2f, r1.min);
        assertFloatEquals(8f, r1.max);
        assertFloatEquals(2f, r2.min);
        assertFloatEquals(8f, r2.max);
    }

    @Test
    public void rangeEquals() {
        Range r1 = new Range(-3, 4);
        Range r2 = new Range(-3, 4);
        Range r3 = new Range(-4, 3);
        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
    }

    @Test
    public void rangeContainsRange() {
        Range r1 = new Range(0, 5);
        Range r2 = new Range(-2, 7);
        Range r3 = new Range(1, 6);
        assertTrue(r2.contains(r1)); // contains
        assertFalse(r1.contains(r2)); // not contains
        assertFalse(r1.contains(r3)); // overlap
    }

    // Clamp / In Range Methods

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

}