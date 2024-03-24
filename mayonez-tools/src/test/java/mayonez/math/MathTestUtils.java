package mayonez.math;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A class providing utility methods for unit tests.
 *
 * @author SlavSquatSuperstar
 */
public final class MathTestUtils {

    private MathTestUtils() {}

    /**
     * Asserts that two floats are roughly equivalent.
     *
     * @param expected the correct float value
     * @param actual   the given float value
     */
    public static void assertFloatEquals(float expected, float actual) {
        assertEquals(expected, actual, FloatMath.FLOAT_EPSILON);
    }

    /**
     * Asserts that two sets of vertices are equivalent after being sorted.
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

}
