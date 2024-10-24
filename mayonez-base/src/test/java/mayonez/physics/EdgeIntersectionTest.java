package mayonez.physics;

import mayonez.math.*;
import mayonez.math.shapes.*;
import org.junit.jupiter.api.*;

import static mayonez.physics.CollisionTestUtils.*;

/**
 * Unit tests for the {@link mayonez.math.shapes.Edge} and
 * {@link mayonez.physics.Collisions} classes.
 *
 * @author SlavSquatSuperstar
 */
public class EdgeIntersectionTest {

    // Line vs Line

    @Test
    void edgeVsEdgeHitOblique() {
        var e1 = new Edge(new Vec2(0, 0), new Vec2(4, 3));
        var e2 = new Edge(new Vec2(4, 0), new Vec2(0, 3));
        assertShapeCollision(e1, e2); // x-shape

        var e3 = new Edge(new Vec2(0, 0), new Vec2(2, 2));
        var e4 = new Edge(new Vec2(1, 1), new Vec2(-1, 3));
        assertShapeCollision(e3, e4); // t-shape
    }

    @Test
    void edgeVsEdgeHitParallel() {
        var e1 = new Edge(new Vec2(0, 0), new Vec2(2, 2));
        var e2 = new Edge(new Vec2(1, 1), new Vec2(3, 3));
        assertShapeCollision(e1, e2);
    }

    @Test
    void edgeVsEdgeMissOblique() {
        var e1 = new Edge(new Vec2(0, 0), new Vec2(4, 3));
        var e2 = new Edge(new Vec2(9, 1), new Vec2(5, 4));
        assertNoShapeCollision(e1, e2);
    }

    @Test
    void edgeVsEdgeMissParallel() {
        var e1 = new Edge(new Vec2(0, 0), new Vec2(2, 2));
        var e2 = new Edge(new Vec2(0, 1), new Vec2(2, 3));
        assertNoShapeCollision(e1, e2);
    }

    // Line vs Shape

    @Test
    void boxVsLineHitBisect() {
        Rectangle rect = new Rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rect at (0, 0)
        var e1 = new Edge(new Vec2(-1, 3), new Vec2(3, -1));
        assertShapeCollision(rect, e1); // adjacent edges

        var e2 = new Edge(new Vec2(-1, 3), new Vec2(1, -3));
        assertShapeCollision(rect, e2); // opposite edges

        var e3 = new Edge(new Vec2(0, 3), new Vec2(0, -3));
        assertShapeCollision(rect, e3); // perpendicular
    }

    @Test
    void boxVsLineHitBoundary() {
        Rectangle rect = new Rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rect at (0, 0)
        var e1 = new Edge(new Vec2(-1, 2), new Vec2(3, 2));
        assertShapeCollision(rect, e1); // parallel to edge

        var e2 = new Edge(new Vec2(1, 3), new Vec2(3, 1));
        assertShapeCollision(rect, e2); // hits corner
    }

    @Test
    void boxVsLineMiss() {
        Rectangle rect = new Rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rect at (0, 0)
        var e = new Edge(new Vec2(1, 8), new Vec2(3, 1));
        assertNoShapeCollision(rect, e); // parallel to edge
    }

    @Test
    void circleVsLineHit() {
        Circle c = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        var e1 = new Edge(new Vec2(1, 3), new Vec2(1, -3));
        assertShapeCollision(c, e1); // secant

        var e2 = new Edge(new Vec2(2, 3), new Vec2(2, -3));
        assertShapeCollision(c, e2); // tangent
    }

    @Test
    void circleVsLineMiss() {
        Circle c = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        var e = new Edge(new Vec2(3, 3), new Vec2(3, -3));
        assertNoShapeCollision(c, e); // secant
    }

    @Test
    void triVsLineHit() {
        Triangle tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        var e = new Edge(new Vec2(0, 2), new Vec2(2, 2));
        assertShapeCollision(tri, e);
    }

    @Test
    void triVsLineMiss() {
        Triangle tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        var e = new Edge(new Vec2(0, 4), new Vec2(2, 4));
        assertNoShapeCollision(tri, e);
    }

}
