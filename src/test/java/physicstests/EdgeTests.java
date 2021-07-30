package physicstests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.colliders.Edge2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Edge2D} class.
 *
 * @author SlavSquatSuperstar
 */
public class EdgeTests {

    // Line vs Point

    @Test
    public void lineContainsEndpoints() {
        Edge2D edge = new Edge2D(new Vec2(0, 0), new Vec2(10, 5));
        Vec2 start = new Vec2(0, 0);
        assertTrue(edge.contains(start));
        Vec2 end = new Vec2(10, 5);
        assertTrue(edge.contains(end));
    }

    @Test
    public void lineContainsPoint() {
        Edge2D edge = new Edge2D(new Vec2(0, 0), new Vec2(10, 5));
        Vec2 point = new Vec2(5, 2.5f);
        assertTrue(edge.contains(point));
    }

    @Test
    public void verticalLineContainsEndpoints() {
        Edge2D edge = new Edge2D(new Vec2(0, 0), new Vec2(0, 10));
        Vec2 start = new Vec2(0, 0);
        assertTrue(edge.contains(start));
        Vec2 end = new Vec2(0, 10);
        assertTrue(edge.contains(end));
    }

    @Test
    public void verticalLineContainsPoint() {
        Edge2D edge = new Edge2D(new Vec2(0, 0), new Vec2(0, 10));
        Vec2 point = new Vec2(0, 5);
        assertTrue(edge.contains(point));
    }

    @Test
    public void lineIntersectsOnePoint() {
        Edge2D e1 = new Edge2D(new Vec2(-1, -1), new Vec2(1, 1));
        Edge2D e2 = new Edge2D(new Vec2(1, -1), new Vec2(-1, 1));
        assertTrue(e1.intersects(e2));
    }

    @Test
    public void nearestPointInsideLine() {
        Edge2D edge = new Edge2D(new Vec2(-4, -4), new Vec2(4, 4));
        assertEquals(edge.start, edge.nearestPoint(edge.start));
        assertEquals(edge.end, edge.nearestPoint(edge.end));
        assertEquals(edge.center(), edge.nearestPoint(edge.center()));
    }

    @Test
    public void nearestPointOutsideLine() {
        Edge2D edge = new Edge2D(new Vec2(0, 0), new Vec2(0, 4));
        assertEquals(new Vec2(0, 3), edge.nearestPoint(new Vec2(1, 3)));
        assertEquals(new Vec2(0, 3), edge.nearestPoint(new Vec2(-1, 3)));
        assertEquals(new Vec2(0, 4), edge.nearestPoint(new Vec2(2, 5)));
    }

    // Line vs Line

    @Test
    public void lineIntersectsOnePointTShape() {
        Edge2D e1 = new Edge2D(new Vec2(-1, 0), new Vec2(1, -0));
        Edge2D e2 = new Edge2D(new Vec2(0, -1), new Vec2(0, 0));
        assertTrue(e1.intersects(e2));
    }

    @Test
    public void sameLineIntersects() {
        Edge2D e1 = new Edge2D(new Vec2(1, 1), new Vec2(3, 1));
        Edge2D e2 = new Edge2D(new Vec2(1, 1), new Vec2(3, 1));
        assertTrue(e1.intersects(e2));
    }

    @Test
    public void overlappingLineIntersects() {
        Edge2D e1 = new Edge2D(new Vec2(1, 1), new Vec2(3, 3));
        Edge2D e2 = new Edge2D(new Vec2(2, 2), new Vec2(4, 4));
        assertTrue(e1.intersects(e2));
    }

    @Test
    public void parallelLineNotIntersects() {
        Edge2D e1 = new Edge2D(new Vec2(1, 1), new Vec2(3, 2));
        Edge2D e2 = new Edge2D(new Vec2(2, 2), new Vec2(4, 3));
        assertFalse(e1.intersects(e2));
    }


}
