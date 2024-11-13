package mayonez.math;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.Mat22} class.
 *
 * @author SlavSquatSuperstar
 */
class Mat22Test {

    // Determinant

    @Test
    void determinantIsPositive() {
        var m = new Mat22(2, 1, 4, 3);
        assertEquals(2f, m.determinant());
    }

    @Test
    void determinantIsNegative() {
        var m = new Mat22(1, 2, 3, 4);
        assertEquals(-2f, m.determinant());
    }

    @Test
    void determinantIsZero() {
        var m = new Mat22(1, 2, 2, 4);
        assertEquals(0f, m.determinant());
    }

    // Matrix Arithmetic

    @Test
    void matrixAdditionCorrect() {
        var m1 = new Mat22(1, 2, 3, 4);
        var m2 = new Mat22(5, 6, 7, 8);
        assertEquals(new Mat22(6, 8, 10, 12), m1.plus(m2));
    }

    @Test
    void matrixSubtractionCorrect() {
        var m1 = new Mat22(5, 6, 7, 8);
        var m2 = new Mat22(1, 2, 3, 4);
        assertEquals(new Mat22(4, 4, 4, 4), m1.minus(m2));
    }

    @Test
    void scalarMultiplicationCorrect() {
        var m = new Mat22(1, 2, 3, 4);
        assertEquals(new Mat22(-2, -4, -6, -8), m.times(-2f));
    }

    @Test
    void matrixMultiplicationCorrect() {
        var m1 = new Mat22(1, 2, 3, 4);
        var m2 = new Mat22(1, 3, 2, 4);
        assertEquals(new Mat22(5, 11, 11, 25), m1.times(m2));
    }

    // Vector Transformation

    @Test
    void vectorTimesIdentityIsSame() {
        var v = new Vec2(3, 4);
        assertEquals(v, new Mat22().times(v));
    }

    @Test
    void matrixScalesVector() {
        var v = new Vec2(3, 4);
        assertEquals(v.mul(2), new Mat22(2, 0, 0, 2).times(v));
    }

    @Test
    void matrixRotatesVector() {
        var v = new Vec2(3, 4);
        assertEquals(v.rotate(90), new Mat22(new Vec2(0, 1), new Vec2(-1, 0)).times(v));
    }

    @Test
    void compositionSameAsTwoTransforms() {
        var v = new Vec2(3, 4);
        var m1 = new Mat22(2, 2, 2, 2);
        var m2 = new Mat22(0, 1, -1, 0);
        assertEquals(m2.times(m1).times(v), m2.times(m1.times(v)));
    }

    // Matrix Multiplication

    @Test
    void scalarScalesMatrix() {
        var m = new Mat22(3, 4, 2, 6);
        assertEquals(new Mat22(6, 8, 4, 12), m.times(2));
    }

    @Test
    void matrixTimesIdentityIsSame() {
        var m = new Mat22(3, 4, 2, 6);
        assertEquals(m.times(1), m.times(new Mat22()));
    }

    @Test
    void matrixMultiplicationNotCommutative() {
        var m1 = new Mat22(3, 4, 2, 6);
        var m2 = new Mat22(-7, -5, 1, 10);
        assertNotEquals(m1.times(m2), m2.times(m1));
    }

}
