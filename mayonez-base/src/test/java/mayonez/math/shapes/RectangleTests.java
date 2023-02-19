package mayonez.math.shapes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import mayonez.math.Vec2;

import static org.junit.jupiter.api.Assertions.*;
import static mayonez.test.TestUtils.assertFloatEquals;

/**
 * Unit tests for the {@link mayonez.math.shapes.Rectangle} class.
 *
 * @author SlavSquatSuperstar
 */
public class RectangleTests {

    private static Rectangle rect;

    @BeforeAll
    public static void getRect() {
        // 4x3 rectangle, min corner at origin
        rect = new Rectangle(new Vec2(2, 1.5f), new Vec2(4, 3));
    }

    @Test
    public void verticesCorrect() {
        var verts = rect.getVertices();
        assertEquals(4, verts.length);
        assertEquals(new Vec2(0, 0), verts[0]);
        assertEquals(new Vec2(4, 0), verts[1]);
        assertEquals(new Vec2(4, 3), verts[2]);
        assertEquals(new Vec2(0, 3), verts[3]);
    }

    @Test
    public void areaCorrect() {
        assertFloatEquals(12f, rect.area());
    }

    @Test
    public void interiorPointInRect() {
        assertTrue(rect.contains(new Vec2(2, 2)));
        assertTrue(rect.contains(new Vec2(3.9f, 2.9f)));
    }

    @Test
    public void boundaryPointInRect() {
        assertTrue(rect.contains(new Vec2(0, 1)));
        assertTrue(rect.contains(new Vec2(1, 3)));
        assertTrue(rect.contains(new Vec2(2, 1)));
    }

    @Test
    public void exteriorPointNotInRect() {
        assertFalse(rect.contains(new Vec2(1, 5)));
        assertFalse(rect.contains(new Vec2(-1, 2)));
    }

    @Test
    public void rectScaledProperly() {
        var xf1 = rect.scale(new Vec2(2, 2), rect.center()).translate(new Vec2(-1, 0.5f));
        assertEquals(new Vec2(1, 2), xf1.center());
        var xf2 = rect.scale(new Vec2(3, 4), new Vec2(0));
        assertEquals(new Vec2(6, 6), xf2.center());
        assertEquals(rect.area() * 12, xf2.area());
    }

//    @Test
//    public void rectRotatedProperly() {
//        var angle = 45f;
//        var poly = new Rectangle(new Vec2(3, 1.5f), new Vec2(4, 3)).rotate(angle, null);
//        var rot = rect.rotate(angle, null);
//        assertEquals(rot.center(), poly.center());
//        assertEquals(poly.boundingRectangle(), rot);
//    }

}
