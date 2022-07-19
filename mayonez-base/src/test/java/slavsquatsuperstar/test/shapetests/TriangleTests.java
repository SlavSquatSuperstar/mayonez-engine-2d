package slavsquatsuperstar.test.shapetests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.shapes.Triangle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Triangle} class.
 *
 * @author SlavSquatSuperstar
 */
public class TriangleTests {

    private static Triangle tri;

    @BeforeAll
    public static void getTri() {
        tri = new Triangle(new Vec2(-1, -1), new Vec2(0, 4), new Vec2(5, -1)); // center (3, 1.5f), size(4, 3)
    }

    @Test
    public void areaCorrect() {
        assertEquals(tri.area(), 0.5f * tri.base * tri.height, MathUtils.EPSILON);
        Triangle tri2 = new Triangle(new Vec2(-1, 0), new Vec2(1, 4), new Vec2(5, -2));
        assertEquals(tri2.area(), 0.5f * tri2.base * tri2.height, MathUtils.EPSILON);
    }
    
    @Test
    public void interiorPointInTri() {
        assertTrue(tri.contains(new Vec2(0, 0)));
        assertTrue(tri.contains(new Vec2(0, 3)));
        assertTrue(tri.contains(new Vec2(3.5f, -0.5f)));
    }

    @Test
    public void boundaryPointInTri() {
        assertTrue(tri.contains(new Vec2(-1, -1)));
        assertTrue(tri.contains(new Vec2(3, -1)));
    }

    @Test
    public void exteriorPointNotInTri() {
        assertFalse(tri.contains(new Vec2(-1, 3)));
        assertFalse(tri.contains(new Vec2(0, -2)));
        assertFalse(tri.contains(new Vec2(2, 3)));
    }

}
