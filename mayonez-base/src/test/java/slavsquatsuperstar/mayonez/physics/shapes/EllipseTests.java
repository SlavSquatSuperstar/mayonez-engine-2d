package slavsquatsuperstar.mayonez.physics.shapes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.mayonez.math.Vec2;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link slavsquatsuperstar.mayonez.physics.shapes.Ellipse} class.
 *
 * @author SlavSquatSuperstar
 */
public class EllipseTests {

    private static Ellipse ellipse;

    @BeforeAll
    public static void getEllipse() {
        ellipse = new Ellipse(new Vec2(2, 3), new Vec2(4, 6)); // 4x6 at (2, 3)
    }

    @Test
    public void interiorPointInEllipse() {
        assertTrue(ellipse.contains(new Vec2(1, 3)));
        assertTrue(ellipse.contains(new Vec2(3, 4)));
    }

    @Test
    public void boundaryPointInEllipse() {
        assertTrue(ellipse.contains(new Vec2(0, 3)));
        assertTrue(ellipse.contains(new Vec2(2, 6)));
    }

    @Test
    public void exteriorPointNotInEllipse() {
        assertFalse(ellipse.contains(new Vec2(0, 0)));
        assertFalse(ellipse.contains(new Vec2(4, 1)));
    }

    @Test
    public void ellipseSupportPoints() {
        assertEquals(new Vec2(4, 3), ellipse.supportPoint(new Vec2(1, 0)));
        assertEquals(new Vec2(2, 6), ellipse.supportPoint(new Vec2(0, 1)));
        assertEquals(new Vec2(3.6f, 4.8f), ellipse.supportPoint(new Vec2(2, 1)));
    }

    @Test
    public void rotatedEllipseSupportPoints() {
        Ellipse e = ellipse.rotate(45, null);
        Vec2 center = e.center();
        assertEquals(new Vec2(4, 3).rotate(45, center), e.supportPoint(new Vec2(1, 1)));
        assertEquals(new Vec2(2, 6).rotate(45, center), e.supportPoint(new Vec2(-1, 1)));
        assertEquals(new Vec2(3.6f, 4.8f).rotate(45, center),
                e.supportPoint(new Vec2(2, 1).rotate(45)));
    }

}
