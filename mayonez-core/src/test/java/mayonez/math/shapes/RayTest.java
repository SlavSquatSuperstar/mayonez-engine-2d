package mayonez.math.shapes;

import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.shapes.Ray} class.
 *
 * @author SlavSquatSuperstar
 */
class RayTest {

    @Test
    void distanceToPointIndicatesDirection() {
        var ray = new Ray(new Edge(new Vec2(-4, 1), new Vec2(4, 1)));
        assertEquals(2, ray.distance(new Vec2(0, 3)));
        assertEquals(-2, ray.distance(new Vec2(0, -1)));
    }

    @Test
    void nonParallelRaysHaveIntersection() {
        var r1 = new Ray(new Vec2(3, 4), new Vec2(0, 1));
        var r2 = new Ray(new Vec2(4, 3), new Vec2(1, 0));
        assertEquals(new Vec2(3, 3), r1.getIntersection(r2));
    }

    @Test
    void parallelRaysNoIntersection() {
        var r1 = new Ray(new Vec2(-5, 1), new Vec2(1, -2));
        var r2 = new Ray(new Vec2(-5, -1), new Vec2(1, -2));
        assertNull(r1.getIntersection(r2));
    }

    @Test
    void raysHaveUnitLength() {
        var r = new Ray(new Vec2(), new Vec2(2, 0));
        assertEquals(r.getPoint(1), new Vec2(1, 0));
        assertEquals(r.getPoint(5), new Vec2(5, 0));
        assertEquals(r.getPoint(-2), new Vec2(-2, 0));
    }

}
