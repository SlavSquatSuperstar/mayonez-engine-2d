package slavsquatsuperstar.test.collisiontests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.colliders.Edge2D;
import slavsquatsuperstar.mayonez.physics.colliders.Ray;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Ray} class.
 *
 * @author SlavSquatSuperstar
 */
public class RayTests {

    @Test
    public void distanceToPointIndicatesDirection() {
        Ray ray = new Ray(new Edge2D(new Vec2(-4, 1), new Vec2(4, 1)));
        assertEquals(2, ray.distance(new Vec2(0, 3)));
        assertEquals(-2, ray.distance(new Vec2(0, -1)));
    }

    @Test
    public void nonParallelRaysHaveIntersection() {
        Ray r1 = new Ray(new Vec2(3, 4), new Vec2(0, 1));
        Ray r2 = new Ray(new Vec2(4, 3), new Vec2(1, 0));
        assertEquals(new Vec2(3, 3), r1.getIntersection(r2));
    }

    @Test
    public void parallelRaysNoIntersection() {
        Ray r1 = new Ray(new Vec2(-5, 1), new Vec2(1, -2));
        Ray r2 = new Ray(new Vec2(-5, -1), new Vec2(1, -2));
        assertNull(r1.getIntersection(r2));
    }

    @Test
    public void normalizedRaysHaveUnitLength() {
        Ray r = new Ray(new Vec2(), new Vec2(2, 0), true);
        assertEquals(r.getPoint(1), new Vec2(1, 0));
        assertEquals(r.getPoint(5), new Vec2(5, 0));
        assertEquals(r.getPoint(-2), new Vec2(-2, 0));
    }

    @Test
    public void nonNormalizedRaysHaveLength() {
        Ray r = new Ray(new Vec2(), new Vec2(2, 0), false);
        assertEquals(r.getPoint(1), new Vec2(2, 0));
        assertEquals(r.getPoint(5), new Vec2(10, 0));
        assertEquals(r.getPoint(-2), new Vec2(-4, 0));
    }

}
