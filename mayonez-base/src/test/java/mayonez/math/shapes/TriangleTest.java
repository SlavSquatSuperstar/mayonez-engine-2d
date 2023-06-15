package mayonez.math.shapes;

import mayonez.math.*;
import org.junit.jupiter.api.*;

import static mayonez.test.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.shapes.Triangle} class.
 *
 * @author SlavSquatSuperstar
 */
class TriangleTest {

    private static Triangle tri;

    @BeforeAll
    static void getTri() {
        // center at (3, 1.5f), 4x3 size
        tri = new Triangle(new Vec2(-1, -1), new Vec2(0, 4), new Vec2(5, -1));
    }

    @Test
    void verticesCorrect() {
        var verts = tri.getVertices();
        assertEquals(3, verts.length);
        assertEquals(new Vec2(-1, -1), verts[0]);
        assertEquals(new Vec2(5, -1), verts[1]);
        assertEquals(new Vec2(0, 4), verts[2]);
    }

    @Test
    void areaCorrect() {
        assertFloatEquals(tri.area(), 0.5f * tri.base * tri.height);
        var tri2 = new Triangle(new Vec2(-1, 0), new Vec2(1, 4), new Vec2(5, -2));
        assertFloatEquals(tri2.area(), 0.5f * tri2.base * tri2.height);
    }

    @Test
    void interiorPointInTri() {
        assertTrue(tri.contains(new Vec2(0, 0)));
        assertTrue(tri.contains(new Vec2(0, 3)));
        assertTrue(tri.contains(new Vec2(3.5f, -0.5f)));
    }

    @Test
    void boundaryPointInTri() {
        assertTrue(tri.contains(new Vec2(-1, -1)));
        assertTrue(tri.contains(new Vec2(3, -1)));
    }

    @Test
    void exteriorPointNotInTri() {
        assertFalse(tri.contains(new Vec2(-1, 3)));
        assertFalse(tri.contains(new Vec2(0, -2)));
        assertFalse(tri.contains(new Vec2(2, 3)));
    }

}
