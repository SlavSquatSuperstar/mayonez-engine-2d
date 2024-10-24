package mayonez.graphics;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.graphics.Color} class.
 *
 * @author SlavSquatSuperstar
 */
class ColorTest {

    @Test
    void rgbHexCodeCorrect() {
        // hex #abcdef
        var color = new Color(171, 205, 239);
        assertEquals("abcdef", color.rgbHexCode());
    }

    @Test
    void rgbaHexCodeCorrect() {
        // hex #12345678
        var color = new Color(18, 52, 86, 120);
        assertEquals("12345678", color.rgbaHexCode());
    }

    @Test
    void floatValuesCorrect() {
        // hex #1f3f7fff
        var color = new Color(32, 64, 128, 255);
        var delta = 0.01f;
        assertEquals(0.125f, color.getFRed(), delta);
        assertEquals(0.25f, color.getFGreen(), delta);
        assertEquals(0.5f, color.getFBlue(), delta);
        assertEquals(1f, color.getFAlpha(), delta);
    }

    @Test
    void combinedValueGivesCorrectRGBAComponents() {
        // hex #12345678
        var color = new Color(2014458966);
        assertEquals(18, color.getRed());
        assertEquals(52, color.getGreen());
        assertEquals(86, color.getBlue());
        assertEquals(120, color.getAlpha());
    }

    @Test
    void rgbaComponentsGivesCorrectCombinedValue() {
        // hex #12345678
        var color = new Color(18, 52, 86, 120);
        assertEquals(2014458966, color.getRGBAValue());
    }

    @Test
    void combineColorsSuccess() {
        var color1 = new Color(63, 63, 63, 255);
        var color2 = new Color(255, 0, 127, 255);
        var result = new Color(63, 0, 31, 255);
        assertEquals(result, color1.combine(color2));
    }

}
