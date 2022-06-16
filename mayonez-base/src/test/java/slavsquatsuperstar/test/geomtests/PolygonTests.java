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
        assertEquals(poly.angMass(5), tri.angMass(5), EPSILON);

        Vec2 p1 = new Vec2(1, 1);
        assertTrue(poly.contains(p1) && tri.contains(p1));
    }

    @Test
    public void rectangleVsPolygon() {
        Polygon poly = new Polygon(new Vec2(0, 0), new Vec2(2, 0), new Vec2(2, 3), new Vec2(0, 3));
        Rectangle rect = new Rectangle(new Vec2(1, 1.5f), new Vec2(2, 3), 0);

        assertEquals(poly.area(), rect.area(), EPSILON);
        assertEquals(poly.perimeter(), rect.perimeter(), EPSILON);
        assertEquals(poly.center(), rect.center());
        assertEquals(poly.angMass(5), rect.angMass(5), EPSILON);

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
        assertEquals(poly.angMass(5), square.angMass(5), EPSILON);
    }

}
