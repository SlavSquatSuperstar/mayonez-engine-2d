package mayonez.math;

import org.junit.jupiter.api.*;

import static mayonez.test.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.Vec2} class.
 *
 * @author SlavSquatSuperstar
 */
public class Vec2Tests {

    @Test
    public void equivalentVectorEquals() {
        var v1 = new Vec2(5, 5);
        var v2 = new Vec2(5, 5);
        assertEquals(v1, v2);
    }

    @Test
    public void sameVectorEquals() {
        var v = new Vec2(5, 5);
        assertSame(v, v);
        assertEquals(v, v);
    }

    @Test
    public void differentVectorNotEquals() {
        var v1 = new Vec2(3, 4);
        var v2 = new Vec2(5, 5);
        assertNotEquals(v1, v2);
    }

    @Test
    public void nullVectorNotEquals() {
        var v = new Vec2(5, 5);
        assertNotEquals(v, null);
        assertNotEquals(null, v);
    }

    @Test
    public void unitVectorLengthIsOne() {
        assertFloatEquals(1f, new Vec2(0.05f, 0.01f).unit().lenSq());
        assertFloatEquals(1f, new Vec2(-100, -500).unit().lenSq());
    }

    @Test
    public void unitVectorOfZeroIsZero() {
        var v = new Vec2();
        assertEquals(v.unit(), v);
    }

    @Test
    public void vectorDivideByZeroIsZero() {
        var v = new Vec2();
        assertEquals(v.div(0), v);
    }

    // Cross Product

    @Test
    public void parallelCrossProductIsZero() {
        var v1 = new Vec2(1, 0);
        var v2 = new Vec2(-1, 0);
        assertFloatEquals(v1.cross(v2), 0f);
    }

    @Test
    public void rightHandCrossProductIsPositive() {
        var v1 = new Vec2(1, 0);
        var v2 = new Vec2(0, 1);
        assertTrue(v1.cross(v2) > 0f);
    }

    @Test
    public void leftHandCrossProductIsNegative() {
        var v2 = new Vec2(0, 1);
        var v1 = new Vec2(1, 0);
        assertTrue(v2.cross(v1) < 0f);
    }

    @Test
    public void crossProductIsAntiCommutative() {
        var v1 = new Vec2(1, 0);
        var v2 = new Vec2(0, 1);
        assertFloatEquals(v1.cross(v2), -v2.cross(v1));
    }

    // Dot Product

    @Test
    public void perpendicularDotProductIsZero() {
        var v1 = new Vec2(1, 0);
        var v2 = new Vec2(0, 1);
        assertFloatEquals(90f, v1.angle(v2));
        assertFloatEquals(0f, v1.dot(v2));
    }

    @Test
    public void acuteDotProductIsPositive() {
        var v1 = new Vec2(3, 4);
        var v2 = new Vec2(4, 3);
        assertTrue(v1.angle(v2) < 90f);
        assertTrue(v1.dot(v2) > 0);
    }

    @Test
    public void obtuseDotProductIsPositive() {
        var v1 = new Vec2(5, 1);
        var v2 = new Vec2(-5, 1);
        assertTrue(v1.angle(v2) > 90f);
        assertTrue(v1.dot(v2) < 0);
    }

    // Projections

    @Test
    public void acuteProjectionIsPositive() {
        var v1 = new Vec2(6, 4);
        var v2 = new Vec2(4, 0);
        assertEquals(new Vec2(6, 0), v1.project(v2));
    }

    @Test
    public void obtuseProjectionIsNegative() {
        var v1 = new Vec2(-6, 4);
        var v2 = new Vec2(4, 0);
        assertEquals(new Vec2(-6, 0), v1.project(v2));
    }

    @Test
    public void perpendicularProjectionIsZero() {
        var v1 = new Vec2(0, 1);
        var v2 = new Vec2(1, 0);
        assertFloatEquals(0f, v1.project(v2).len());
    }

    @Test
    public void parallelProjectionIsSelf() {
        var v1 = new Vec2(2, 2);
        var v2 = new Vec2(4, 4);
        assertEquals(v1, v1.project(v2));
    }

    // Clamping
    @Test
    public void clampVectorSameSuccess() {
        var min = new Vec2(-2, -2);
        var max = new Vec2(4, 4);
        assertEquals(new Vec2(-2, 4), new Vec2(-2, 4).clampInbounds(min, max));
        assertEquals(new Vec2(1, 1), new Vec2(1, 1).clampInbounds(min, max));
    }

    @Test
    public void clampVectorDownSuccess() {
        var min = new Vec2(-2, -2);
        var max = new Vec2(4, 4);
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
        var v = new Vec2(6, 1);
        assertEquals(v, v.rotate(0, new Vec2()));
        assertEquals(new Vec2(-1, 6), v.rotate(90, new Vec2()));
        assertEquals(new Vec2(-6, -1), v.rotate(180, new Vec2()));
        assertEquals(new Vec2(1, -6), v.rotate(270, new Vec2()));
        assertEquals(v, v.rotate(360, new Vec2()));
    }

    @Test
    public void vectorSignedAngleAxes() {
        assertFloatEquals(0, new Vec2(1, 0).angle());
        assertFloatEquals(90, new Vec2(0, 1).angle());
        assertFloatEquals(180, new Vec2(-1, 0).angle());
        assertFloatEquals(-90, new Vec2(0, -1).angle());
    }

    @Test
    public void vectorSignedAngleQuadrants() {
        var root3 = FloatMath.sqrt(3f);
        assertFloatEquals(30, new Vec2(root3, 1).angle());
        assertFloatEquals(150, new Vec2(-root3, 1).angle());
        assertFloatEquals(-150, new Vec2(-root3, -1).angle());
        assertFloatEquals(-30, new Vec2(root3, -1).angle());
    }

    @Test
    public void vectorUnsignedAngleAxes() {
        assertFloatEquals(0, new Vec2(1, 0).posAngle());
        assertFloatEquals(90, new Vec2(0, 1).posAngle());
        assertFloatEquals(180, new Vec2(-1, 0).posAngle());
        assertFloatEquals(270, new Vec2(0, -1).posAngle());
    }

    @Test
    public void vectorUnsignedAngleQuadrants() {
        var root3 = FloatMath.sqrt(3f);
        assertFloatEquals(30, new Vec2(root3, 1).posAngle());
        assertFloatEquals(150, new Vec2(-root3, 1).posAngle());
        assertFloatEquals(210, new Vec2(-root3, -1).posAngle());
        assertFloatEquals(330, new Vec2(root3, -1).posAngle());
    }

    @Test
    public void vectorSmallAngleWithOther() {
        var tolerance = 1e-5f;
        var v = new Vec2(2, 2); // 45º
        var root3 = FloatMath.sqrt(3f); // √3
        assertEquals(45, new Vec2(1, 0).angle(v), tolerance); // 0º
        assertEquals(15, new Vec2(root3, 1).angle(v), tolerance); // 30º
        assertEquals(0, new Vec2(1, 1).angle(v), tolerance); // 45º
        assertEquals(15, new Vec2(1, root3).angle(v), tolerance); // 60º
        assertEquals(180, new Vec2(-1, -1).angle(v), tolerance); // 225º
        assertEquals(90, new Vec2(1, -1).angle(v), tolerance); // 315º
    }

    @Test
    public void vectorPositiveAngleWithOther() {
        var tolerance = 1e-5f;
        var v = new Vec2(2, 2); // 45º
        var root3 = FloatMath.sqrt(3f); // √3
        assertEquals(0, new Vec2(1, 1).posAngle(v), tolerance); // 45º
        assertEquals(15, new Vec2(1, root3).posAngle(v), tolerance); // 60º
        assertEquals(75, new Vec2(-1, root3).posAngle(v), tolerance); // 120
        assertEquals(180, new Vec2(-1, -1).posAngle(v), tolerance); // 225º
        assertEquals(270, new Vec2(1, -1).posAngle(v), tolerance); // 315º
        assertEquals(315, new Vec2(1, 0).posAngle(v), tolerance); // 0º
        assertEquals(345, new Vec2(root3, 1).posAngle(v), tolerance); // 30º
    }

}
