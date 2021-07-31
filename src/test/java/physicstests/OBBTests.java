package physicstests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BoxCollider2D} class.
 *
 * @author SlavSquatSuperstar
 */
public class OBBTests {

    static BoxCollider2D obb;

    // Create box centered at (0, 0) with dimensions 4x4 with a rotation of 45 degrees counterclockwise
    @BeforeAll
    public static void getOBB() {
        obb = new BoxCollider2D(new Vec2(4, 4)).setTransform(new Transform().rotate(45));
    }

    // OBB vs Point

    @Test
    public void nearestPointInsideOBB() {
        assertEquals(new Vec2(1, 1), obb.nearestPoint(new Vec2(1, 1)));
    }

    @Test
    public void nearestPointOutsideOBB() {
        assertEquals(new Vec2(2, 0).rotate(45), obb.nearestPoint(new Vec2(2, 2)));
    }

    @Test
    public void vertexPointIsInOBB() {
        for (Vec2 v : obb.getVertices())
            assertTrue(obb.contains(v));
    }

    // OBB vs Line

    @Test
    public void outsideRayHitsOBB() {
        assertTrue(obb.raycast(new Ray2D(new Vec2(-10, 0), new Vec2(1, 0)), null, 0));
        assertTrue(obb.raycast(new Ray2D(new Vec2(-5, 5), new Vec2(1, -1)), null, 0));
    }

    @Test
    public void limitedOutsideRayHitsOBB() {
        assertTrue(obb.raycast(new Ray2D(new Vec2(-4, 0), new Vec2(1, 0)), null, 5));
        assertTrue(obb.raycast(new Ray2D(new Vec2(-4, 4), new Vec2(1, -1)), null, 5));
    }

    @Test
    public void outsideRayMissesOBB() {
        assertFalse(obb.raycast(new Ray2D(new Vec2(-10, 0), new Vec2(-1, 0)), null, 0));
        assertFalse(obb.raycast(new Ray2D(new Vec2(-5, 5), new Vec2(-1, 1)), null, 0));
    }

    @Test
    public void insideRayHitsOBB() {
        assertTrue(obb.raycast(new Ray2D(new Vec2(-2, 0), new Vec2(1, 0)), null, 0));
        assertTrue(obb.raycast(new Ray2D(new Vec2(-1, 1), new Vec2(1, -1)), null, 0));
    }

    @Test
    public void edgeLineIsInOBB() {
        Vec2[] vertices = obb.getVertices();
        for (int i = 0; i < vertices.length; i++)
            assertTrue(obb.intersects(new Edge2D(vertices[i], vertices[(i + 1) / 4])));
    }

    // OBB vs Shape

    @Test
    public void obbIntersectsCircle() {
        CircleCollider c = new CircleCollider(4);
        c.setTransform(new Transform(new Vec2(2, 2)));
        c.setRigidBody(new Rigidbody2D(0f));
        assertTrue(obb.detectCollision(c));
    }

    @Test
    public void obbIntersectsAABB() {
        AlignedBoxCollider2D box = new AlignedBoxCollider2D(new Vec2(4, 4));
        box.setTransform(new Transform(new Vec2(2, 2)));
        box.setRigidBody(new Rigidbody2D(0f));
        assertTrue(obb.detectCollision(box));
    }

}
