package mayonez.math;

import org.junit.jupiter.api.Test;

import static mayonez.test.TestUtils.assertFloatEquals;

/**
 * Unit tests for the {@link mayonez.math.Angle} class.
 *
 * @author SlavSquatSuperstar
 */
public class AngleTests {

    @Test
    public void degreesConstructorCorrect() {
        Angle ang1 = Angle.createDegrees(45f); // 45º = π/4
        float f = (float) (1 / Math.sqrt(2.0)); // √2/2 = 1/√2
        assertFloatEquals(ang1.getSin(), f);
        assertFloatEquals(ang1.getCos(), f);

        Angle ang2 = Angle.createDegrees(30f); // 30º = π/6
        assertFloatEquals(ang2.getSin(), 0.5f); // sin(30º) = 1/2
        assertFloatEquals(ang2.getCos(), (float) (Math.sqrt(3.0) * 0.5)); // cos(30º) = √3/2
    }

    @Test
    public void radiansConstructorCorrect() {
        Angle ang1 = Angle.createRadians((float) (Math.PI * 0.25)); // π/4 = 45º
        float f = (float) (1.0 / Math.sqrt(2.0)); // √2/2 = 1/√2
        assertFloatEquals(ang1.getSin(), f);
        assertFloatEquals(ang1.getCos(), f);

        Angle ang2 = Angle.createRadians((float) (Math.PI / 3.0)); // π/3 = 60º
        assertFloatEquals(ang2.getSin(), (float) (Math.sqrt(3.0) * 0.5)); // sin(60º) = √3/2
        assertFloatEquals(ang2.getCos(), 0.5f); // cos(60º) = 1/2
    }

    @Test
    public void angleAdditionCorrect() {
        Angle ang1 = Angle.createDegrees(90f); // 90º = π/2
        Angle ang2 = Angle.createDegrees(30f); // 30º = π/6
        Angle ang3 = ang1.add(ang2); // 120º = 2π/3

        assertFloatEquals(ang3.getDegrees(), 120f);
        assertFloatEquals(ang3.getRadians(), (float) (Math.PI * 2.0 / 3.0));
        assertFloatEquals(ang3.getSin(), (float) (Math.sqrt(3.0) * 0.5)); // sin(120º) = √3/2
        assertFloatEquals(ang3.getCos(), -0.5f); // cos(120º) = -1/2
    }

    @Test
    public void angleRotationCorrect() {
        Angle ang = Angle.createDegrees(0f); // 90º = π/2
        float f = (float) (1.0 / Math.sqrt(2.0)); // √2/2 = 1/√2

        ang.rotateDegrees(45f); // 45º
        assertFloatEquals(ang.getSin(), f); // sin(45º) = 1/√2
        assertFloatEquals(ang.getCos(), f); // cos(45º) = 1/√2

        ang.rotateRadians((float) (Math.PI * -0.25)); // π/4 = 45º
        assertFloatEquals(ang.getSin(), 0f); // sin(0) = 0
        assertFloatEquals(ang.getCos(), 1f); // cos(0) = 1
    }

}
