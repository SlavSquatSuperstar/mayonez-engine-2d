package slavsquatsuperstar.test.collisiontests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.colliders.Edge2D;
import slavsquatsuperstar.mayonez.physics.shapes.Ray;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Edge2D} class.
 *
 * @author SlavSquatSuperstar
 */
public class EdgeTests {

    // Nearest Point

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

    // Raycast

    @Test
    public void rayMissesLine() {
        Edge2D edge = new Edge2D(new Vec2(-2, 0), new Vec2(2, 0));
        assertNull(edge.raycast(new Ray(new Vec2(0, 2), new Vec2(0, 1)), 0));
        assertNull(edge.raycast(new Ray(new Vec2(-3, 2), new Vec2(0, -1)), 0));
    }

    @Test
    public void rayHitsLine() {
        Edge2D edge = new Edge2D(new Vec2(-2, 0), new Vec2(2, 0));
        assertNotNull(edge.raycast(new Ray(new Vec2(0, 2), new Vec2(0, -1)), 0));
        assertNotNull(edge.raycast(new Ray(new Vec2(-2, -2), new Vec2(0.5f, 1)), 0));
    }

    @Test
    public void lineIntersectionCorrectDistance() {
        Edge2D e1 = new Edge2D(new Vec2(-2, 0), new Vec2(2, 0));
        Edge2D e2 = new Edge2D(new Vec2(0, -2), new Vec2(0, 2));
        assertEquals(2, e1.raycast(new Ray(e2), e2.length()).getDistance(), MathUtils.FLOAT_EPSILON);
    }

    @Test
    public void lineIntersectionCorrectPoint() {
        Edge2D e1 = new Edge2D(new Vec2(-2, 0), new Vec2(2, 0));
        Edge2D e2 = new Edge2D(new Vec2(0, -2), new Vec2(0, 2));
        Ray ray = new Ray(e2);
        assertEquals(new Vec2(), ray.getPoint(e1.raycast(ray, e2.length()).getDistance()));
    }

}
