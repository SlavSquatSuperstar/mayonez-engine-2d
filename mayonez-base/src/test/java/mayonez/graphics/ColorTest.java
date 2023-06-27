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
    void rgbaHexCodeCorrect() {
        // hex #12345678
        var color = new Color(18, 52, 86, 120);
        assertEquals("#12345678", color.rgbaHexCode());
    }

    @Test
    void rgbHexCodeCorrect() {
        // hex #abcdef
        var color = new Color(171, 205, 239);
        assertEquals("#abcdef", color.rgbHexCode());
    }

    @Test
    void combinedValueGivesCorrectRGBAComponents() {
        // hex #12345678
//        var color = new Color(18, 52, 86, 120);
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

}
