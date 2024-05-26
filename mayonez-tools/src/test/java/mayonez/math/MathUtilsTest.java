package mayonez.math;

import org.junit.jupiter.api.*;

import static mayonez.math.MathTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.MathUtils} class.
 *
 * @author SlavSquatSuperstar
 */
class MathUtilsTest {

    private static final float PI = MathUtils.PI;

    // Equals

    @Test
    void floatsAreEqual() {
        assertTrue(MathUtils.equals(0.0000010f, 0.0000019f));
        assertTrue(MathUtils.equals(-0.0000005f, 0.0000005f));
        assertTrue(MathUtils.equals(0.0000010f, 0.0000020f));
        assertTrue(MathUtils.equals(0.0001f, 0.0002f, 0.0001f));
    }

    @Test
    void floatsAreNotEqual() {
        assertFalse(MathUtils.equals(0.0000010f, 0.0000021f));
        assertFalse(MathUtils.equals(-0.0000005f, 0.0000006f));
        assertFalse(MathUtils.equals(0.0001f, 0.0002f, 0.00005f));
    }

    @Test
    void floatEpsilonEqualsZero() {
        assertFloatEquals(0f, Float.MIN_VALUE);
    }

    // Square/Square Root

    @Test
    void squaredIsCorrect() {
        assertFloatEquals(4, MathUtils.squared(2));
        assertFloatEquals(PI * PI, MathUtils.squared(PI));
    }

    @Test
    void sqrtIsCorrect() {
        assertFloatEquals(2, MathUtils.sqrt(4));
        assertFloatEquals(PI, MathUtils.sqrt(PI * PI));
        assertFloatEquals(PI, MathUtils.sqrt(PI) * MathUtils.sqrt(PI));
    }

    // Hypotenuse

    @Test
    void hypotIsCorrect() {
        assertFloatEquals(5, MathUtils.hypot(3, 4));
        assertFloatEquals(MathUtils.sqrt(2), MathUtils.hypot(1, 1));
    }

    @Test
    void hypotVarargsIsCorrect() {
        assertFloatEquals(10, MathUtils.hypot(6, 8));
        assertFloatEquals(13, MathUtils.hypot(3, 4, 12));
    }

    @Test
    void invHypotIsCorrect() {
        assertFloatEquals(4, MathUtils.invHypot(5, 3));
        assertFloatEquals(1, MathUtils.invHypot(MathUtils.sqrt(2), 1));
    }

    // Average/Sum

    @Test
    void averageIsCorrect() {
        assertEquals(3, MathUtils.avg(1, 2, 3, 4, 5));
        assertFloatEquals(2.5f, MathUtils.avg(1f, 2f, 3f, 4f));
    }

    @Test
    void sumIsCorrect() {
        assertEquals(10, MathUtils.sum(1, 2, 3, 4), 10);
        assertFloatEquals(7.5f, MathUtils.sum(0.5f, 1f, 1.5f, 2f, 2.5f));
    }

    // Find Min/Max

    @Test
    void minIsCorrect() {
        assertEquals(-6, MathUtils.min(-6, 1, 2, 3, 5));
        assertFloatEquals(-6.5f, MathUtils.min(-6.5f, 1, 2, 3, 5.5f));
    }

    @Test
    void maxIsCorrect() {
        assertEquals(5, MathUtils.max(-6, 1, 2, 3, 5));
        assertFloatEquals(5.5f, MathUtils.max(-6.5f, 1, 2, 3, 5.5f));
    }

    @Test
    void minIndexIsCorrect() {
        assertEquals(4, MathUtils.minIndex(1, 2, 3, 5, -6));
        assertEquals(4, MathUtils.minIndex(1, 2, 3, 5.5f, -6.5f));
    }

    @Test
    void maxIndexIsCorrect() {
        assertEquals(3, MathUtils.maxIndex(1, 2, 3, 5, -6));
        assertEquals(3, MathUtils.maxIndex(1, 2, 3, 5.5f, -6.5f));
    }

    // Round/Truncate

    @Test
    void roundUpIsCorrect() {
        assertFloatEquals(0.07f, MathUtils.round(0.069f, 2));
        assertFloatEquals(0.007f, MathUtils.round(0.0069f, 3));
    }

    @Test
    void roundDownIsCorrect() {
        assertFloatEquals(0.04f, MathUtils.round(0.0420f, 2));
        assertFloatEquals(0.004f, MathUtils.round(0.00420f, 3));
    }

    @Test
    void truncateIsCorrect() {
        assertFloatEquals(0.07f, MathUtils.truncate(0.071f, 2));
        assertFloatEquals(0.06f, MathUtils.truncate(0.069f, 2));
    }

    // Angles

    @Test
    void angleConversionIsCorrect() {
        assertFloatEquals(180f, MathUtils.toDegrees(MathUtils.PI));
        assertFloatEquals(30f, MathUtils.toDegrees(MathUtils.PI / 6f));

        assertFloatEquals(MathUtils.PI, MathUtils.toRadians(180f));
        assertFloatEquals(MathUtils.PI / 6f, MathUtils.toRadians(30f));
    }

}
