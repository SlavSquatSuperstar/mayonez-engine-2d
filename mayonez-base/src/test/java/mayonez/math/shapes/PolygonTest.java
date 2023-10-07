package mayonez.math.shapes;

import mayonez.math.*;
import org.junit.jupiter.api.*;

import static mayonez.test.TestUtils.*;
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

    // Vertices

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

    // Subclasses

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

    // Polygon vs Point
    @Test
    void polygonContainsInteriorPoints() {
        assertTrue(penta.contains(new Vec2(2, 4)));
        assertTrue(penta.contains(new Vec2(5,3)));
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

    // Properties

    // From https://en.wikipedia.org/wiki/Shoelace_formula#Example
    @Test
    void polygonAreaCorrect() {
        var penta = new Polygon(false, new Vec2(1, 6), new Vec2(3, 1),
                new Vec2(7, 2), new Vec2(4, 4), new Vec2(8, 5));
        assertFloatEquals(16.5f, penta.area());
    }

    @Test
    void polygonSupportPointSuccess() {
        var rect = new Rectangle(new Vec2(2, 2), new Vec2(4, 4)); // 4x4 var at (0, 0)
        assertEquals(new Vec2(0, 0), rect.supportPoint(new Vec2(-1, -1)));
        assertEquals(new Vec2(4, 4), rect.supportPoint(new Vec2(1, 2)));
    }

}
