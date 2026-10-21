package mayonez.math.shapes;

import mayonez.Transform;
import mayonez.math.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static mayonez.math.MathTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.shapes.Polygon} class to make sure its subclasses behave like it.
 *
 * @author SlavSquatSuperstar
 */
class PolygonTest {

    private static Polygon penta;

    @BeforeAll
    static void createPolygon() {
        penta = new Polygon(new Vec2(1, 5), new Vec2(3, 1),
                new Vec2(7, 2), new Vec2(8, 5), new Vec2(4, 7));
    }

    // Vertices Tests

    @Test
    void polygonVerticesMinimumCountIs3() {
        for (var sides = -1; sides < 3; sides++) {
            var vertices = Polygon.regularPolygonVertices(new Vec2(), sides, 1f);
            assertEquals(3, vertices.length);
        }
    }

    @Test
    void polygonVerticesHasCorrectCount() {
        for (var sides = 3; sides < 10; sides++) {
            var vertices = Polygon.regularPolygonVertices(new Vec2(), sides, 1f);
            assertEquals(sides, vertices.length);
        }
    }

    // Subclass Tests

    @Test
    void triangleVsPolygon() {
        // 2x3 triangle, min corner at origin
        Vec2[] vertices = {new Vec2(0, 0), new Vec2(2, 3), new Vec2(3, 0)};
        var poly = new Polygon(vertices);
        var tri = new Triangle(vertices[0], vertices[1], vertices[2]);

        assertFloatEquals(poly.area(), tri.area());
        assertEquals(poly.center(), tri.center());
        assertFloatEquals(poly.angularMass(5), tri.angularMass(5));

        var p1 = new Vec2(1, 1);
        assertTrue(poly.contains(p1) && tri.contains(p1));
    }

    @Test
    void rectangleVsPolygon() {
        // 2x3 rectangle, min corner at origin
        Vec2[] vertices = {new Vec2(0, 0), new Vec2(2, 0), new Vec2(2, 3), new Vec2(0, 3)};
        var poly = new Polygon(vertices);
        var rect = new Rectangle(new Vec2(1, 1.5f), new Vec2(2, 3));

        assertFloatEquals(poly.area(), 6f);
        assertFloatEquals(poly.area(), rect.area());
        assertEquals(poly.center(), rect.center());
        assertFloatEquals(poly.angularMass(5), rect.angularMass(5));

        var p1 = new Vec2(1, 1);
        assertTrue(poly.contains(p1));
        assertTrue(rect.contains(p1));
    }

    // Contains Point Tests

    @Test
    void polygonContainsInteriorPoints() {
        assertTrue(penta.contains(new Vec2(2, 4)));
        assertTrue(penta.contains(new Vec2(5, 3)));
    }

    @Test
    void polygonContainsBoundaryPoints() {
        assertTrue(penta.contains(new Vec2(2.5f, 6)));
        assertTrue(penta.contains(new Vec2(8, 5))); // vertex
    }

    @Test
    void polygonDoesNotContainsExteriorPoints() {
        assertFalse(penta.contains(new Vec2(2, 0)));
        assertFalse(penta.contains(new Vec2(2, 6)));
        assertFalse(penta.contains(new Vec2(0, 4.5f)));
    }

    // Property Tests

    // From https://en.wikipedia.org/wiki/Shoelace_formula#Example
    @Test
    void polygonAreaCorrect() {
        var penta = new Polygon(false, new Vec2(1, 6), new Vec2(3, 1),
                new Vec2(7, 2), new Vec2(4, 4), new Vec2(8, 5));
        assertFloatEquals(16.5f, penta.area());
    }

    @Test
    void polygonSupportPointSuccess() {
        var rect = Rectangle.fromMinAndMax(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 var at (0, 0)
        assertEquals(new Vec2(0, 0), rect.supportPoint(new Vec2(-1, -1)));
        assertEquals(new Vec2(4, 4), rect.supportPoint(new Vec2(1, 2)));
    }

    @Test
    void polygonNormalsPointOutward() {
        var rect = new Rectangle(new Vec2(0, 0), new Vec2(2, 2));
        var normals = new Vec2[]{new Vec2(0, -1), new Vec2(1, 0), new Vec2(0, 1), new Vec2(-1, 0)};
        assertTrue(Objects.deepEquals(normals, rect.getNormals()));
    }

    // Transform Tests

    @Test
    void polygonTransformedProperly() {
        var xf = new Transform(new Vec2(-2f, 10f), 50f, new Vec2(6f, 4f));
        var transformed = (Polygon) penta.transform(xf, null);
        assertEquals(penta.area() * xf.getDeterminant(), transformed.area(), 1e-4);
        var transformedCenter = penta.center().add(xf.getPosition());
        assertEquals(transformedCenter.x, transformed.center().x, 1e-4);
        assertEquals(transformedCenter.y, transformed.center().y, 1e-4);
    }

}
