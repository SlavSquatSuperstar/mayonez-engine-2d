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
 * Unit tests for {@link AlignedBoxCollider2D} class.
 *
 * @author SlavSquatSuperstar
 */
public class AABBTests {

    static AlignedBoxCollider2D aabb;

    // Create box centered at (0, 0) with dimensions 4x4
    @BeforeAll
    public static void getAABB() {
        aabb = new AlignedBoxCollider2D(new Vec2(4, 4)).setTransform(new Transform());
    }

    @Test
    public void aabbIsAtObjectCenter() {
        assertEquals(new Vec2(0, 0), aabb.center());
    }

    @Test
    public void vertexPointIsInOBB() {
        for (Vec2 v : aabb.getVertices())
            assertTrue(aabb.contains(v));
    }

    @Test
    public void pointNotInAABB() {
        assertFalse(aabb.contains(new Vec2(3, -3)));
        assertFalse(aabb.contains(new Vec2(-3, 3)));
        assertFalse(aabb.contains(new Vec2(-3, -3)));
        assertFalse(aabb.contains(new Vec2(3, 3)));
    }

    @Test
    public void tangentLineIsInAABB() {
        assertTrue(aabb.intersects(new Edge2D(new Vec2(1, 3), new Vec2(3, 1))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(-1, -3), new Vec2(-3, -1))));
    }

    @Test
    public void bisectLineIsInAABB() {
        assertTrue(aabb.intersects(new Edge2D(new Vec2(-2, -2), new Vec2(2, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(-2, 2), new Vec2(2, -2))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(0, -2), new Vec2(0, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(-2, 0), new Vec2(2, 0))));
    }

    @Test
    public void edgeLineIsInAABB() {
        assertTrue(aabb.intersects(new Edge2D(new Vec2(-2, -2), new Vec2(-2, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, -2), new Vec2(2, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(-2, -2), new Vec2(2, -2))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(-2, 2), new Vec2(2, 2))));
    }

    @Test
    public void lineIsInAABB() {
        assertTrue(aabb.intersects(new Edge2D(new Vec2(0, 0), new Vec2(-2, -2))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(0, 0), new Vec2(2, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(1, 2), new Vec2(-1, 2))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(1, 1), new Vec2(3, 3))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, 2), new Vec2(3, 3))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, 2), new Vec2(2, 3))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, 2), new Vec2(3, 1))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(-1, -1), new Vec2(-3, -3))));
        assertTrue(aabb.intersects(new Edge2D(new Vec2(3, 2), new Vec2(2, 2))));

    }

    @Test
    public void lineNotInAABB() {
        assertFalse(aabb.intersects(new Edge2D(new Vec2(3, 3), new Vec2(4, 4))));
        assertFalse(aabb.intersects(new Edge2D(new Vec2(4, 4), new Vec2(3, 3))));
        assertFalse(aabb.intersects(new Edge2D(new Vec2(5, 2), new Vec2(4, 2))));
        assertFalse(aabb.intersects(new Edge2D(new Vec2(3, 1), new Vec2(3, -1))));
    }

    @Test
    public void aabbIntersectsCircle() {
        CircleCollider c = new CircleCollider(4);
        c.setTransform(new Transform(new Vec2(2, 2)));
        c.setRigidBody(new Rigidbody2D(0f));
        assertTrue(aabb.detectCollision(c));
    }

    @Test
    public void aabbIntersectsAABB() {
        AlignedBoxCollider2D other = new AlignedBoxCollider2D(new Vec2(4, 4));
        other.setTransform(new Transform(new Vec2(2, 2)));
        other.setRigidBody(new Rigidbody2D(0f));
        assertTrue(aabb.detectCollision(other));
    }

}
