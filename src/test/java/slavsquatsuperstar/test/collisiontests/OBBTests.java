package slavsquatsuperstar.test.collisiontests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.physics2d.colliders.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BoxCollider2D} class.
 *
 * @author SlavSquatSuperstar
 */
public class OBBTests {

    static BoxCollider2D obb;

    // Create box centered at (0, 0) with dimensions 4x4 with a rotation of 45 degrees clockwise
    @BeforeAll
    public static void getOBB() {
        obb = new BoxCollider2D(new Vec2(2, 2)).setTransform(
                new Transform().rotate(45).resize(new Vec2(2, 2)));
    }

    // Contains Point

    @Test
    public void vertexPointIsInOBB() {
        for (Vec2 v : obb.getVertices())
            assertTrue(obb.contains(v));
    }

    @Test
    public void edgePointIsInOBB() {
        assertTrue(obb.contains(new Vec2(2, 0).rotate(45)));
        assertTrue(obb.contains(new Vec2(0, -2).rotate(45)));
    }

    @Test
    public void interiorPointIsInOBB() {
        assertTrue(obb.contains(new Vec2(1.9f, -1.9f).rotate(45)));
        assertTrue(obb.contains(new Vec2(-1, -1).rotate(45)));
    }

    @Test
    public void exteriorPointNotInOBB() {
        assertFalse(obb.contains(new Vec2(2.1f, -2.1f).rotate(45)));
        assertFalse(obb.contains(new Vec2(3, 3)));
        assertFalse(obb.contains(new Vec2(0, 5)));
    }

    // Nearest Point

    @Test
    public void nearestPointInsideOBB() {
        assertEquals(new Vec2(1, 1), obb.nearestPoint(new Vec2(1, 1)));
    }

    @Test
    public void nearestPointOutsideOBB() {
        assertEquals(new Vec2(2, 0).rotate(45), obb.nearestPoint(new Vec2(2, 2)));
    }

    // Raycast

    @Test
    public void outsideRayHitsOBB() {
        assertNotNull(obb.raycast(new Ray2D(new Vec2(-10, 0), new Vec2(1, 0)), 0));
        assertNotNull(obb.raycast(new Ray2D(new Vec2(-5, 5), new Vec2(1, -1)), 0));
    }

    @Test
    public void limitedOutsideRayHitsOBB() {
        assertNotNull(obb.raycast(new Ray2D(new Vec2(-4, 0), new Vec2(1, 0)), 5));
        assertNotNull(obb.raycast(new Ray2D(new Vec2(-4, 4), new Vec2(1, -1)), 5));
    }

    @Test
    public void outsideRayMissesOBB() {
        assertNull(obb.raycast(new Ray2D(new Vec2(-10, 0), new Vec2(-1, 0)), 0));
        assertNull(obb.raycast(new Ray2D(new Vec2(-5, 5), new Vec2(-1, 1)), 0));
    }

    @Test
    public void insideRayHitsOBB() {
        assertNotNull(obb.raycast(new Ray2D(new Vec2(-2, 0), new Vec2(1, 0)), 0));
        assertNotNull(obb.raycast(new Ray2D(new Vec2(-1, 1), new Vec2(1, -1)), 0));
    }

    // Line Intersection

    @Test
    public void secantLineIsInOBB() {
        assertTrue(obb.intersects(new Edge2D(new Vec2(2, 5), new Vec2(2, -5))));
        assertTrue(obb.intersects(new Edge2D(new Vec2(-5, 1), new Vec2(5, 2))));
    }

    @Test
    public void bisectLineIsInOBB() {
        assertTrue(obb.intersects(new Edge2D(new Vec2(0, 4), new Vec2(0, -4))));
        assertTrue(obb.intersects(new Edge2D(new Vec2(-4, 4), new Vec2(4, -4))));
    }

    @Test
    public void edgeLineIsInOBB() {
        Edge2D[] edges = obb.getEdges();
        for (Edge2D edge : edges)
            assertTrue(obb.intersects(edge));
    }

    // OBB vs Shape

    @Test
    public void obbIntersectsCircle() {
        CircleCollider c = new CircleCollider(4);
        c.setTransform(new Transform(new Vec2(2, 2)));
        assertTrue(obb.detectCollision(c));
    }

    @Test
    public void obbIntersectsAABB() {
        AlignedBoxCollider2D box = new AlignedBoxCollider2D(new Vec2(4, 4));
        box.setTransform(new Transform(new Vec2(2, 2)));
        assertTrue(obb.detectCollision(box));
    }

    @Test
    public void obbIntersectsOBB() {
        BoxCollider2D box = new BoxCollider2D(new Vec2(4, 4));
        box.setTransform(new Transform(new Vec2(1.5f, 1.5f)).rotate(45));
        assertTrue(obb.detectCollision(box));
    }

}
