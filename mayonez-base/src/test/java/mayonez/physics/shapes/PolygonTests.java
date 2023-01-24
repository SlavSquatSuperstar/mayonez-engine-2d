package mayonez.physics.shapes;

import org.junit.jupiter.api.Test;
import mayonez.math.Vec2;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static mayonez.test.TestUtils.assertFloatEquals;

/**
 * Unit tests for the {@link mayonez.physics.shapes.Polygon} class to make sure its subclasses behave like it.
 *
 * @author SlavSquatSuperstar
 */
public class PolygonTests {
    @Test
    public void triangleVsPolygon() {
        // 2x3 triangle, min corner at origin
        Vec2[] vertices = {new Vec2(0, 0), new Vec2(2, 3), new Vec2(3, 0)};
        var poly = new Polygon(vertices);
        var tri = new Triangle(vertices);

        assertFloatEquals(poly.area(), tri.area());
        assertEquals(poly.center(), tri.center());
        assertFloatEquals(poly.angularMass(5), tri.angularMass(5));

        var p1 = new Vec2(1, 1);
        assertTrue(poly.contains(p1) && tri.contains(p1));
    }

    @Test
    public void rectangleTemplateCorrect() {
        // 2x3 rectangle, min corner at origin
        var poly = new Rectangle(new Vec2(1, 1.5f), new Vec2(2, 3));
        var rect = new Rectangle(new Vec2(1, 1.5f), new Vec2(2, 3));

        assertFloatEquals(poly.area(), 6f);
        assertFloatEquals(poly.area(), rect.area());
        assertEquals(poly.center(), rect.center());
        assertFloatEquals(poly.angularMass(5), rect.angularMass(5));

        var p1 = new Vec2(1, 1);
        assertTrue(poly.contains(p1) && rect.contains(p1));
    }

    // From https://en.wikipedia.org/wiki/Shoelace_formula#Example
    @Test
    public void polygonAreaCorrect() {
        var penta = new Polygon(false, new Vec2(1, 6), new Vec2(3, 1), new Vec2(7, 2),
                new Vec2(4, 4), new Vec2(8, 5));
        assertFloatEquals(16.5f, penta.area());
    }

    @Test
    public void polygonSupportPointSuccess() {
        var rect = new Rectangle(new Vec2(2, 2), new Vec2(4, 4)); // 4x4 var at (0, 0)
        assertEquals(new Vec2(0, 0), rect.supportPoint(new Vec2(-1, -1)));
        assertEquals(new Vec2(4, 4), rect.supportPoint(new Vec2(1, 2)));
    }

    @Test
    public void polygonNormalsPointOutward() {
        var rect = new Rectangle(new Vec2(0, 0), new Vec2(2, 2));
        var normals = new Vec2[]{new Vec2(0, -1), new Vec2(1, 0), new Vec2(0, 1), new Vec2(-1, 0)};
        assertTrue(Objects.deepEquals(normals, rect.getNormals()));
    }

}
