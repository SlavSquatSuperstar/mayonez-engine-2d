package slavsquatsuperstar.mayonez.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static slavsquatsuperstar.test.TestUtils.assertFloatEquals;

/**
 * Unit tests for the {@link Vec2} class.
 *
 * @author SlavSquatSuperstar
 */
public class VectorTests {
    
    @Test
    public void equivalentVectorEquals() {
        Vec2 v1 = new Vec2(5, 5);
        Vec2 v2 = new Vec2(5, 5);
        assertEquals(v1, v2);
    }

    @Test
    public void sameVectorEquals() {
        Vec2 v = new Vec2(5, 5);
        assertSame(v, v);
        assertEquals(v, v);
    }

    @Test
    public void differentVectorNotEquals() {
        Vec2 v1 = new Vec2(3, 4);
        Vec2 v2 = new Vec2(5, 5);
        assertNotEquals(v1, v2);
    }

    @Test
    public void nullVectorNotEquals() {
        Vec2 v = new Vec2(5, 5);
        assertNotEquals(v, null);
        assertNotEquals(null, v);
    }

    @Test
    public void unitVectorLengthIsOne() {
        assertFloatEquals(1f, new Vec2(500, 500).unit().lenSq());
        assertFloatEquals(1f, new Vec2(-500, -500).unit().lenSq());
    }

    @Test
    public void unitVectorOfZeroIsZero() {
        Vec2 v = new Vec2();
        assertEquals(v.unit(), v);
    }

    @Test
    public void vectorDivideByZeroIsZero() {
        Vec2 v = new Vec2();
        assertEquals(v.div(0), v);
    }

    // Cross Product

    @Test
    public void parallelCrossProductIsZero() {
        Vec2 v1 = new Vec2(1, 0);
        Vec2 v2 = new Vec2(-1, 0);
        assertFloatEquals(v1.cross(v2), 0f);
    }

    @Test
    public void rightHandCrossProductIsPositive() {
        Vec2 v1 = new Vec2(1, 0);
        Vec2 v2 = new Vec2(0, 1);
        assertTrue(v1.cross(v2) > 0f);
    }

    @Test
    public void leftHandCrossProductIsNegative() {
        Vec2 v2 = new Vec2(0, 1);
        Vec2 v1 = new Vec2(1, 0);
        assertTrue(v2.cross(v1) < 0f);
    }

    @Test
    public void crossProductIsAntiCommutative() {
        Vec2 v1 = new Vec2(1, 0);
        Vec2 v2 = new Vec2(0, 1);
        assertFloatEquals(v1.cross(v2), -v2.cross(v1));
    }

    // Dot Product

    @Test
    public void perpendicularDotProductIsZero() {
        Vec2 v1 = new Vec2(1, 0);
        Vec2 v2 = new Vec2(0, 1);
        assertFloatEquals(90f, v1.angle(v2));
        assertFloatEquals(0f, v1.dot(v2));
    }

    @Test
    public void acuteDotProductIsPositive() {
        Vec2 v1 = new Vec2(3, 4);
        Vec2 v2 = new Vec2(4, 3);
        assertTrue(v1.angle(v2) < 90f);
        assertTrue(v1.dot(v2) > 0);
    }

    @Test
    public void obtuseDotProductIsPositive() {
        Vec2 v1 = new Vec2(5, 1);
        Vec2 v2 = new Vec2(-5, 1);
        assertTrue(v1.angle(v2) > 90f);
        assertTrue(v1.dot(v2) < 0);
    }

    // Projections

    @Test
    public void acuteProjectionIsPositive() {
        Vec2 v1 = new Vec2(6, 4);
        Vec2 v2 = new Vec2(4, 0);
        assertEquals(new Vec2(6, 0), v1.project(v2));
    }

    @Test
    public void obtuseProjectionIsNegative() {
        Vec2 v1 = new Vec2(-6, 4);
        Vec2 v2 = new Vec2(4, 0);
        assertEquals(new Vec2(-6, 0), v1.project(v2));
    }

    @Test
    public void perpendicularProjectionIsZero() {
        Vec2 v1 = new Vec2(0, 1);
        Vec2 v2 = new Vec2(1, 0);
        assertFloatEquals(0f, v1.project(v2).len());
    }

    @Test
    public void parallelProjectionIsSelf() {
        Vec2 v1 = new Vec2(2, 2);
        Vec2 v2 = new Vec2(4, 4);
        assertEquals(v1, v1.project(v2));
    }

    // Clamping
    @Test
    public void clampVectorSameSuccess() {
        Vec2 min = new Vec2(-2, -2);
        Vec2 max = new Vec2(4, 4);
        assertEquals(new Vec2(-2, 4), new Vec2(-2, 4).clampInbounds(min, max));
        assertEquals(new Vec2(1, 1), new Vec2(1, 1).clampInbounds(min, max));
    }

    @Test
    public void clampVectorDownSuccess() {
        Vec2 min = new Vec2(-2, -2);
        Vec2 max = new Vec2(4, 4);
        assertEquals(new Vec2(1, -2), new Vec2(1, -3).clampInbounds(min, max));
        assertEquals(new Vec2(4, 1), new Vec2(5, 1).clampInbounds(min, max));
        assertEquals(new Vec2(-2, 4), new Vec2(-3, 5).clampInbounds(min, max));
    }

    @Test
    public void clampVectorLengthSameSuccess() {
        assertEquals(new Vec2(4, 0), new Vec2(4, 0).clampLength(5));
        assertEquals(new Vec2(-2, -2), new Vec2(-2, -2).clampLength(5));
    }

    @Test
    public void clampVectorLengthDownSuccess() {
        assertEquals(new Vec2(5, 0), new Vec2(6, 0).clampLength(5));
        assertEquals(new Vec2(0, -5).rotate(-45), new Vec2(-5, -5).clampLength(5));
    }

    // Rotation / Angles

    @Test
    public void vectorRotateSuccess() {
        Vec2 v = new Vec2(6, 1);
        assertEquals(v, v.rotate(0, new Vec2()));
        assertEquals(new Vec2(-1, 6), v.rotate(90, new Vec2()));
        assertEquals(new Vec2(-6, -1), v.rotate(180, new Vec2()));
        assertEquals(new Vec2(1, -6), v.rotate(270, new Vec2()));
        assertEquals(v, v.rotate(360, new Vec2()));
    }

    @Test
    public void vectorAngleSuccess() {
        assertFloatEquals(new Vec2(0, 2).angle(), 90);
        assertFloatEquals(new Vec2(-2, 0).rotate(-30).angle(), 150);
    }

}
