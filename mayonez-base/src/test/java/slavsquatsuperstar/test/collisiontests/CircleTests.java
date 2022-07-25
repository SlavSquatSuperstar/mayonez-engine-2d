package slavsquatsuperstar.test.collisiontests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.physics.colliders.CircleCollider;
import slavsquatsuperstar.mayonez.physics.collision.RaycastResult;
import slavsquatsuperstar.mayonez.physics.shapes.Ray;

import static org.junit.jupiter.api.Assertions.*;
import static slavsquatsuperstar.test.TestUtils.assertFloatEquals;

/**
 * Unit tests for {@link CircleCollider} class.
 *
 * @author SlavSquatSuperstar
 */
public class CircleTests {

    static CircleCollider c;

    // Create a circle centered at (0, 0) with a radius of 1 and scale it by 2x
    @BeforeAll
    public static void getCircle() {
        c = new CircleCollider(1).setTransform(new Transform().resize(new Vec2(2, 2)));
    }

    // Nearest Point

    @Test
    public void nearestPointInsideCircle() {
        assertEquals(c.center(), c.nearestPoint(c.center()));
        assertEquals(new Vec2(2, 0), c.nearestPoint(new Vec2(2, 0)));
    }

    @Test
    public void nearestPointOutsideCircle() {
        assertEquals(new Vec2(2, 0), c.nearestPoint(new Vec2(4, 0)));
        assertEquals(new Vec2(0, -2).rotate(-45), c.nearestPoint(new Vec2(-4, -4)));
    }

    // Raycast
    @Test
    public void outsideRayHitsCircle() {
        RaycastResult rc = c.raycast(new Ray(new Vec2(-5, 0), new Vec2(1, 0)), 0);
        assertNotNull(rc);
        assertFloatEquals(3, rc.getDistance());
        assertEquals(new Vec2(-2, 0), rc.getContact());
        assertNotNull(c.raycast(new Ray(new Vec2(5, 5), new Vec2(-1, -1.5f), 1), 0));
    }

    @Test
    public void outsideRayMissesCircle() {
        assertNull(c.raycast(new Ray(new Vec2(-5, 0), new Vec2(-1, 0)), 0));
        assertNull(c.raycast(new Ray(new Vec2(5, 5), new Vec2(1, 1.5f), 1), 0));
    }

    @Test
    public void limitedOutsideRayHitsCircle() {
        assertNotNull(c.raycast(new Ray(new Vec2(-5, 0), new Vec2(1, 0)), 5));
        assertNotNull(c.raycast(new Ray(new Vec2(3, 3), new Vec2(-1, -1.5f), 1), 5));
    }

    @Test
    public void limitedOutsideRayMissesCircle() {
        assertNull(c.raycast(new Ray(new Vec2(-15, 0), new Vec2(1, 0)), 5));
        assertNull(c.raycast(new Ray(new Vec2(10, 10), new Vec2(-1, -1.5f), 1), 5));
    }

    @Test
    public void insideRayHitsCircle() {
        assertNotNull(c.raycast(new Ray(new Vec2(-1, -1), new Vec2(1, 1.5f), 1), 0));
        assertNotNull(c.raycast(new Ray(new Vec2(1, 0), new Vec2(0, 1)), 0));
    }

}
