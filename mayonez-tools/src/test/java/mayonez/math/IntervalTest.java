package mayonez.math;

import org.junit.jupiter.api.*;

import static mayonez.math.MathTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.Interval} class.
 *
 * @author SlavSquatSuperstar
 */
class IntervalTest {

    @Test
    void rangeCorrectMinMax() {
        var r1 = new Interval(2, 8);
        var r2 = new Interval(8, 2);
        assertFloatEquals(2f, r1.min);
        assertFloatEquals(8f, r1.max);
        assertFloatEquals(2f, r2.min);
        assertFloatEquals(8f, r2.max);
    }

    @Test
    void rangeEquals() {
        var r1 = new Interval(-3, 4);
        var r2 = new Interval(-3, 4);
        var r3 = new Interval(-4, 3);
        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
    }

    @Test
    void rangeContainsRange() {
        var r1 = new Interval(0, 5);
        var r2 = new Interval(-2, 7);
        var r3 = new Interval(1, 6);
        assertTrue(r2.contains(r1)); // contains
        assertFalse(r1.contains(r2)); // not contains
        assertFalse(r1.contains(r3)); // overlap
    }

    // Clamp / In Range Methods

    @Test
    void clampUpSuccess() {
        assertEquals(0, MathUtils.clamp(-1, 0, 5));
        assertFloatEquals(-5f, MathUtils.clamp(-6f, -5f, 0f));
        assertFloatEquals(2.5f, MathUtils.clamp(0f, 2.5f, 7.5f));
    }

    @Test
    void clampDownSuccess() {
        assertEquals(5, MathUtils.clamp(6, 0, 5));
        assertFloatEquals(0f, MathUtils.clamp(1f, -5f, 0f));
        assertFloatEquals(7.5f, MathUtils.clamp(10f, 2.5f, 7.5f));
    }

    @Test
    void clampNoneSuccess() {
        assertEquals(1, MathUtils.clamp(1, 0, 5));
        assertFloatEquals(-1f, MathUtils.clamp(-1f, -5f, 0f));
    }

    @Test
    void inRangeSuccess() {
        assertTrue(MathUtils.inRange(5, 1, 10));
        assertTrue(MathUtils.inRange(-5, -10, -1));
        assertTrue(MathUtils.inRange(0, -5, 5));
    }

    @Test
    void inRangeFailTooLow() {
        assertFalse(MathUtils.inRange(0, 1, 10));
        assertFalse(MathUtils.inRange(-11, -10, -1));
        assertFalse(MathUtils.inRange(-6, -5, 5));
    }

    @Test
    void inRangeFailTooHigh() {
        assertFalse(MathUtils.inRange(11, 1, 10));
        assertFalse(MathUtils.inRange(0, -10, -1));
        assertFalse(MathUtils.inRange(6, -5, 5));
    }

    // Lerp Methods

    @Test
    void lerpSuccess() {
        var range = new Interval(0, 5);
        assertEquals(2, range.lerp(0.4f)); // middle
        assertEquals(6, range.lerp(1.2f)); // past end
        assertEquals(-4, range.lerp(-0.8f)); // negative
    }

    @Test
    void invLerpSuccess() {
        var range = new Interval(0, 5);
        assertEquals(0.4f, range.invLerp(2)); // middle
        assertEquals(1.2f, range.invLerp(6)); // past end
        assertEquals(-0.8f, range.invLerp(-4)); // negative
    }

}
