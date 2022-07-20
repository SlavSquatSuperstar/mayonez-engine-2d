package slavsquatsuperstar.test.collisiontests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.physics.colliders.CircleCollider;
import slavsquatsuperstar.mayonez.physics.colliders.Edge2D;
import slavsquatsuperstar.mayonez.physics.colliders.Ray;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BoxCollider} class.
 *
 * @author SlavSquatSuperstar
 */
public class BoxTests {

    static BoxCollider box;

    // 4x4 centered at (0, 0) rotated by 45 degrees
    @BeforeAll
    public static void getBox() {
        box = new BoxCollider(new Vec2(2, 2)).setTransform(new Transform(new Vec2(), 45, new Vec2(2, 2)));
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
        assertEquals(new Vec2(2, 2).rotate(45), box.nearestPoint(new Vec2(3, 4).rotate(45)));
    }

    // Raycast

    @Test
    public void outsideRayHitsBox() {
        assertNotNull(box.raycast(new Ray(new Vec2(-10, 0), new Vec2(1, 0)), 0));
        assertNotNull(box.raycast(new Ray(new Vec2(-5, 5), new Vec2(1, -1)), 0));
    }

    @Test
    public void limitedOutsideRayHitsBox() {
        assertNotNull(box.raycast(new Ray(new Vec2(-4, 0), new Vec2(1, 0)), 5));
        assertNotNull(box.raycast(new Ray(new Vec2(-4, 4), new Vec2(1, -1)), 5));
    }

    @Test
    public void outsideRayMissesBox() {
        assertNull(box.raycast(new Ray(new Vec2(-10, 0), new Vec2(-1, 0)), 0));
        assertNull(box.raycast(new Ray(new Vec2(-5, 5), new Vec2(-1, 1)), 0));
    }

    @Test
    public void limitedOutsideRayMissesBox() {
        assertNull(box.raycast(new Ray(new Vec2(-10, 0), new Vec2(1, 0)), 2));
        assertNull(box.raycast(new Ray(new Vec2(-10, 10), new Vec2(1, -1)), 2));
    }

    @Test
    public void insideRayHitsBox() {
        assertNotNull(box.raycast(new Ray(new Vec2(-1.5f, 0), new Vec2(1, 0)), 0));
        assertNotNull(box.raycast(new Ray(new Vec2(-1, 1), new Vec2(1, -1)), 0));
    }

    // Line Intersection

    @Test
    public void interiorLineIsInBox() {
        assertTrue(box.intersects(new Edge2D(new Vec2(1.5f, 1.5f), new Vec2(-1, -1))));
    }

    @Test
    public void tangentLineIsInBox() {
        assertTrue(box.intersects(new Edge2D(new Vec2(1, 3).rotate(45), new Vec2(3, 1).rotate(45))));
        assertTrue(box.intersects(new Edge2D(new Vec2(-1, -3).rotate(45), new Vec2(-3, -1).rotate(45))));
    }

    @Test
    public void secantLineIsInBox() {
        assertTrue(box.intersects(new Edge2D(new Vec2(2, 5), new Vec2(2, -5))));
        assertTrue(box.intersects(new Edge2D(new Vec2(-5, 1), new Vec2(5, 2))));
    }

    @Test
    public void bisectLineIsInBox() {
        assertTrue(box.intersects(new Edge2D(new Vec2(0, 4), new Vec2(0, -4))));
        assertTrue(box.intersects(new Edge2D(new Vec2(-4, 4), new Vec2(4, -4))));
        assertTrue(box.intersects(new Edge2D(new Vec2(-3, -3), new Vec2(3, 3))));
        assertTrue(box.intersects(new Edge2D(new Vec2(0, -3), new Vec2(0, 3))));
        assertTrue(box.intersects(new Edge2D(new Vec2(-2, 0), new Vec2(2, 0))));
    }

    @Test
    public void edgeLineIsInBox() {
        Edge2D[] edges = box.getEdges();
        for (Edge2D edge : edges)
            assertTrue(box.intersects(edge));

//        assertTrue(aabb.intersects(new Edge2D(new Vec2(-2, -2), new Vec2(-2, 2))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, 3), new Vec2(2, -3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(-2.1f, 2), new Vec2(1.9f, 2))));
    }

//    @Test
//    public void lineIsInAABB() {
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(0, 0), new Vec2(-2, -2))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(0, 0), new Vec2(2, 2))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(1, 2), new Vec2(-1, 2))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(1, 1), new Vec2(3, 3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, 2), new Vec2(3, 3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, 2), new Vec2(2, 3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, 2), new Vec2(3, 1))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(-1, -1), new Vec2(-3, -3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(3, 2), new Vec2(2, 2))));
//    }
//
//    @Test
//    public void lineNotInAABB() {
//        assertFalse(aabb.intersects(new Edge2D(new Vec2(3, 3), new Vec2(4, 4))));
//        assertFalse(aabb.intersects(new Edge2D(new Vec2(4, 4), new Vec2(3, 3))));
//        assertFalse(aabb.intersects(new Edge2D(new Vec2(5, 2), new Vec2(4, 2))));
//        assertFalse(aabb.intersects(new Edge2D(new Vec2(3, 1), new Vec2(3, -1))));
//    }

    // Box vs Shape

    @Test
    public void boxIntersectsCircle() {
        CircleCollider c = new CircleCollider(4);
        c.setTransform(new Transform(new Vec2(2, 2)));
        assertTrue(box.detectCollision(c));
    }

    @Test
    public void boxIntersectsBox() {
        BoxCollider box = new BoxCollider(new Vec2(4, 4));
        box.setTransform(new Transform(new Vec2(1.5f, 1.5f), 45f));
        assertTrue(box.detectCollision(box));
    }

}
