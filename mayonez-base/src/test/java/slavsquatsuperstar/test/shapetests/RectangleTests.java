package slavsquatsuperstar.test.shapetests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.shapes.BoundingRectangle;

import static org.junit.jupiter.api.Assertions.*;
import static slavsquatsuperstar.math.MathUtils.EPSILON;

/**
 * Unit tests for {@link BoundingRectangle} class.
 *
 * @author SlavSquatSuperstar
 */
public class RectangleTests {

    private static BoundingRectangle rect;

    @BeforeAll
    public static void getRect() {
        rect = new BoundingRectangle(new Vec2(3, 1.5f), new Vec2(4, 3)); // center (3, 1.5f), size(4, 3), no rotation
        // vertices (1, 1), (5, 1), (1, 3), (5, 3)
    }

    @Test
    public void areaCorrect() {
        assertEquals(rect.area(), rect.width * rect.height, EPSILON);
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
    public void rectTransformedProperly() {
        BoundingRectangle transformed = rect.scale(new Vec2(2, 2), null).translate(new Vec2(-1, 0.5f)).rotate(45, null);
        assertEquals(transformed.center(), new Vec2(2, 2));
//        assertEquals(rect.area() * 4f, transformed.area()); // TODO doesn't do this anymore, use template
    }

    @Test
    public void rectNonLinearTransformedProperly() {
        BoundingRectangle transformed = rect.scale(new Vec2(3, 4), null);
        assertEquals(rect.center(), transformed.center());
        assertEquals(rect.area() * 12, transformed.area());
    }

}
