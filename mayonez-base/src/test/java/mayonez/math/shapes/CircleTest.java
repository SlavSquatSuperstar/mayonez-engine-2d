package mayonez.math.shapes;

import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.math.shapes.Circle} class.
 *
 * @author SlavSquatSuperstar
 */
class CircleTest {

    private static Circle circle;

    @BeforeAll
    static void getCircle() {
        circle = new Circle(new Vec2(2, 2), 2); // center (2, 2), radius 2
    }

    // Circle Properties Tests

    @Test
    void areaCorrect() {
        assertEquals(4f * MathUtils.PI, circle.area(), MathUtils.FLOAT_EPSILON);
    }

    @Test
    void circumferenceCorrect() {
        assertEquals(4f * MathUtils.PI, circle.circumference(), MathUtils.FLOAT_EPSILON);
    }

    // Contains Point Tests

    @Test
    void interiorPointInCircle() {
        assertTrue(circle.contains(new Vec2(2, 2)));
        assertTrue(circle.contains(new Vec2(1, 3)));
    }

    @Test
    void boundaryPointInCircle() {
        assertTrue(circle.contains(new Vec2(2, 0)));
        assertTrue(circle.contains(new Vec2(4, 2)));
    }

    @Test
    void exteriorPointNotInCircle() {
        assertFalse(circle.contains(new Vec2(0, 0)));
        assertFalse(circle.contains(new Vec2(4, 4)));
    }

    @Test
    void circleTransformedProperly() {
        var newCircle = circle.scale(new Vec2(2, 1), null).translate(new Vec2(1, 1));
        assertEquals(new Vec2(3, 3), newCircle.center());
        assertEquals(circle.area() * 4f, newCircle.area());
    }

    // Support Point Tests

    @Test
    void circleSupportPointSuccess() {
        assertEquals(new Vec2(0, 2), circle.supportPoint(new Vec2(-1, 0)));
        assertEquals(new Vec2(2, 4), circle.supportPoint(new Vec2(0, 1)));
    }


}
