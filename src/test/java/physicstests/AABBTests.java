package physicstests;

import org.junit.Before;
import org.junit.Test;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.primitives.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.primitives.Edge2D;

import static junit.framework.TestCase.*;

/**
 * Unit tests for {@link AlignedBoxCollider2D} class.
 *
 * @author SlavSquatSuperstar
 */
public class AABBTests {

    AlignedBoxCollider2D aabb;

    // Create box centered at (0, 0) with dimensions 4x4
    @Before
    public void getAABB() {
        aabb = new AlignedBoxCollider2D(new Vector2(4, 4));
        aabb.setTransform(new Transform());
        aabb.setRigidBody(new Rigidbody2D(0f));
    }

    @Test
    public void aabbIsAtObjectCenter() {
        assertEquals(new Vector2(0, 0), aabb.center());
    }

    @Test
    public void vertexPointIsInOBB() {
        for (Vector2 v : aabb.getVertices())
            assertTrue(aabb.contains(v));
    }

    @Test
    public void pointNotInAABB() {
        assertFalse(aabb.contains(new Vector2(3, -3)));
        assertFalse(aabb.contains(new Vector2(-3, 3)));
        assertFalse(aabb.contains(new Vector2(-3, -3)));
        assertFalse(aabb.contains(new Vector2(3, 3)));
    }

    @Test
    public void tangentLineIsInAABB() {
        assertTrue(aabb.intersects(new Edge2D(new Vector2(1, 3), new Vector2(3, 1))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(-1, -3), new Vector2(-3, -1))));
    }

    @Test
    public void bisectLineIsInAABB() {
        assertTrue(aabb.intersects(new Edge2D(new Vector2(-2, -2), new Vector2(2, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(-2, 2), new Vector2(2, -2))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(0, -2), new Vector2(0, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(-2, 0), new Vector2(2, 0))));
    }

    @Test
    public void edgeLineIsInAABB() {
        assertTrue(aabb.intersects(new Edge2D(new Vector2(-2, -2), new Vector2(-2, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(2, -2), new Vector2(2, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(-2, -2), new Vector2(2, -2))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(-2, 2), new Vector2(2, 2))));
    }

    @Test
    public void lineIsInAABB() {
        assertTrue(aabb.intersects(new Edge2D(new Vector2(0, 0), new Vector2(-2, -2))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(0, 0), new Vector2(2, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(1, 2), new Vector2(-1, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(1, 1), new Vector2(3, 3))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(2, 2), new Vector2(3, 3))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(2, 2), new Vector2(2, 3))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(2, 2), new Vector2(3, 1))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(-1, -1), new Vector2(-3, -3))));
        assertTrue(aabb.intersects(new Edge2D(new Vector2(3, 2), new Vector2(2, 2))));

    }

    @Test
    public void lineNotInAABB() {
        assertFalse(aabb.intersects(new Edge2D(new Vector2(3, 3), new Vector2(4, 4))));
        assertFalse(aabb.intersects(new Edge2D(new Vector2(4, 4), new Vector2(3, 3))));
        assertFalse(aabb.intersects(new Edge2D(new Vector2(5, 2), new Vector2(4, 2))));
        assertFalse(aabb.intersects(new Edge2D(new Vector2(3, 1), new Vector2(3, -1))));
    }

    @Test
    public void aabbIntersectsCircle() {
        CircleCollider c = new CircleCollider(4);
        c.setTransform(new Transform(new Vector2(2, 2)));
        c.setRigidBody(new Rigidbody2D(0f));
        assertTrue(aabb.detectCollision(c));
    }

    @Test
    public void aabbIntersectsAABB() {
        AlignedBoxCollider2D other = new AlignedBoxCollider2D(new Vector2(4, 4));
        other.setTransform(new Transform(new Vector2(2, 2)));
        other.setRigidBody(new Rigidbody2D(0f));
        assertTrue(aabb.detectCollision(other));
    }

}
