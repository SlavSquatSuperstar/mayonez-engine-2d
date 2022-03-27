package slavsquatsuperstar.test.geomtests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.math.geom.Rectangle;
import slavsquatsuperstar.math.geom.Square;

import static org.junit.jupiter.api.Assertions.*;
import static slavsquatsuperstar.math.MathUtils.EPSILON;

/**
 * Unit tests for {@link Square} class to ensure it functions like its superclass {@link Rectangle}.
 *
 * @author SlavSquatSuperstar
 */
public class SquareTests {

    private static Rectangle rect;
    private static Square square;

    @BeforeAll
    public static void getShapes() {
        // min: (-1, -1), max: (3, 3)
        rect = new Rectangle(new Vec2(1, 1), 4, 4);
        square = new Square(new Vec2(1, 1), 4);
    }

    @Test
    public void squareVsRectangleProperties() {
        assertEquals(rect.area(), square.area(), EPSILON);
        assertEquals(rect.perimeter(), square.perimeter(), EPSILON);
        assertEquals(rect.center(), square.center());
        assertEquals(rect.angMass(5), square.angMass(5), EPSILON);
    }

    @Test
    public void squareVsRectangleContains() {
        Vec2 intPt = new Vec2(0.5f, 0.5f);
        assertTrue(rect.contains(intPt) && square.contains(intPt));

        Vec2 bdryPt = new Vec2(3, -1);
        assertTrue(rect.contains(bdryPt) && square.contains(bdryPt));

        Vec2 extPt = new Vec2(5, 5);
        assertTrue(!rect.contains(extPt) && !square.contains(extPt));}

}
