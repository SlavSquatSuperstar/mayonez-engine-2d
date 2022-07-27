package slavsquatsuperstar.test.shapetests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.shapes.Ellipse;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Ellipse} class.
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

}
