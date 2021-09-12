package slavsquatsuperstar.test.geometrytests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.colliders.Edge2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.Ray2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Ray2D} class.
 *
 * @author SlavSquatSuperstar
 */
public class RayTests {

    @Test
    public void distanceToPointIndicatesDirection() {
        Ray2D ray = new Ray2D(new Edge2D(new Vec2(-4, 1), new Vec2(4, 1)));
        assertEquals(2, ray.distance(new Vec2(0, 3)));
        assertEquals(-2, ray.distance(new Vec2(0, -1)));
    }

    @Test
    public void nonParallelRaysHaveIntersection() {
        Ray2D r1 = new Ray2D(new Vec2(3, 4), new Vec2(0, 1));
        Ray2D r2 = new Ray2D(new Vec2(4, 3), new Vec2(1, 0));
        assertEquals(new Vec2(3, 3), r1.getIntersection(r2));
    }

    @Test
    public void parallelRaysNoIntersection() {
        Ray2D r1 = new Ray2D(new Vec2(-5, 1), new Vec2(1, -2));
        Ray2D r2 = new Ray2D(new Vec2(-5, -1), new Vec2(1, -2));
        assertNull(r1.getIntersection(r2));
    }

}
