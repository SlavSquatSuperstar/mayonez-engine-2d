package mayonez.physics.detection;

import mayonez.math.Vec2;
import mayonez.math.shapes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static mayonez.test.TestUtils.assertFloatEquals;
import static mayonez.test.TestUtils.assertVerticesEqual;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for the {@link mayonez.physics.detection.GJKDetector} class.
 *
 * @author SlavSquatSuperstar
 */
public class GJKTests {

    private GJKDetector gjk;

    @BeforeEach
    public void getGJK() {
        gjk = new GJKDetector();
    }

    // Simplex Tests
    @Test
    public void getCirclesSimplex() {
        var c1 = new Circle(new Vec2(0, 0), 5);
        var c2 = new Circle(new Vec2(9.9f, 0), 5);
        var c3 = new Circle(new Vec2(10.1f, 0), 5);

        assertNotNull(gjk.getSimplex(c1, c2));
        assertNull(gjk.getSimplex(c1, c3));

        testPenetration(c1, c2, 0.100002f);
    }

    @Test
    public void getRectanglesSimplex() {
        var r1 = new Rectangle(new Vec2(2, 2), new Vec2(4, 4));
        var r2 = new Rectangle(new Vec2(5, 5), new Vec2(4, 4), 45);
        var r3 = new Rectangle(new Vec2(5.5f, 5.5f), new Vec2(4, 4), 45);

        assertNotNull(gjk.getSimplex(r1, r2));
        assertNull(gjk.getSimplex(r1, r3));

        testPenetration(r1, r2, 0.585788f);
    }

    @Test
    public void getEllipsesSimplex() {
        var e1 = new Ellipse(new Vec2(1.5f, 1), new Vec2(3, 2));
        var e2 = new Ellipse(new Vec2(3.5f, 2.5f), new Vec2(2, 3));
        var e3 = new Ellipse(new Vec2(4, 3), new Vec2(2, 3));

        assertNotNull(gjk.getSimplex(e1, e2));
        assertNull(gjk.getSimplex(e1, e3));

        testPenetration(e1, e2, 0.045203f);
    }

    // Source: https://dyn4j.org/2010/04/gjk-gilbert-johnson-keerthi/#gjk-iteration
    @Test
    public void getPolygonsSimplex() {
        var p1 = new Triangle(new Vec2(4, 5), new Vec2(9, 9), new Vec2(4, 11));
        var p2 = new Polygon(new Vec2(5, 7), new Vec2(7, 3), new Vec2(10, 2), new Vec2(12, 7));

        var simplex = gjk.getSimplex(p1, p2);
        assertNotNull(simplex);
        testPenetration(p1, p2, 0.937044f);

        var simplexPoly = simplex.toPolygon();
        assertVerticesEqual(simplexPoly.getVertices(), new Vec2[]{new Vec2(4, 2), new Vec2(-8, -2), new Vec2(-1, -2)});
        assertTrue(simplexPoly.contains(new Vec2(0, 0)));
    }

    private void testPenetration(Shape shape1, Shape shape2, float depth) {
        var pen = gjk.getPenetration(shape1, shape2);
        assertNotNull(pen);
        assertFloatEquals(depth, pen.getDepth());
    }

}
