package slavsquatsuperstar.test.collisiontests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.colliders.Edge2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.Ray2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Edge2D} class.
 *
 * @author SlavSquatSuperstar
 */
public class EdgeTests {

    // Contains Point

    @Test
    public void endpointIsInLine() {
        Edge2D edge = new Edge2D(new Vec2(0, 0), new Vec2(10, 5));
        assertTrue(edge.contains(edge.end));
        assertTrue(edge.contains(edge.start));
    }

    @Test
    public void pointIsInLine() {
        Edge2D edge = new Edge2D(new Vec2(0, 0), new Vec2(10, 5));
        assertTrue(edge.contains(new Vec2(5, 2.5f)));
    }

    @Test
    public void endpointIsInVerticalLine() {
        Edge2D edge = new Edge2D(new Vec2(0, 0), new Vec2(0, 10));
        assertTrue(edge.contains(edge.start));
        assertTrue(edge.contains(edge.end));
    }

    @Test
    public void pointISInVerticalLine() {
        Edge2D edge = new Edge2D(new Vec2(0, 0), new Vec2(0, 10));
        assertTrue(edge.contains(new Vec2(0, 5)));
    }

    @Test
    public void pointNotInLine() {
        Edge2D edge = new Edge2D(new Vec2(-2, -2), new Vec2(2, 2));
        assertFalse(edge.contains(new Vec2(-1, 1)));
        assertFalse(edge.contains(new Vec2(1, -1)));
    }

    @Test
    public void nearestPointInsideLine() {
        Edge2D edge = new Edge2D(new Vec2(-4, -4), new Vec2(4, 4));
        assertEquals(edge.start, edge.nearestPoint(edge.start));
        assertEquals(edge.end, edge.nearestPoint(edge.end));
    }

    @Test
    public void nearestPointOutsideLine() {
        Edge2D edge = new Edge2D(new Vec2(0, 0), new Vec2(0, 4));
        assertEquals(new Vec2(0, 3), edge.nearestPoint(new Vec2(1, 3)));
        assertEquals(new Vec2(0, 3), edge.nearestPoint(new Vec2(-1, 3)));
        assertEquals(new Vec2(0, 4), edge.nearestPoint(new Vec2(2, 5)));
    }

    // Line Intersection

    @Test
    public void linesIntersectOnce() {
        Edge2D e1 = new Edge2D(new Vec2(-1, -1), new Vec2(1, 1));
        Edge2D e2 = new Edge2D(new Vec2(1, -1), new Vec2(-1, 1));
        assertTrue(e1.intersects(e2));
    }

    @Test
    public void linesIntersectOnceTShape() {
        Edge2D e1 = new Edge2D(new Vec2(-2, 0), new Vec2(2, 0));
        Edge2D e2 = new Edge2D(new Vec2(0, 0), new Vec2(0, 2));
        assertTrue(e1.intersects(e2));
    }

    @Test
    public void sameLinesIntersect() {
        Edge2D e1 = new Edge2D(new Vec2(1, 1), new Vec2(3, 1));
        Edge2D e2 = new Edge2D(new Vec2(1, 1), new Vec2(3, 1));
        assertTrue(e1.intersects(e2));
    }

    @Test
    public void overlappingLinesIntersect() {
        Edge2D e1 = new Edge2D(new Vec2(1, 1), new Vec2(3, 3));
        Edge2D e2 = new Edge2D(new Vec2(2, 2), new Vec2(4, 4));
        assertTrue(e1.intersects(e2));
    }

    @Test
    public void parallelLinesNoIntersection() {
        Edge2D e1 = new Edge2D(new Vec2(1, 1), new Vec2(3, 2));
        Edge2D e2 = new Edge2D(new Vec2(2, 2), new Vec2(4, 3));
        assertFalse(e1.intersects(e2));
    }

    @Test
    public void skewLinesNoIntersection() {
        Edge2D e1 = new Edge2D(new Vec2(0, 0), new Vec2(2, 4));
        Edge2D e2 = new Edge2D(new Vec2(1, 0), new Vec2(3, 1));
        assertFalse(e1.intersects(e2));
    }

    // Raycast

    @Test
    public void rayMissesLine() {
        Edge2D edge = new Edge2D(new Vec2(-2, 0), new Vec2(2, 0));
        assertNull(edge.raycast(new Ray2D(new Vec2(0, 2), new Vec2(0, 1)), 0));
        assertNull(edge.raycast(new Ray2D(new Vec2(-3, 2), new Vec2(0, -1)), 0));
    }

    @Test
    public void rayHitsLine() {
        Edge2D edge = new Edge2D(new Vec2(-2, 0), new Vec2(2, 0));
        assertNotNull(edge.raycast(new Ray2D(new Vec2(0, 2), new Vec2(0, -1)), 0));
        assertNotNull(edge.raycast(new Ray2D(new Vec2(-2, -2), new Vec2(0.5f, 1)), 0));
    }

    @Test
    public void lineIntersectionCorrectDistance() {
        Edge2D e1 = new Edge2D(new Vec2(-2, 0), new Vec2(2, 0));
        Edge2D e2 = new Edge2D(new Vec2(0, -2), new Vec2(0, 2));
        assertEquals(2, e1.raycast(new Ray2D(e2), e2.length()).getDistance(), MathUtils.EPSILON);
    }

    @Test
    public void lineIntersectionCorrectPoint() {
        Edge2D e1 = new Edge2D(new Vec2(-2, 0), new Vec2(2, 0));
        Edge2D e2 = new Edge2D(new Vec2(0, -2), new Vec2(0, 2));
        Ray2D ray = new Ray2D(e2);
        assertEquals(new Vec2(), ray.getPoint(e1.raycast(ray, e2.length()).getDistance()));
    }

    // Clip Line

//    @Test
//    public void clipLineBothEndpointsSuccess() {
//        BoxCollider2D box = new BoxCollider2D(new Vec2(2, 2));
//        Edge2D e1 = new Edge2D(new Vec2(-2, -1), new Vec2(1, 2)).clipToBounds(box);
//        assertEquals(new Edge2D(new Vec2(-1, 0), new Vec2(0, 1)), e1);
//        Edge2D e2 = new Edge2D(new Vec2(-2, 0), new Vec2(2, 0)).clipToBounds(box);
//        assertEquals(new Edge2D(new Vec2(-1, 0), new Vec2(1, 0)), e2);
//    }
//
//    @Test
//    public void clipLineOneEndpointSuccess() {
//        BoxCollider2D box = new BoxCollider2D(new Vec2(2, 2));
//        Edge2D e1 = new Edge2D(new Vec2(-2, 0), new Vec2(0, 0)).clipToBounds(box);
//        assertEquals(new Edge2D(new Vec2(-1, 0), new Vec2(0, 0)), e1);
//        Edge2D e2 = new Edge2D(new Vec2(-2, 2), new Vec2(0.5f, -0.5f)).clipToBounds(box);
//        assertEquals(new Edge2D(new Vec2(-1, 1), new Vec2(0.5f, -0.5f)), e2);
//    }
//
//    @Test
//    public void clipLineInsideShapeSuccess() {
//        BoxCollider2D box = new BoxCollider2D(new Vec2(2, 2));
//        Edge2D edge = new Edge2D(new Vec2(-1, 1), new Vec2(1, -1));
//        assertEquals(edge, edge.clipToBounds(box));
//    }
//
//    @Test
//    public void clipLineOutsideShapeFailure() {
//        BoxCollider2D box = new BoxCollider2D(new Vec2(2, 2));
//        assertNull(new Edge2D(new Vec2(-2, -1), new Vec2(-1, -2)).clipToBounds(box));
//    }
//
//    @Test
//    public void clipLineToPointFailure() {
//        BoxCollider2D box = new BoxCollider2D(new Vec2(2, 2));
//        assertNull(new Edge2D(new Vec2(-2, 0), new Vec2(0, -2)).clipToBounds(box));
//    }

}
