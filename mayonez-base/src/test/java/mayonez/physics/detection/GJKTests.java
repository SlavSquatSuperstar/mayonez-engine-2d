package mayonez.physics.detection;

import org.junit.jupiter.api.Test;
import mayonez.math.Vec2;
import mayonez.test.TestUtils;
import mayonez.physics.shapes.*;

import static org.junit.jupiter.api.Assertions.*;
import static mayonez.test.TestUtils.assertFloatEquals;
import static mayonez.test.TestUtils.assertVerticesEqual;

/**
 * Unit Tests for the {@link mayonez.physics.detection.GJKDetector} class.
 *
 * @author SlavSquatSuperstar
 */
public class GJKTests {

    // Simplex Tests
    @Test
    public void getCirclesSimplex() {
        var c1 = new Circle(new Vec2(0, 0), 5);
        var c2 = new Circle(new Vec2(9.9f, 0), 5);
        var c3 = new Circle(new Vec2(10.1f, 0), 5);
        assertNotNull(new GJKDetector().getSimplex(c1, c2));
        assertFloatEquals(0.100002f, new GJKDetector().getPenetration(c1, c2).getDepth());
        assertNull(new GJKDetector().getSimplex(c1, c3));
    }

    @Test
    public void getRectanglesSimplex() {
        var r1 = new Rectangle(new Vec2(2, 2), new Vec2(4, 4));
        var r2 = new Rectangle(new Vec2(5, 5), new Vec2(4, 4), 45);
        var r3 = new Rectangle(new Vec2(5.5f, 5.5f), new Vec2(4 ,4), 45);
        assertNotNull(new GJKDetector().getSimplex(r1, r2));
        assertFloatEquals(0.585788f, new GJKDetector().getPenetration(r1, r2).getDepth());
        assertNull(new GJKDetector().getSimplex(r1, r3));
    }

    @Test
    public void getEllipsesSimplex() {
        var e1 = new Ellipse(new Vec2(1.5f, 1), new Vec2(3, 2));
        var e2 = new Ellipse(new Vec2(3.5f, 2.5f), new Vec2(2, 3));
        var e3 = new Ellipse(new Vec2(4, 3), new Vec2(2, 3));
        assertNotNull(new GJKDetector().getSimplex(e1, e2));
        assertFloatEquals(0.045203f, new GJKDetector().getPenetration(e1, e2).getDepth());
        assertNull(new GJKDetector().getSimplex(e1, e3));
    }

    // Source: https://dyn4j.org/2010/04/gjk-gilbert-johnson-keerthi/#gjk-iteration
    @Test
    public void getPolygonsSimplex() {
        var p1 = new Triangle(new Vec2(4, 5), new Vec2(9, 9), new Vec2(4, 11));
        var p2 = new Polygon(new Vec2(5, 7), new Vec2(7, 3), new Vec2(10, 2), new Vec2(12, 7));
        TestUtils.assertShapeCollision(p1, p2);
        assertFloatEquals(0.937044f, new GJKDetector().getPenetration(p1, p2).getDepth());

        var simplex = new GJKDetector().getSimplex(p1, p2).toPolygon();
        assertVerticesEqual(simplex.getVertices(), new Vec2[]{new Vec2(4, 2), new Vec2(-8, -2), new Vec2(-1, -2)});
        assertTrue(simplex.contains(new Vec2(0, 0)));
    }

}
