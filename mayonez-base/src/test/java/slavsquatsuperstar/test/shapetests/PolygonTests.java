package slavsquatsuperstar.test.shapetests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.shapes.BoundingRectangle;
import slavsquatsuperstar.mayonez.physics.shapes.Polygon;
import slavsquatsuperstar.mayonez.physics.shapes.Triangle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static slavsquatsuperstar.test.TestUtils.assertFloatsEqual;

/**
 * Unit tests for {@link Polygon} class to make sure its subclasses behave like it.
 *
 * @author SlavSquatSuperstar
 */
public class PolygonTests {
    @Test
    public void triangleVsPolygon() {
        // 2x3 triangle, min corner at origin
        Vec2[] vertices = {new Vec2(0, 0), new Vec2(2, 3), new Vec2(3, 0)};
        Polygon poly = new Polygon(vertices);
        Triangle tri = new Triangle(vertices);

        assertFloatsEqual(poly.area(), tri.area());
        assertEquals(poly.center(), tri.center());
        assertFloatsEqual(poly.angularMass(5), tri.angularMass(5));

        Vec2 p1 = new Vec2(1, 1);
        assertTrue(poly.contains(p1) && tri.contains(p1));
    }

    @Test
    public void rectangleTemplateCorrect() {
        // 2x3 rectangle, min corner at origin
        Polygon poly = Polygon.rectangle(new Vec2(1, 1.5f), new Vec2(2, 3));
        BoundingRectangle rect = new BoundingRectangle(new Vec2(1, 1.5f), new Vec2(2, 3));

        assertFloatsEqual(poly.area(), 6f);
        assertFloatsEqual(poly.area(), rect.area());
        assertEquals(poly.center(), rect.center());
        assertFloatsEqual(poly.angularMass(5), rect.angularMass(5));

        Vec2 p1 = new Vec2(1, 1);
        assertTrue(poly.contains(p1) && rect.contains(p1));
    }

    // From https://en.wikipedia.org/wiki/Shoelace_formula#Example
    @Test
    public void polygonAreaCorrect() {
        Polygon penta = new Polygon(new Vec2(1, 6), new Vec2(3, 1), new Vec2(7, 2),
                new Vec2(4, 4), new Vec2(8, 5));
        assertFloatsEqual(16.5f, penta.area());
    }

}
