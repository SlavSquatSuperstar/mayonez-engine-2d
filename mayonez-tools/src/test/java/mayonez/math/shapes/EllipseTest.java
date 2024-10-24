package mayonez.math.shapes;

import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.shapes.Ellipse} class.
 *
 * @author SlavSquatSuperstar
 */
class EllipseTest {

    private static Ellipse ellipse;

    @BeforeAll
    static void getEllipse() {
        ellipse = new Ellipse(new Vec2(2, 3), new Vec2(4, 6)); // 4x6 at (2, 3)
    }

    @Test
    void interiorPointInEllipse() {
        assertTrue(ellipse.contains(new Vec2(1, 3)));
        assertTrue(ellipse.contains(new Vec2(3, 4)));
    }

    @Test
    void boundaryPointInEllipse() {
        assertTrue(ellipse.contains(new Vec2(0, 3)));
        assertTrue(ellipse.contains(new Vec2(2, 6)));
    }

    @Test
    void exteriorPointNotInEllipse() {
        assertFalse(ellipse.contains(new Vec2(0, 0)));
        assertFalse(ellipse.contains(new Vec2(4, 1)));
    }

    @Test
    void ellipseSupportPoints() {
        assertEquals(new Vec2(4, 3), ellipse.supportPoint(new Vec2(1, 0)));
        assertEquals(new Vec2(2, 6), ellipse.supportPoint(new Vec2(0, 1)));
        assertEquals(new Vec2(3.6f, 4.8f), ellipse.supportPoint(new Vec2(2, 1)));
    }

    @Test
    void rotatedEllipseSupportPoints() {
        var e = ellipse.rotate(45, null);
        var center = e.center();
        assertEquals(new Vec2(4, 3).rotate(45, center), e.supportPoint(new Vec2(1, 1)));
        assertEquals(new Vec2(2, 6).rotate(45, center), e.supportPoint(new Vec2(-1, 1)));
        assertEquals(new Vec2(3.6f, 4.8f).rotate(45, center),
                e.supportPoint(new Vec2(2, 1).rotate(45)));
    }

}
