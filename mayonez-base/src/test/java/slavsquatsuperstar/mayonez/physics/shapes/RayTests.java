package slavsquatsuperstar.mayonez.physics.shapes;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.mayonez.math.Vec2;
import slavsquatsuperstar.mayonez.physics.collision.Collisions;
import slavsquatsuperstar.mayonez.physics.collision.RaycastInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static slavsquatsuperstar.test.TestUtils.assertFloatEquals;
import static slavsquatsuperstar.mayonez.physics.collision.Collisions.raycast;

/**
 * Unit tests for the {@link slavsquatsuperstar.mayonez.physics.shapes.Ray} class.
 *
 * @author SlavSquatSuperstar
 */
public class RayTests {

    @Test
    public void distanceToPointIndicatesDirection() {
        Ray ray = new Ray(new Edge(new Vec2(-4, 1), new Vec2(4, 1)));
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
    public void raysHaveUnitLength() {
        Ray r = new Ray(new Vec2(), new Vec2(2, 0));
        assertEquals(r.getPoint(1), new Vec2(1, 0));
        assertEquals(r.getPoint(5), new Vec2(5, 0));
        assertEquals(r.getPoint(-2), new Vec2(-2, 0));
    }

    @Test
    public void rayVsCircleHit() {
        Circle c = new Circle(new Vec2(5, 0), 2);
        RaycastInfo rc = raycast(c, new Ray(new Vec2(0, 0), new Vec2(1, 0)), 3);
        assertFloatEquals(rc.distance, 3); // ray from outside
        rc = raycast(c, new Ray(new Vec2(4, 0), new Vec2(1, 0)), 0);
        assertFloatEquals(rc.distance, 3); // ray from inside
    }

    @Test
    public void rayVsCircleMiss() {
        Circle c = new Circle(new Vec2(0, 0), 2);
        assertNull(raycast(c, new Ray(new Vec2(-3, 0), new Vec2(0, 1)), 0));
        assertNull(raycast(c, new Ray(new Vec2(-4, 0), new Vec2(1, 0)), 1)); // too far away
    }

    @Test
    public void rayVsEdgeHit() {
        Edge e = new Edge(new Vec2(0, -2), new Vec2(0, 2));
        RaycastInfo rc = raycast(e, new Ray(new Vec2(-2, 0), new Vec2(1, 0)), 0);
        assertEquals(new Vec2(0, 0), rc.contact);
        assertFloatEquals(2, rc.distance);
    }

    @Test
    public void rayVsPolygonHit() {
        Rectangle r = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        RaycastInfo rc = raycast(r, new Ray(new Vec2(-4, 0), new Vec2(1, 0)), 0);
        assertEquals(2, rc.distance);
    }

    @Test
    public void rayVsRect() {
        Rectangle r = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        RaycastInfo rc = raycast(r, new Ray(new Vec2(-4, 0), new Vec2(1, 0)), 0);
        assertEquals(2, rc.distance);

        rc = Collisions.raycast(r, new Ray(new Vec2(-2, 4), new Vec2(1, -1)), 0);
        assertEquals(rc.contact, new Vec2(0, 2));
        rc = Collisions.raycast(r, new Ray(new Vec2(2, 4), new Vec2(1, -1)), 0);
        assertNull(rc);
    }

}
