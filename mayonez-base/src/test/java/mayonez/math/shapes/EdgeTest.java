package mayonez.math.shapes;

import mayonez.math.*;
import mayonez.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.shapes.Edge} class.
 *
 * @author SlavSquatSuperstar
 */
class EdgeTest {

    // Clip Edge
    @Test
    void clipEdgeBothEnds() {
        var edge = new Edge(new Vec2(1, 1), new Vec2(5, 1));
        var segment = new Edge(new Vec2(2, 0), new Vec2(4, 0));
        assertEquals(edge.clipToSegment(segment), new Edge(new Vec2(2, 1), new Vec2(4, 1)));
    }

    @Test
    void clipEdgeOneEnd() {
        var edge = new Edge(new Vec2(1, 1), new Vec2(5, 1));
        var seg1 = new Edge(new Vec2(0, 0), new Vec2(4, 0));
        assertEquals(edge.clipToSegment(seg1), new Edge(new Vec2(1, 1), new Vec2(4, 1)));
        var seg2 = new Edge(new Vec2(2, 0), new Vec2(6, 0));
        assertEquals(edge.clipToSegment(seg2), new Edge(new Vec2(2, 1), new Vec2(5, 1)));
    }

    @Test
    void clipEdgeNoEnds() {
        var edge = new Edge(new Vec2(1, 1), new Vec2(5, 1));
        var segment = new Edge(new Vec2(0, 0), new Vec2(6, 0));
        assertEquals(edge.clipToSegment(segment), edge);
    }

    @Test
    void clipEdgeAngledBothEnds() {
        var edge = new Edge(new Vec2(0, 1), new Vec2(5, 1));
        var segment = new Edge(new Vec2(4.5f, 0), new Vec2(2.1f, -1.2f));
        assertEquals(edge.clipToSegment(segment), new Edge(new Vec2(1, 1), new Vec2(4, 1)));
    }

    @Test
    void clipEdgeAngledOneEnd() {
        var edge = new Edge(new Vec2(0, 1), new Vec2(5, 1));
        var segment = new Edge(new Vec2(4.5f, 0), new Vec2(0.5f, -2));
        assertEquals(edge.clipToSegment(segment), new Edge(new Vec2(0, 1), new Vec2(4, 1)));
    }

    @Test
    void clipEdgeAngledNoEnds() {
        var edge = new Edge(new Vec2(0, 1), new Vec2(5, 1));
        var segment = new Edge(new Vec2(6.5f, 1), new Vec2(0.5f, -2));
        assertEquals(edge.clipToSegment(segment), new Edge(new Vec2(0, 1), new Vec2(5, 1)));
    }

    // Lerp Methods

    @Test
    void lerpSuccess() {
        var edge = new Edge(new Vec2(0, 1), new Vec2(5, 1));
        assertEquals(new Vec2(2, 1), edge.lerp(0.4f)); // middle
        assertEquals(new Vec2(6, 1), edge.lerp(1.2f)); // past end
        assertEquals(new Vec2(-5, 1), edge.lerp(-1)); // negative
    }

    @Test
    void invLerpSuccess() {
        var edge = new Edge(new Vec2(0, 1), new Vec2(5, 1));
        assertEquals(0.4f, edge.invLerp(new Vec2(2, 2))); // middle
        assertEquals(1.2f, edge.invLerp(new Vec2(6, 1))); // past end
        assertEquals(-1, edge.invLerp(new Vec2(-5, 1))); // negative
    }

    // Point vs Line

    @Test
    void pointVsEdgeHitInterior() {
        var e1 = new Edge(new Vec2(0, 0), new Vec2(2, 2));
        assertTrue(e1.contains(new Vec2(1, 1))); // oblique

        var e2 = new Edge(new Vec2(0, 0), new Vec2(2, 0));
        assertTrue(e2.contains(new Vec2(1, 0))); // horizontal

        var e3 = new Edge(new Vec2(0, 0), new Vec2(0, 2));
        assertTrue(e3.contains(new Vec2(0, 1))); // vertical
    }

    @Test
    void pointVsEdgeHitEndpoint() {
        var e = new Edge(new Vec2(0, 0), new Vec2(2, 2));
        assertTrue(e.contains(new Vec2(2, 2)));
    }

    @Test
    void pointNotInEdge() {
        var e = new Edge(new Vec2(0, 0), new Vec2(2, 2));
        assertFalse(e.contains(new Vec2(3, 3))); // outside
        assertFalse(e.contains(new Vec2(1, 0))); // above
    }

    // Unit Normals
    @Test
    void unitNormalsCorrect() { // always to the left of line vector
        var e1 = new Edge(new Vec2(0, 0), new Vec2(4, 0));
        assertEquals(new Vec2(0, 1), e1.unitNormal());

        var e2 = new Edge(new Vec2(4, 0), new Vec2(0, 0));
        assertEquals(new Vec2(0, -1), e2.unitNormal());
    }

    @Test
    void unitNormalsDirectionalCorrect() { // always to the left of line vector
        var e = new Edge(new Vec2(0, 0), new Vec2(4, 0));
        assertEquals(new Vec2(0, 1), e.unitNormal(new Vec2(1, 1)));
        assertEquals(new Vec2(0, -1), e.unitNormal(new Vec2(1, -1)));
        assertEquals(new Vec2(0, 0), e.unitNormal(new Vec2(1, 0)));
    }

    // Line vs Line

    @Test
    void edgeVsEdgeHitOblique() {
        var e1 = new Edge(new Vec2(0, 0), new Vec2(4, 3));
        var e2 = new Edge(new Vec2(4, 0), new Vec2(0, 3));
        TestUtils.assertShapeCollision(e1, e2); // x-shape

        var e3 = new Edge(new Vec2(0, 0), new Vec2(2, 2));
        var e4 = new Edge(new Vec2(1, 1), new Vec2(-1, 3));
        TestUtils.assertShapeCollision(e3, e4); // t-shape
    }

    @Test
    void edgeVsEdgeHitParallel() {
        var e1 = new Edge(new Vec2(0, 0), new Vec2(2, 2));
        var e2 = new Edge(new Vec2(1, 1), new Vec2(3, 3));
        TestUtils.assertShapeCollision(e1, e2);
    }

    @Test
    void edgeVsEdgeMissOblique() {
        var e1 = new Edge(new Vec2(0, 0), new Vec2(4, 3));
        var e2 = new Edge(new Vec2(9, 1), new Vec2(5, 4));
        TestUtils.assertNoShapeCollision(e1, e2);
    }

    @Test
    void edgeVsEdgeMissParallel() {
        var e1 = new Edge(new Vec2(0, 0), new Vec2(2, 2));
        var e2 = new Edge(new Vec2(0, 1), new Vec2(2, 3));
        TestUtils.assertNoShapeCollision(e1, e2);
    }

    // Line vs Shape

    @Test
    void boxVsLineHitBisect() {
        Rectangle rect = new Rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rect at (0, 0)
        var e1 = new Edge(new Vec2(-1, 3), new Vec2(3, -1));
        TestUtils.assertShapeCollision(rect, e1); // adjacent edges

        var e2 = new Edge(new Vec2(-1, 3), new Vec2(1, -3));
        TestUtils.assertShapeCollision(rect, e2); // opposite edges

        var e3 = new Edge(new Vec2(0, 3), new Vec2(0, -3));
        TestUtils.assertShapeCollision(rect, e3); // perpendicular
    }

    @Test
    void boxVsLineHitBoundary() {
        Rectangle rect = new Rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rect at (0, 0)
        var e1 = new Edge(new Vec2(-1, 2), new Vec2(3, 2));
        TestUtils.assertShapeCollision(rect, e1); // parallel to edge

        var e2 = new Edge(new Vec2(1, 3), new Vec2(3, 1));
        TestUtils.assertShapeCollision(rect, e2); // hits corner
    }

    @Test
    void boxVsLineMiss() {
        Rectangle rect = new Rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rect at (0, 0)
        var e = new Edge(new Vec2(1, 8), new Vec2(3, 1));
        TestUtils.assertNoShapeCollision(rect, e); // parallel to edge
    }

    @Test
    void circleVsLineHit() {
        Circle c = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        var e1 = new Edge(new Vec2(1, 3), new Vec2(1, -3));
        TestUtils.assertShapeCollision(c, e1); // secant

        var e2 = new Edge(new Vec2(2, 3), new Vec2(2, -3));
        TestUtils.assertShapeCollision(c, e2); // tangent
    }

    @Test
    void circleVsLineMiss() {
        Circle c = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        var e = new Edge(new Vec2(3, 3), new Vec2(3, -3));
        TestUtils.assertNoShapeCollision(c, e); // secant
    }

    @Test
    void triVsLineHit() {
        Triangle tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        var e = new Edge(new Vec2(0, 2), new Vec2(2, 2));
        TestUtils.assertShapeCollision(tri, e);
    }

    @Test
    void triVsLineMiss() {
        Triangle tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        var e = new Edge(new Vec2(0, 4), new Vec2(2, 4));
        TestUtils.assertNoShapeCollision(tri, e);
    }

}
