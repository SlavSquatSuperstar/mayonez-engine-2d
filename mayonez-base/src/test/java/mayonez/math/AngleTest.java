package mayonez.math;

import org.junit.jupiter.api.*;

import static mayonez.math.MathUtils.PI;
import static mayonez.math.MathTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the {@link mayonez.math.Angle} class.
 *
 * @author SlavSquatSuperstar
 */
class AngleTest {

    private static final float HALF_SQRT_2 = MathUtils.SQRT_2 / 2f; // √2/2 = 1/√2
    private static final float HALF_SQRT_3 = MathUtils.SQRT_3 / 2f; // √3/2

    // Angle Factory Tests

    @Test
    void degreesConstructorCorrect() {
        var ang1 = Angle.createDegrees(45f); // 45º = π/4
        assertFloatEquals(HALF_SQRT_2, ang1.getSin());
        assertFloatEquals(HALF_SQRT_2, ang1.getCos());

        var ang2 = Angle.createDegrees(30f); // 30º = π/6
        assertFloatEquals(0.5f, ang2.getSin()); // sin(30º) = 1/2
        assertFloatEquals(HALF_SQRT_3, ang2.getCos()); // cos(30º) = √3/2
    }

    @Test
    void radiansConstructorCorrect() {
        var ang1 = Angle.createRadians(PI / 4); // π/4 = 45º
        assertFloatEquals(HALF_SQRT_2, ang1.getSin());
        assertFloatEquals(HALF_SQRT_2, ang1.getCos());

        var ang2 = Angle.createRadians(PI / 3f); // π/3 = 60º
        assertFloatEquals(HALF_SQRT_3, ang2.getSin()); // sin(60º) = √3/2
        assertFloatEquals(0.5f, ang2.getCos()); // cos(60º) = 1/2
    }

    // Angle Equals Tests

    @Test
    void angleEqualsCorrect() {
        var ang1 = Angle.createDegrees(90f);
        var ang2 = Angle.createDegrees(90f);
        assertEquals(ang1, ang2);
    }

    @Test
    void angleNotEqualsCorrect() {
        var ang1 = Angle.createDegrees(90f);
        var ang2 = Angle.createDegrees(450f);
        assertNotEquals(ang1, ang2);
    }

    // Angle Operator Tests

    @Test
    void angleAdditionCorrect() {
        var ang1 = Angle.createDegrees(90f); // 90º = π/2
        var ang2 = Angle.createDegrees(30f); // 30º = π/6
        var ang3 = ang1.add(ang2); // 120º = 2π/3

        assertFloatEquals(120f, ang3.getDegrees());
        assertFloatEquals(PI * 2f / 3f, ang3.getRadians());
        assertFloatEquals(HALF_SQRT_3, ang3.getSin()); // sin(120º) = √3/2
        assertFloatEquals(-0.5f, ang3.getCos()); // cos(120º) = -1/2
    }

    @Test
    void angleAdditionCorrectRandom() {
        var deg1 = randomAngle();
        var deg2 = randomAngle();
        var sumDeg = deg1 + deg2;
        var sum = Angle.createDegrees(deg1).add(Angle.createDegrees(deg2));
        assertEquals(Angle.createDegrees(sumDeg), sum);
    }

    @Test
    void angleSubtractionCorrect() {
        var ang1 = Angle.createDegrees(90f); // 90º = π/2
        var ang2 = Angle.createDegrees(30f); // 30º = π/6
        var ang3 = ang1.sub(ang2); // 60 = π/3

        assertFloatEquals(60f, ang3.getDegrees());
        assertFloatEquals(PI / 3f, ang3.getRadians());
        assertFloatEquals(HALF_SQRT_3, ang3.getSin()); // sin(60) = √3/2
        assertFloatEquals(0.5f, ang3.getCos()); // cos(60) = 1/2
    }

    @Test
    void angleSubtractionCorrectRandom() {
        var deg1 = randomAngle();
        var deg2 = randomAngle();
        var diffDeg = deg1 - deg2;
        var diff = Angle.createDegrees(deg1).sub(Angle.createDegrees(deg2));
        assertEquals(Angle.createDegrees(diffDeg), diff);
    }

    @Test
    void angleNegationCorrectRandom() {
        var deg = randomAngle();
        var ang = Angle.createDegrees(deg);
        assertEquals(Angle.createDegrees(-deg), ang.negate());
    }

    // Angle Rotation Tests

    @Test
    void angleRotationDegreesCorrect() {
        var ang = Angle.createDegrees(0f); // 0º

        ang.rotateDegrees(45f); // 45º
        assertFloatEquals(HALF_SQRT_2, ang.getSin()); // sin(45º) = 1/√2
        assertFloatEquals(HALF_SQRT_2, ang.getCos()); // cos(45º) = 1/√2
    }

    @Test
    void angleRotationRadiansCorrect() {
        var ang = Angle.createRadians(PI / 2f); // 90º = π/2

        ang.rotateRadians(-PI / 4f); // -π/4 = -45º
        assertFloatEquals(HALF_SQRT_2, ang.getSin()); // sin(0) = 0
        assertFloatEquals(HALF_SQRT_2, ang.getCos()); // cos(0) = 1
    }

    // Helper Methods

    private float randomAngle() {
        return Random.randomRange(-360, 360, 15f);
    }

}
