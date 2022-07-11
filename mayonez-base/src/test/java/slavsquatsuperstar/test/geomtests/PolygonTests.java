package slavsquatsuperstar.test.geomtests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.math.geom.Polygon;
import slavsquatsuperstar.math.geom.Rectangle;
import slavsquatsuperstar.math.geom.Square;
import slavsquatsuperstar.math.geom.Triangle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static slavsquatsuperstar.math.MathUtils.EPSILON;

/**
 * Unit tests for {@link Polygon} class to make sure its subclasses behave like it.
 *
 * @author SlavSquatSuperstar
 */
public class PolygonTests {
    @Test
    public void triangleVsPolygon() {
        Vec2[] verts = {new Vec2(0, 0), new Vec2(2, 3), new Vec2(3, 0)};
        Polygon poly = new Polygon(verts);
        Triangle tri = new Triangle(verts);

        assertEquals(poly.area(), tri.area(), EPSILON);
        assertEquals(poly.perimeter(), tri.perimeter(), EPSILON);
        assertEquals(poly.center(), tri.center());
        assertEquals(poly.angularMass(5), tri.angularMass(5), EPSILON);

        Vec2 p1 = new Vec2(1, 1);
        assertTrue(poly.contains(p1) && tri.contains(p1));
    }

    @Test
    public void rectangleVsPolygon() {
        Polygon poly = new Polygon(new Vec2(0, 0), new Vec2(2, 0), new Vec2(2, 3), new Vec2(0, 3));
        Rectangle rect = new Rectangle(new Vec2(1, 1.5f), new Vec2(2, 3));

        assertEquals(poly.area(), rect.area(), EPSILON);
        assertEquals(poly.perimeter(), rect.perimeter(), EPSILON);
        assertEquals(poly.center(), rect.center());
        assertEquals(poly.angularMass(5), rect.angularMass(5), EPSILON);

        Vec2 p1 = new Vec2(1, 1);
        assertTrue(poly.contains(p1) && rect.contains(p1));
    }

    @Test
    public void polygonVsSquare() {
        Polygon poly = new Polygon(new Vec2(1, 1), 4, 2 * MathUtils.sqrt(2));
        Square square = new Square(new Vec2(1, 1), 4, 0);

        assertEquals(poly.area(), square.area(), EPSILON);
        assertEquals(poly.perimeter(), square.perimeter(), EPSILON);
        assertEquals(poly.center(), square.center());
        assertEquals(poly.angularMass(5), square.angularMass(5), EPSILON);
    }

    // From https://en.wikipedia.org/wiki/Shoelace_formula#Example
    @Test
    public void polygonAreaCorrect() {
        Polygon penta = new Polygon(new Vec2(1, 6), new Vec2(3, 1), new Vec2(7, 2),
                new Vec2(4, 4), new Vec2(8, 5));
        assertEquals(16.5f, penta.area(), EPSILON);
    }

}
