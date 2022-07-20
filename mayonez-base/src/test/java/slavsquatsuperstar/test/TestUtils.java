package slavsquatsuperstar.test;

import slavsquatsuperstar.math.MathUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A class providing utility methods for unit tests.
 *
 * @author SlavSquatSuperstar
 */
public final class TestUtils {

    private TestUtils() {
    }

    public static final float EPSILON = MathUtils.FLOAT_EPSILON;

    /**
     * Asserts that two floats are roughly equivalent.
     *
     * @param expected the correct float value
     * @param actual   the given float value
     */
    public static void assertFloatEquals(float expected, float actual) {
        assertEquals(expected, actual, EPSILON);
    }
}
