package physicstests;

import org.junit.Before;
import org.junit.Test;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.primitives.Line2D;

import static junit.framework.TestCase.*;

public class CircleTests {

    CircleCollider c;

    // Create circle centered at (0, 0) with radius 2
    @Before
    public void getCircle() {
        c = new CircleCollider(2);
        c.setTransform(new Transform(new Vector2(0, 0)));
        c.setRigidBody(new Rigidbody2D(0f));
    }

    @Test
    public void circleIsAtObjectCenter() {
        assertEquals(new Vector2(), c.getCenter());
    }

    // Circle vs Point

    @Test
    public void centerPointIsInCircle() {
        assertTrue(c.contains(c.getCenter()));
    }

    @Test
    public void edgePointIsInCircle() {
        assertTrue(c.contains(new Vector2(-2, 0)));
        assertTrue(c.contains(new Vector2(0, -2)));
        assertTrue(c.contains(new Vector2(0, 2)));
        assertTrue(c.contains(new Vector2(2, 0)));
    }

    @Test
    public void pointNotInCircle() {
        assertFalse(c.contains(new Vector2(-2, -2)));
        assertFalse(c.contains(new Vector2(2, 2)));
    }

    // Circle vs Line

    @Test
    public void tangentLineIsInCircle() {
        assertTrue(c.intersects(new Line2D(new Vector2(-2, -2), new Vector2(0, -2))));
        assertTrue(c.intersects(new Line2D(new Vector2(-2, -2), new Vector2(-2, 0))));
        assertTrue(c.intersects(new Line2D(new Vector2(0, -2), new Vector2(0, 0))));
        assertTrue(c.intersects(new Line2D(new Vector2(-2, 0), new Vector2(0, 0))));
    }

    @Test
    public void secantLineIsInCircle() {
        assertTrue(c.intersects(new Line2D(new Vector2(0, -2), new Vector2(0, 2))));
        assertTrue(c.intersects(new Line2D(new Vector2(-2, 0), new Vector2(2, 0))));
        assertTrue(c.intersects(new Line2D(new Vector2(-2, -2), new Vector2(2, 2))));
        assertTrue(c.intersects(new Line2D(new Vector2(-2, -2), new Vector2(2, 1))));
    }

    @Test
    public void lineIsInCircle() {
        assertTrue(c.intersects(new Line2D(new Vector2(1, 1), new Vector2(3, 3))));
        assertTrue(c.intersects(new Line2D(new Vector2(0, 1), new Vector2(0, 4))));
    }

    @Test
    public void lineNotInCircle() {
        assertFalse(c.intersects(new Line2D(new Vector2(-2, 3), new Vector2(2, 3))));
        assertFalse(c.intersects(new Line2D(new Vector2(0, 4), new Vector2(4, 0))));
        assertFalse(c.intersects(new Line2D(new Vector2(0, 4), new Vector2(0, 3))));
        assertFalse(c.intersects(new Line2D(new Vector2(4, 0), new Vector2(3, 0))));
        assertFalse(c.intersects(new Line2D(new Vector2(0, 3), new Vector2(0, 4))));
        assertFalse(c.intersects(new Line2D(new Vector2(3, 0), new Vector2(4, 0))));
    }

    // Circle vs Primitive

    @Test
    public void circleIntersectsCircle() {
        CircleCollider other = new CircleCollider(4);
        other.setTransform(new Transform(new Vector2(2, 2)));
        other.setRigidBody(new Rigidbody2D(0f));
        assertTrue(c.detectCollision(other));
    }

    @Test
    public void circleIntersectsAABB() {
        AlignedBoxCollider2D aabb = new AlignedBoxCollider2D(new Vector2(4, 4));
        aabb.setTransform(new Transform(new Vector2(0, 0)));
        aabb.setRigidBody(new Rigidbody2D(0f));
        assertTrue(c.detectCollision(aabb));
    }

}
