package physicstests;

import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.Line2D;
import slavsquatsuperstar.mayonez.physics2d.RigidBody2D;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class CircleTests {

    public static CircleCollider c;

    /**
     * Create a circle centered at (2, 2) with radius 2
     */
    @BeforeClass
    public static void getCircle() {
        c = new CircleCollider(2);
        c.setTransform(new Transform(new Vector2(2, 2)));
        c.setRigidBody(new RigidBody2D());
    }

    @Test
    public void circleIsAtObjectCenter() {
        assertEquals(new Vector2(2, 2), c.center());
    }

    @Test
    public void pointIsInCircle() {
        assertTrue(c.contains(new Vector2(0, 2)));
        assertTrue(c.contains(new Vector2(2, 0)));
        assertTrue(c.contains(new Vector2(2, 2)));
        assertTrue(c.contains(new Vector2(2, 4)));
        assertTrue(c.contains(new Vector2(4, 2)));
    }

    @Test
    public void pointNotInCircle() {
        assertFalse(c.contains(new Vector2(0, 0)));
        assertFalse(c.contains(new Vector2(4, 4)));
    }

    @Test
    public void lineIsInCircle() {
        assertTrue(c.intersects(new Line2D(new Vector2(2, 0), new Vector2(2, 4))));
        assertTrue(c.intersects(new Line2D(new Vector2(0, 2), new Vector2(4, 2))));
        assertTrue(c.intersects(new Line2D(new Vector2(0, 0), new Vector2(4, 4))));
        assertTrue(c.intersects(new Line2D(new Vector2(1, 1), new Vector2(3, 3))));
        assertTrue(c.intersects(new Line2D(new Vector2(0, 0), new Vector2(4, 1))));
    }

    @Test
    public void lineNotInCircle() {
        assertFalse(c.intersects(new Line2D(new Vector2(0, 5), new Vector2(4, 5))));
    }

    @Test
    public void circleIntersectsAABB() {
        AlignedBoxCollider2D aabb = new AlignedBoxCollider2D(new Vector2(4, 4));
        aabb.setTransform(new Transform(new Vector2(0, 0)));
        aabb.setRigidBody(new RigidBody2D());
        assertTrue(c.intersects(aabb));
    }

}
