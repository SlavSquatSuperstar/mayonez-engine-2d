package mayonez.math;

import org.junit.jupiter.api.*;

import static mayonez.math.MathTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.MathUtils} class.
 *
 * @author SlavSquatSuperstar
 */
class FloatMathTest {

    // Equality
    @Test
    void floatEqualsSuccess() {
        assertTrue(MathUtils.equals(0.0000010f, 0.0000019f));
        assertTrue(MathUtils.equals(-0.0000005f, 0.0000005f));
        assertTrue(MathUtils.equals(0.0000010f, 0.0000020f));
        assertTrue(MathUtils.equals(0.0001f, 0.0002f, 0.0001f));
    }

    @Test
    void floatEqualsFail() {
        assertFalse(MathUtils.equals(0.0000010f, 0.0000021f));
        assertFalse(MathUtils.equals(-0.0000005f, 0.0000006f));
        assertFalse(MathUtils.equals(0.0001f, 0.0002f, 0.00005f));
    }

    @Test
    void floatEpsilonEqualsZero() {
        assertFloatEquals(0f, Float.MIN_VALUE);
    }

    // Pythagorean Theorem

    @Test
    void squaredSuccess() {
        assertFloatEquals(4, MathUtils.squared(2));
        assertFloatEquals(3.14f * 3.14f, MathUtils.squared(3.14f));
    }

    @Test
    void sqrtSuccess() {
        assertFloatEquals(2, MathUtils.sqrt(4));
        assertFloatEquals(3.14f, MathUtils.sqrt(3.14f * 3.14f));
    }

    @Test
    void hypotSuccess() {
        assertFloatEquals(5, MathUtils.hypot(3, 4));
        assertFloatEquals(MathUtils.sqrt(2), MathUtils.hypot(1, 1));
    }

    @Test
    void invHypotSuccess() {
        assertFloatEquals(4, MathUtils.invHypot(5, 3));
        assertFloatEquals(1, MathUtils.invHypot(MathUtils.sqrt(2), 1));
    }

    // Accumulator

    @Test
    void findAverageAndSumSuccess() {
        assertEquals(MathUtils.avg(1, 2, 3, 4, 5), 3);
        assertFloatEquals(MathUtils.avg(1f, 2f, 3f, 4f), 2.5f);

        assertEquals(MathUtils.sum(1, 2, 3, 4), 10);
        assertFloatEquals(MathUtils.sum(0.5f, 1f, 1.5f, 2f, 2.5f), 7.5f);
    }

    // Find Extreme
    @Test
    void findMinAndMaxSuccess() {
        assertEquals(MathUtils.min(-6, 1, 2, 3, 5), -6);
        assertFloatEquals(MathUtils.min(-6.5f, 1, 2, 3, 5.5f), -6.5f);

        assertEquals(MathUtils.max(-6, 1, 2, 3, 5), 5);
        assertFloatEquals(MathUtils.max(-6.5f, 1, 2, 3, 5.5f), 5.5f);
    }

    // Rounding

    @Test
    void roundUpSuccess() {
        assertFloatEquals(0.07f, MathUtils.round(0.069f, 2));
        assertFloatEquals(0.007f, MathUtils.round(0.0069f, 3));
    }

    @Test
    void roundDownSuccess() {
        assertFloatEquals(0.04f, MathUtils.round(0.0420f, 2));
        assertFloatEquals(0.004f, MathUtils.round(0.00420f, 3));
        assertFloatEquals(0.06f, MathUtils.truncate(0.069f, 2));
    }

    // Trig

    @Test
    void angleConversionSuccess() {
        assertFloatEquals(180, MathUtils.toDegrees(MathUtils.PI));
        assertFloatEquals(MathUtils.PI, MathUtils.toRadians(180));
    }

    @Test
    void hypotenuseNDimensionsSuccess() {
        assertFloatEquals(10, MathUtils.hypot(6, 8));
        assertFloatEquals(13, MathUtils.hypot(3, 4, 12));
    }


}
