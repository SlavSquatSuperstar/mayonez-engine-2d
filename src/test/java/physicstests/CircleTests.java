package physicstests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.colliders.Edge2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CircleTests} class.
 *
 * @author SlavSquatSuperstar
 */
public class CircleTests {

    static CircleCollider c;

    // Create circle centered at (0, 0) with radius 2
    @BeforeAll
    public static void getCircle() {
        c = new CircleCollider(2).setTransform(new Transform());
    }

    // Circle vs Point

    @Test
    public void centerPointIsInCircle() {
        assertTrue(c.contains(c.center()));
    }

    @Test
    public void edgePointIsInCircle() {
        assertTrue(c.contains(new Vec2(-2, 0)));
        assertTrue(c.contains(new Vec2(0, -2)));
        assertTrue(c.contains(new Vec2(0, 2)));
        assertTrue(c.contains(new Vec2(2, 0)));
    }

    @Test
    public void pointNotInCircle() {
        assertFalse(c.contains(new Vec2(-2, -2)));
        assertFalse(c.contains(new Vec2(2, 2)));
    }

    @Test
    public void nearestPointInsideCircle() {
        assertEquals(c.center(), c.nearestPoint(c.center()));
        assertEquals(new Vec2(2, 0), c.nearestPoint(new Vec2(2, 0)));
    }

    @Test
    public void nearestPointOutsideCircle() {
        assertEquals(new Vec2(2, 0), c.nearestPoint(new Vec2(4, 0)));
        assertEquals(new Vec2(-2, 0), c.nearestPoint(new Vec2(-4, 0)));
    }

    // Circle vs Line

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
    public void lineIsInCircle() {
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
        other.setRigidBody(new Rigidbody2D(0f));
        assertTrue(c.detectCollision(other));
    }

    @Test
    public void circleIntersectsAABB() {
        AlignedBoxCollider2D aabb = new AlignedBoxCollider2D(new Vec2(4, 4));
        aabb.setTransform(new Transform(new Vec2(0, 0)));
        aabb.setRigidBody(new Rigidbody2D(0f));
        assertTrue(c.detectCollision(aabb));
    }

}
