package mayonez.test;

import mayonez.math.FloatMath;
import mayonez.math.Vec2;
import mayonez.physics.Collisions;
import mayonez.physics.shapes.Shape;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A class providing utility methods for unit tests.
 *
 * @author SlavSquatSuperstar
 */
public final class TestUtils {

    private TestUtils() {}

    public static final float EPSILON = FloatMath.FLOAT_EPSILON;

    /**
     * Asserts that two floats are roughly equivalent.
     *
     * @param expected the correct float value
     * @param actual   the given float value
     */
    public static void assertFloatEquals(float expected, float actual) {
        assertEquals(expected, actual, EPSILON);
    }

    /**
     * Asserts that two sets of vertices are equivalent. Sometimes two arrays may store the same set of vertices
     * but in a different order.
     *
     * @param verts1 the first set of vertices
     * @param verts2 the second set of vertices
     */
    public static void assertVerticesEqual(Vec2[] verts1, Vec2[] verts2) {
        // Sort vectors by x, then y
        Arrays.sort(verts1, Comparator.comparing(v -> v.y));
        Arrays.sort(verts1, Comparator.comparing(v -> v.x));
        Arrays.sort(verts2, Comparator.comparing(v -> v.y));
        Arrays.sort(verts2, Comparator.comparing(v -> v.x));
        assertArrayEquals(verts1, verts2);
    }

    /**
     * Asserts that two shapes intersect or touch each other.
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     */
    public static void assertShapeCollision(Shape shape1, Shape shape2) {
        assertTrue(Collisions.checkCollision(shape1, shape2));
    }

    /**
     * Asserts that two shapes do not intersect or touch each other.
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     */
    public static void assertNoShapeCollision(Shape shape1, Shape shape2) {
        assertFalse(Collisions.checkCollision(shape1, shape2));
    }
}
