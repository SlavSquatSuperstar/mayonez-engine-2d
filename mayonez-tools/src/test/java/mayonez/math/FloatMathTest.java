package mayonez.math;

import org.junit.jupiter.api.*;

import static mayonez.math.MathTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.FloatMath} and {@link mayonez.math.FloatMath} classes.
 *
 * @author SlavSquatSuperstar
 */
class FloatMathTest {

    // Equality
    @Test
    void floatEqualsSuccess() {
        assertTrue(FloatMath.equals(0.0000010f, 0.0000019f));
        assertTrue(FloatMath.equals(-0.0000005f, 0.0000005f));
        assertTrue(FloatMath.equals(0.0000010f, 0.0000020f));
        assertTrue(FloatMath.equals(0.0001f, 0.0002f, 0.0001f));
    }

    @Test
    void floatEqualsFail() {
        assertFalse(FloatMath.equals(0.0000010f, 0.0000021f));
        assertFalse(FloatMath.equals(-0.0000005f, 0.0000006f));
        assertFalse(FloatMath.equals(0.0001f, 0.0002f, 0.00005f));
    }

    @Test
    void floatEpsilonEqualsZero() {
        assertFloatEquals(0f, Float.MIN_VALUE);
    }

    // Pythagorean Theorem

    @Test
    void squaredSuccess() {
        assertFloatEquals(4, FloatMath.squared(2));
        assertFloatEquals(3.14f * 3.14f, FloatMath.squared(3.14f));
    }

    @Test
    void sqrtSuccess() {
        assertFloatEquals(2, FloatMath.sqrt(4));
        assertFloatEquals(3.14f, FloatMath.sqrt(3.14f * 3.14f));
    }

    @Test
    void hypotSuccess() {
        assertFloatEquals(5, FloatMath.hypot(3, 4));
        assertFloatEquals(FloatMath.sqrt(2), FloatMath.hypot(1, 1));
    }

    @Test
    void invHypotSuccess() {
        assertFloatEquals(4, FloatMath.invHypot(5, 3));
        assertFloatEquals(1, FloatMath.invHypot(FloatMath.sqrt(2), 1));
    }

    // Accumulator

    @Test
    void findAverageAndSumSuccess() {
        assertEquals(FloatMath.avg(1, 2, 3, 4, 5), 3);
        assertFloatEquals(FloatMath.avg(1f, 2f, 3f, 4f), 2.5f);

        assertEquals(FloatMath.sum(1, 2, 3, 4), 10);
        assertFloatEquals(FloatMath.sum(0.5f, 1f, 1.5f, 2f, 2.5f), 7.5f);
    }

    // Find Extreme
    @Test
    void findMinAndMaxSuccess() {
        assertEquals(FloatMath.min(-6, 1, 2, 3, 5), -6);
        assertFloatEquals(FloatMath.min(-6.5f, 1, 2, 3, 5.5f), -6.5f);

        assertEquals(FloatMath.max(-6, 1, 2, 3, 5), 5);
        assertFloatEquals(FloatMath.max(-6.5f, 1, 2, 3, 5.5f), 5.5f);
    }

    // Rounding

    @Test
    void roundUpSuccess() {
        assertFloatEquals(0.07f, FloatMath.round(0.069f, 2));
        assertFloatEquals(0.007f, FloatMath.round(0.0069f, 3));
    }

    @Test
    void roundDownSuccess() {
        assertFloatEquals(0.04f, FloatMath.round(0.0420f, 2));
        assertFloatEquals(0.004f, FloatMath.round(0.00420f, 3));
        assertFloatEquals(0.06f, FloatMath.truncate(0.069f, 2));
    }

    // Trig

    @Test
    void angleConversionSuccess() {
        assertFloatEquals(180, FloatMath.toDegrees(FloatMath.PI));
        assertFloatEquals(FloatMath.PI, FloatMath.toRadians(180));
    }

    @Test
    void hypotenuseNDimensionsSuccess() {
        assertFloatEquals(10, FloatMath.hypot(6, 8));
        assertFloatEquals(13, FloatMath.hypot(3, 4, 12));
    }


}
