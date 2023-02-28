package mayonez.math;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.Mat22} class.
 *
 * @author SlavSquatSuperstar
 */
public class Mat22Tests {

    // Vector Transformation

    @Test
    public void vectorTimesIdentityIsSame() {
        var v = new Vec2(3, 4);
        assertEquals(v, new Mat22().times(v));
    }

    @Test
    public void matrixScalesVector() {
        var v = new Vec2(3, 4);
        assertEquals(v.mul(2), new Mat22(2, 0, 0, 2).times(v));
    }

    @Test
    public void matrixRotatesVector() {
        var v = new Vec2(3, 4);
        assertEquals(v.rotate(90), new Mat22(new Vec2(0, 1), new Vec2(-1, 0)).times(v));
    }

    @Test
    public void compositionSameAsTwoTransforms() {
        var v = new Vec2(3, 4);
        var m1 = new Mat22(2, 2, 2, 2);
        var m2 = new Mat22(0, 1, -1, 0);
        assertEquals(m2.times(m1).times(v), m2.times(m1.times(v)));
    }

    // Matrix Multiplication

    @Test
    public void scalarScalesMatrix() {
        var m = new Mat22(3, 4, 2, 6);
        assertEquals(new Mat22(6, 8, 4, 12), m.times(2));
    }

    @Test
    public void matrixTimesIdentityIsSame() {
        var m = new Mat22(3, 4, 2, 6);
        assertEquals(m.times(1), m.times(new Mat22()));
    }

    @Test
    public void matrixMultiplicationNotCommutative() {
        var m1 = new Mat22(3, 4, 2, 6);
        var m2 = new Mat22(-7, -5, 1, 10);
        assertNotEquals(m1.times(m2), m2.times(m1));
    }

}
