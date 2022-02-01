package slavsquatsuperstar.test.collisiontests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.physics2d.collision.RaycastResult;
import slavsquatsuperstar.mayonez.physics2d.colliders.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CircleTests} class.
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

    // Contains Point

    @Test
    public void interiorPointIsInCircle() {
        assertTrue(c.contains(c.center()));
        assertTrue(c.contains(new Vec2(1, 1)));
        assertTrue(c.contains(new Vec2(-1.9f, 0)));
    }

    @Test
    public void edgePointIsInCircle() {
        assertTrue(c.contains(new Vec2(0, 2)));
        assertTrue(c.contains(new Vec2(2, 0).rotate(45)));
    }

    @Test
    public void exteriorPointNotInCircle() {
        assertFalse(c.contains(new Vec2(0, 3)));
        assertFalse(c.contains(new Vec2(-2, -2)));
        assertFalse(c.contains(new Vec2(2.1f, 0).rotate(45)));
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
        RaycastResult rc = c.raycast(new Ray2D(new Vec2(-5, 0), new Vec2(1, 0)), 0);
        assertNotNull(rc);
        assertEquals(3, rc.getDistance(), MathUtils.EPSILON);
        assertEquals(new Vec2(-2, 0), rc.getContact());
        assertNotNull(c.raycast(new Ray2D(new Vec2(5, 5), new Vec2(-1, -1.5f)), 0));
    }

    @Test
    public void outsideRayMissesCircle() {
        assertNull(c.raycast(new Ray2D(new Vec2(-5, 0), new Vec2(-1, 0)), 0));
        assertNull(c.raycast(new Ray2D(new Vec2(5, 5), new Vec2(1, 1.5f)), 0));
    }

    @Test
    public void limitedOutsideRayHitsCircle() {
        assertNotNull(c.raycast(new Ray2D(new Vec2(-5, 0), new Vec2(1, 0)), 5));
        assertNotNull(c.raycast(new Ray2D(new Vec2(3, 3), new Vec2(-1, -1.5f)), 5));
    }

    @Test
    public void limitedOutsideRayMissesCircle() {
        assertNull(c.raycast(new Ray2D(new Vec2(-15, 0), new Vec2(1, 0)), 5));
        assertNull(c.raycast(new Ray2D(new Vec2(10, 10), new Vec2(-1, -1.5f)), 5));
    }

    @Test
    public void insideRayHitsCircle() {
        assertNotNull(c.raycast(new Ray2D(new Vec2(-1, -1), new Vec2(1, 1.5f)), 0));
        assertNotNull(c.raycast(new Ray2D(new Vec2(1, 0), new Vec2(0, 1)), 0));
    }

    // Line Intersection

    @Test
    public void tangentLineIsInCircle() {
        assertTrue(c.intersects(new Edge2D(new Vec2(-2, -2), new Vec2(0, -2))));
        assertTrue(c.intersects(new Edge2D(new Vec2(-2, -2), new Vec2(-2, 0))));
        assertTrue(c.intersects(new Edge2D(new Vec2(0, -2), new Vec2(0, 0))));
        assertTrue(c.intersects(new Edge2D(new Vec2(-2, 0), new Vec2(0, 0))));
    }

    @Test
    public void secantLineIsInCircle() {
        assertTrue(c.intersects(new Edge2D(new Vec2(0, -2), new Vec2(0, 2))));
        assertTrue(c.intersects(new Edge2D(new Vec2(-2, 0), new Vec2(2, 0))));
        assertTrue(c.intersects(new Edge2D(new Vec2(-2, -2), new Vec2(2, 2))));
        assertTrue(c.intersects(new Edge2D(new Vec2(-2, -2), new Vec2(2, 1))));
    }

    @Test
    public void perpendicularLineIsInCircle() {
        assertTrue(c.intersects(new Edge2D(new Vec2(1, 1), new Vec2(3, 3))));
        assertTrue(c.intersects(new Edge2D(new Vec2(0, 1), new Vec2(0, 4))));
    }

    @Test
    public void lineNotInCircle() {
        assertFalse(c.intersects(new Edge2D(new Vec2(-2, 3), new Vec2(2, 3))));
        assertFalse(c.intersects(new Edge2D(new Vec2(0, 4), new Vec2(4, 0))));
        assertFalse(c.intersects(new Edge2D(new Vec2(0, 4), new Vec2(0, 3))));
        assertFalse(c.intersects(new Edge2D(new Vec2(4, 0), new Vec2(3, 0))));
        assertFalse(c.intersects(new Edge2D(new Vec2(0, 3), new Vec2(0, 4))));
        assertFalse(c.intersects(new Edge2D(new Vec2(3, 0), new Vec2(4, 0))));
    }

    // Circle vs Primitive

    @Test
    public void circleIntersectsCircle() {
        CircleCollider other = new CircleCollider(4);
        other.setTransform(new Transform(new Vec2(2, 2)));
        assertTrue(c.detectCollision(other));
    }

    @Test
    public void circleIntersectsAABB() {
        BoundingBoxCollider2D aabb = new BoundingBoxCollider2D(new Vec2(4, 4));
        aabb.setTransform(new Transform(new Vec2(1, 0)));
        assertTrue(c.detectCollision(aabb));
    }

    @Test
    public void circleIntersectsOBB() {
        BoxCollider2D aabb = new BoxCollider2D(new Vec2(4, 4));
        aabb.setTransform(new Transform(new Vec2(1, 0).rotate(45)));
        assertTrue(c.detectCollision(aabb));
    }

}
