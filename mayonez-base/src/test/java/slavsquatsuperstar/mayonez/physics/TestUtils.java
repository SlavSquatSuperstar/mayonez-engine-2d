package slavsquatsuperstar.mayonez.physics;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.mayonez.physics.collision.Collisions;
import slavsquatsuperstar.mayonez.physics.shapes.Shape;

import static org.junit.jupiter.api.Assertions.*;

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
