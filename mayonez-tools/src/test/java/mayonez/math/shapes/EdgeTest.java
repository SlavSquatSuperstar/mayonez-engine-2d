package mayonez.math.shapes;

import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.shapes.Edge} class.
 *
 * @author SlavSquatSuperstar
 */
class EdgeTest {

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
    void leftUnitNormalsCorrect() {
        var e1 = new Edge(new Vec2(0, 0), new Vec2(4, 0));
        assertEquals(new Vec2(0, 1), e1.unitNormalLeft());

        var e2 = new Edge(new Vec2(4, 0), new Vec2(0, 0));
        assertEquals(new Vec2(0, -1), e2.unitNormalLeft());
    }

    @Test
    void rightUnitNormalsCorrect() {
        var e1 = new Edge(new Vec2(0, 0), new Vec2(4, 0));
        assertEquals(new Vec2(0, -1), e1.unitNormalRight());

        var e2 = new Edge(new Vec2(4, 0), new Vec2(0, 0));
        assertEquals(new Vec2(0, 1), e2.unitNormalRight());
    }

    @Test
    void unitNormalsDirectionalCorrect() { // always to the left of line vector
        var e = new Edge(new Vec2(0, 0), new Vec2(4, 0));
        assertEquals(new Vec2(0, 1), e.unitNormal(new Vec2(1, 1)));
        assertEquals(new Vec2(0, -1), e.unitNormal(new Vec2(1, -1)));
        assertEquals(new Vec2(0, 0), e.unitNormal(new Vec2(1, 0)));
    }

}
