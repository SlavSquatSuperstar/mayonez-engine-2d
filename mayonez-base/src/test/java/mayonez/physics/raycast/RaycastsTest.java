package mayonez.physics.raycast;

import mayonez.math.*;
import mayonez.math.shapes.*;
import org.junit.jupiter.api.*;

import static mayonez.physics.raycast.Raycasts.*;
import static mayonez.test.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.physics.raycast.Raycasts} class.
 *
 * @author SlavSquatSuperstar
 */
class RaycastsTest {

    @Test
    void rayVsCircleHit() {
        var c = new Circle(new Vec2(5, 0), 2);
        var rc = raycast(c, new Ray(new Vec2(0, 0), new Vec2(1, 0)), 3);
        assertFloatEquals(rc.getDistance(), 3); // ray from outside
        rc = raycast(c, new Ray(new Vec2(4, 0), new Vec2(1, 0)), 0);
        assertFloatEquals(rc.getDistance(), 3); // ray from inside
    }

    @Test
    void rayVsCircleMiss() {
        var c = new Circle(new Vec2(0, 0), 2);
        assertNull(raycast(c, new Ray(new Vec2(-3, 0), new Vec2(0, 1)), 0));
        assertNull(raycast(c, new Ray(new Vec2(-4, 0), new Vec2(1, 0)), 1)); // too far away
    }

    @Test
    void rayVsEdgeHit() {
        var e = new Edge(new Vec2(0, -2), new Vec2(0, 2));
        var rc = raycast(e, new Ray(new Vec2(-2, 0), new Vec2(1, 0)), 0);
        assertEquals(new Vec2(0, 0), rc.getContact());
        assertFloatEquals(2, rc.getDistance());
    }

    @Test
    void rayVsPolygonHit() {
        var r = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        var rc = raycast(r, new Ray(new Vec2(-4, 0), new Vec2(1, 0)), 0);
        assertEquals(2, rc.getDistance());
    }

    @Test
    void rayVsRect() {
        var r = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        var rc = raycast(r, new Ray(new Vec2(-4, 0), new Vec2(1, 0)), 0);
        assertEquals(2, rc.getDistance());

        rc = raycast(r, new Ray(new Vec2(-2, 4), new Vec2(1, -1)), 0);
        assertEquals(rc.getContact(), new Vec2(0, 2));
        rc = raycast(r, new Ray(new Vec2(2, 4), new Vec2(1, -1)), 0);
        assertNull(rc);
    }

}
