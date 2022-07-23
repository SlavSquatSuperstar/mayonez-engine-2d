package slavsquatsuperstar.test.shapetests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.shapes.BoundingRectangle;

import static org.junit.jupiter.api.Assertions.*;
import static slavsquatsuperstar.test.TestUtils.assertFloatsEqual;

/**
 * Unit tests for {@link BoundingRectangle} class.
 *
 * @author SlavSquatSuperstar
 */
public class RectangleTests {

    private static BoundingRectangle rect;

    @BeforeAll
    public static void getRect() {
        // 4x3 rectangle, min corner at origin
        rect = new BoundingRectangle(new Vec2(3, 1.5f), new Vec2(4, 3));
        // vertices (1, 1), (5, 1), (1, 3), (5, 3)
    }

    @Test
    public void areaCorrect() {
        assertFloatsEqual(rect.area(), rect.width * rect.height);
    }

    @Test
    public void interiorPointInRect() {
        assertTrue(rect.contains(new Vec2(2, 2)));
        assertTrue(rect.contains(new Vec2(4.9f, 2.9f)));
    }

    @Test
    public void boundaryPointInRect() {
        assertTrue(rect.contains(new Vec2(1, 1)));
        assertTrue(rect.contains(new Vec2(2, 3)));
        assertTrue(rect.contains(new Vec2(2, 1)));
    }

    @Test
    public void exteriorPointNotInRect() {
        assertFalse(rect.contains(new Vec2(0, 5)));
        assertFalse(rect.contains(new Vec2(-1, 2)));
    }


    @Test
    public void rectScaledProperly() {
        BoundingRectangle xf1 = rect.scale(new Vec2(2, 2), null).translate(new Vec2(-1, 0.5f));
        assertEquals(xf1.center(), new Vec2(2, 2));
        BoundingRectangle xf2 = rect.scale(new Vec2(3, 4), null);
        assertEquals(rect.center(), xf2.center());
        assertEquals(rect.area() * 12, xf2.area());
    }

//    @Test
//    public void rectRotatedProperly() {
//        float angle = 45f;
//        Polygon poly = Polygon.rectangle(new Vec2(3, 1.5f), new Vec2(4, 3)).rotate(angle, null);
//        BoundingRectangle rot = rect.rotate(angle, null);
//        assertEquals(rot.center(), poly.center());
//        assertEquals(poly.boundingRectangle(), rot);
//    }

}
