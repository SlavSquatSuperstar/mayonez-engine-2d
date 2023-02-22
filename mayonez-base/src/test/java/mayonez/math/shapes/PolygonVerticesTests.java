package mayonez.math.shapes;

import mayonez.math.Vec2;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link PolygonVertices} class to make sure its subclasses behave like it.
 *
 * @author SlavSquatSuperstar
 */
public class PolygonVerticesTests {

    // Vertices

    @Test
    public void oneVertexOrdered() {
        Vec2[] verts = {new Vec2(2, 2)};
        Vec2[] sorted = PolygonVertices.orderedVertices(verts);
        assertTrue(Objects.deepEquals(verts, sorted));
    }

    @Test
    public void twoVerticesOrdered() {
        Vec2[] verts = {new Vec2(2, 2), new Vec2(-1, -1)};
        Vec2[] sorted = PolygonVertices.orderedVertices(verts);
        assertTrue(Objects.deepEquals(verts, sorted));
    }

    @Test
    public void threeVerticesOrdered() {
        Vec2[] verts = {new Vec2(0, 3), new Vec2(-1, 0), new Vec2(2, 1)};
        Vec2[] sorted = PolygonVertices.orderedVertices(verts);
        assertEquals(verts.length, sorted.length);
        assertEquals(new Vec2(-1, 0), sorted[0]);
        assertEquals(new Vec2(2, 1), sorted[1]);
        assertEquals(new Vec2(0, 3), sorted[2]);
    }

    @Test
    public void threeVerticesHorizontalFlatOrdered() {
        Vec2[] verts = {new Vec2(2, 2), new Vec2(0, 0), new Vec2(-1, -1)};
        Vec2[] sorted = PolygonVertices.orderedVertices(verts);
        assertEquals(verts.length, sorted.length);
        assertEquals(new Vec2(-1, -1), sorted[0]);
        assertEquals(new Vec2(0, 0), sorted[1]);
        assertEquals(new Vec2(2, 2), sorted[2]);
    }

    @Test
    public void threeVerticesVerticalFlatOrdered() {
        Vec2[] verts = {new Vec2(1, 3), new Vec2(0, 1), new Vec2(-1, -1)};
        Vec2[] sorted = PolygonVertices.orderedVertices(verts);
        assertEquals(verts.length, sorted.length);
        assertEquals(new Vec2(-1, -1), sorted[0]);
        assertEquals(new Vec2(0, 1), sorted[1]);
        assertEquals(new Vec2(1, 3), sorted[2]);
    }

    @Test
    public void fourVerticesOrdered() {
        Vec2[] verts = {new Vec2(2, 3), new Vec2(2, 0), new Vec2(0, 0), new Vec2(0, 3)};
        Vec2[] sorted = PolygonVertices.orderedVertices(verts);
        assertEquals(verts.length, sorted.length);
        assertEquals(new Vec2(0, 0), sorted[0]);
        assertEquals(new Vec2(2, 0), sorted[1]);
        assertEquals(new Vec2(2, 3), sorted[2]);
        assertEquals(new Vec2(0, 3), sorted[3]);
    }

    @Test
    public void fiveVerticesOrdered() {
        Vec2[] verts = {new Vec2(0, 3), new Vec2(-2, 2), new Vec2(-2, 0),
                new Vec2(1, -1), new Vec2(2, 1)};
        Vec2[] sorted = PolygonVertices.orderedVertices(verts);
        assertEquals(verts.length, sorted.length);
        assertEquals(new Vec2(-2, 0), sorted[0]);
        assertEquals(new Vec2(1, -1), sorted[1]);
        assertEquals(new Vec2(2, 1), sorted[2]);
        assertEquals(new Vec2(0, 3), sorted[3]);
        assertEquals(new Vec2(-2, 2), sorted[4]);
    }

    // Convex Hull

    @Test
    public void threeVerticesHull() {
        Vec2[] verts = {new Vec2(0, 3), new Vec2(-1, 0), new Vec2(0, 1), new Vec2(2, 1)};
        Vec2[] hull = PolygonVertices.orderedConvexHull(verts);
        assertEquals(3, hull.length);
        assertEquals(new Vec2(-1, 0), hull[0]);
        assertEquals(new Vec2(2, 1), hull[1]);
        assertEquals(new Vec2(0, 3), hull[2]);
    }

    @Test
    public void fiveVerticesHull() {
        Vec2[] verts = {new Vec2(0, 3), new Vec2(0, 0), new Vec2(-2, 2), new Vec2(-2, 0),
                new Vec2(-1, 1), new Vec2(1, -1), new Vec2(2, 1), new Vec2(1, 2)};
        Vec2[] hull = PolygonVertices.orderedConvexHull(verts);
        assertEquals(5, hull.length);
        assertEquals(new Vec2(-2, 0), hull[0]);
        assertEquals(new Vec2(1, -1), hull[1]);
        assertEquals(new Vec2(2, 1), hull[2]);
        assertEquals(new Vec2(0, 3), hull[3]);
        assertEquals(new Vec2(-2, 2), hull[4]);
    }

}
