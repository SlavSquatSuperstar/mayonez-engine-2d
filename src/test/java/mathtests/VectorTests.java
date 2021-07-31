package mathtests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.math.MathUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Vec2} class.
 *
 * @author SlavSquatSuperstar
 */
public class VectorTests {

    @Test
    public void vectorEquals() {
        Vec2 v1 = new Vec2(5, 5);
        Vec2 v2 = new Vec2(5, 5);
        assertSame(v1, v1);
        assertSame(v2, v2);
        assertEquals(v1, v2);
    }

    @Test
    public void unitVectorLengthIsOne() {
        assertEquals(1f, new Vec2(500, 500).unitVector().lenSquared(), MathUtils.EPSILON);
        assertEquals(1f, new Vec2(-500, -500).unitVector().lenSquared(), MathUtils.EPSILON);
    }

    @Test
    public void unitVectorOfZeroIsZero() {
        Vec2 v = new Vec2();
        assertEquals(v.unitVector(), v);
    }

    @Test
    public void vectorDivideByZeroIsZero() {
        Vec2 v = new Vec2();
        assertEquals(v.div(0), v);
    }

    // Cross Product

    @Test
    public void parallelDotProductIsZero() {
        Vec2 v1 = new Vec2(1, 0);
        Vec2 v2 = new Vec2(-1, 0);
        assertEquals(v1.cross(v2), 0f, MathUtils.EPSILON);
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
        assertEquals(v1.cross(v2), -v2.cross(v1), MathUtils.EPSILON);
    }

    // Dot Product

    @Test
    public void perpendicularDotProductIsZero() {
        Vec2 v1 = new Vec2(1, 0);
        Vec2 v2 = new Vec2(0, 1);
        assertEquals(90f, v1.angle(v2), MathUtils.EPSILON);
        assertEquals(0f, v1.dot(v2), MathUtils.EPSILON);
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
        assertEquals(0f, v1.project(v2).len(), MathUtils.EPSILON);
    }

    @Test
    public void parallelProjectionIsSelf() {
        Vec2 v1 = new Vec2(2, 2);
        Vec2 v2 = new Vec2(4, 4);
        assertEquals(v1, v1.project(v2));
    }

    // Rotation

    @Test
    public void vectorRotateSuccess() {
        Vec2 v = new Vec2(6, 1);
        assertEquals(v, v.rotate(0, new Vec2()));
        assertEquals(new Vec2(-1, 6), v.rotate(90, new Vec2()));
        assertEquals(new Vec2(-6, -1), v.rotate(180, new Vec2()));
        assertEquals(new Vec2(1, -6), v.rotate(270, new Vec2()));
        assertEquals(v, v.rotate(360, new Vec2()));
    }

}