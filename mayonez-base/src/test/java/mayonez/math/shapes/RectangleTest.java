package mayonez.math.shapes;

import mayonez.Transform;
import mayonez.math.*;
import org.junit.jupiter.api.*;

import static mayonez.math.MathTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.shapes.Rectangle} class.
 *
 * @author SlavSquatSuperstar
 */
class RectangleTest {

    private static Rectangle rect;

    @BeforeAll
    static void getRect() {
        // 4x3 rectangle, min corner at origin
        rect = Rectangle.fromMinAndMax(new Vec2(0, 0), new Vec2(4, 3));
    }

    // Property Tests

    @Test
    void verticesCorrect() {
        var verts = rect.getVertices();
        assertEquals(4, verts.length);
        assertEquals(new Vec2(0, 0), verts[0]);
        assertEquals(new Vec2(4, 0), verts[1]);
        assertEquals(new Vec2(4, 3), verts[2]);
        assertEquals(new Vec2(0, 3), verts[3]);
    }

    @Test
    void areaCorrect() {
        assertFloatEquals(12f, rect.area());
    }

    // Contains Point Tests

    @Test
    void interiorPointInRect() {
        assertTrue(rect.contains(new Vec2(2, 2)));
        assertTrue(rect.contains(new Vec2(3.9f, 2.9f)));
    }

    @Test
    void boundaryPointInRect() {
        assertTrue(rect.contains(new Vec2(0, 1)));
        assertTrue(rect.contains(new Vec2(1, 3)));
        assertTrue(rect.contains(new Vec2(2, 1)));
    }

    @Test
    void exteriorPointNotInRect() {
        assertFalse(rect.contains(new Vec2(1, 5)));
        assertFalse(rect.contains(new Vec2(-1, 2)));
    }

    // Transform Tests

    @Test
    void rectScaledProperly() {
        var xf1 = rect.scale(new Vec2(2, 2), rect.center()).translate(new Vec2(-1, 0.5f));
        assertEquals(new Vec2(1, 2), xf1.center());
        var xf2 = rect.scale(new Vec2(3, 4), new Vec2(0));
        assertEquals(new Vec2(6, 6), xf2.center());
        assertEquals(rect.area() * 12, xf2.area());
    }

    @Test
    void rectRotatedProperly() {
        var angle = 45f;
        var rotated1 = new Rectangle(new Vec2(2, 1.5f), new Vec2(4, 3)).rotate(angle, null);
        var rotated2 = rect.rotate(angle, null);
        assertEquals(rotated1.center(), rotated2.center());
        assertEquals(rotated1.area(), rotated2.area());
    }

    @Test
    void rectTransformedProperly() {
        var xf = new Transform(new Vec2(-2f, 10f), 50f, new Vec2(6f, 4f));
        var transformed = (Rectangle) rect.transform(xf, null);
        assertEquals(rect.area() * xf.getDeterminant(), transformed.area(), 1e-4);
        assertEquals(rect.getAngle() + xf.getRotation(), transformed.getAngle());
        assertEquals(rect.center().add(xf.getPosition()), transformed.center());
    }

}
