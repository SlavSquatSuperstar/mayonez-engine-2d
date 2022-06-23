package slavsquatsuperstar.test.collisiontests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.physics2d.colliders.BoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.CircleCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.Edge2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.Ray2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BoxCollider2D} class.
 *
 * @author SlavSquatSuperstar
 */
public class BoxTests {

    static BoxCollider2D box;

    // Create box centered at (0, 0) with dimensions 4x4 with a rotation of 45 degrees clockwise
    @BeforeAll
    public static void getBox() {
        box = new BoxCollider2D(new Vec2(2, 2)).setTransform(
                new Transform().rotate(45).resize(new Vec2(2, 2)));
    }

    // Contains Point

    @Test
    public void vertexPointIsInBox() {
        for (Vec2 v : box.getVertices())
            assertTrue(box.contains(v));
    }

    @Test
    public void edgePointIsInBox() {
        assertTrue(box.contains(new Vec2(2, 0).rotate(45)));
        assertTrue(box.contains(new Vec2(0, -2).rotate(45)));
    }

    @Test
    public void interiorPointIsInBox() {
        assertTrue(box.contains(new Vec2(1.9f, -1.9f).rotate(45)));
        assertTrue(box.contains(new Vec2(-1, -1).rotate(45)));
    }

    @Test
    public void exteriorPointNotInBox() {
        assertFalse(box.contains(new Vec2(2.1f, -2.1f).rotate(45)));
        assertFalse(box.contains(new Vec2(3, 3)));
        assertFalse(box.contains(new Vec2(0, 5)));
    }

    // Nearest Point

    @Test
    public void nearestPointInsideBox() {
        assertEquals(new Vec2(1, 1), box.nearestPoint(new Vec2(1, 1)));
    }

    @Test
    public void nearestPointOutsideBox() {
        assertEquals(new Vec2(2, 0).rotate(45), box.nearestPoint(new Vec2(2, 2)));
    }

    // Raycast

    @Test
    public void outsideRayHitsBox() {
        assertNotNull(box.raycast(new Ray2D(new Vec2(-10, 0), new Vec2(1, 0)), 0));
        assertNotNull(box.raycast(new Ray2D(new Vec2(-5, 5), new Vec2(1, -1)), 0));
    }

    @Test
    public void limitedOutsideRayHitsBox() {
        assertNotNull(box.raycast(new Ray2D(new Vec2(-4, 0), new Vec2(1, 0)), 5));
        assertNotNull(box.raycast(new Ray2D(new Vec2(-4, 4), new Vec2(1, -1)), 5));
    }

    @Test
    public void outsideRayMissesBox() {
        assertNull(box.raycast(new Ray2D(new Vec2(-10, 0), new Vec2(-1, 0)), 0));
        assertNull(box.raycast(new Ray2D(new Vec2(-5, 5), new Vec2(-1, 1)), 0));
    }

    @Test
    public void insideRayHitsBox() {
        assertNotNull(box.raycast(new Ray2D(new Vec2(-2, 0), new Vec2(1, 0)), 0));
        assertNotNull(box.raycast(new Ray2D(new Vec2(-1, 1), new Vec2(1, -1)), 0));
    }

    // Line Intersection

    @Test
    public void secantLineIsInBox() {
        assertTrue(box.intersects(new Edge2D(new Vec2(2, 5), new Vec2(2, -5))));
        assertTrue(box.intersects(new Edge2D(new Vec2(-5, 1), new Vec2(5, 2))));
    }

    @Test
    public void bisectLineIsInBox() {
        assertTrue(box.intersects(new Edge2D(new Vec2(0, 4), new Vec2(0, -4))));
        assertTrue(box.intersects(new Edge2D(new Vec2(-4, 4), new Vec2(4, -4))));
    }

    @Test
    public void edgeLineIsInBox() {
        Edge2D[] edges = box.getEdges();
        for (Edge2D edge : edges)
            assertTrue(box.intersects(edge));
    }

    // Box vs Shape

    @Test
    public void BoxIntersectsCircle() {
        CircleCollider2D c = new CircleCollider2D(4);
        c.setTransform(new Transform(new Vec2(2, 2)));
        assertTrue(box.detectCollision(c));
    }

    @Test
    public void BoxIntersectsBox() {
        BoxCollider2D box = new BoxCollider2D(new Vec2(4, 4));
        box.setTransform(new Transform(new Vec2(1.5f, 1.5f)).rotate(45));
        assertTrue(box.detectCollision(box));
    }

}
