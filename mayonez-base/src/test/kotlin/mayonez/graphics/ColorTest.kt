package mayonez.graphics

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Unit tests for the [mayonez.graphics.Color] class.
 *
 * @author SlavSquatSuperstar
 */
internal class ColorTest {

    @Test
    fun rgbHexCodeCorrect() {
        // hex #abcdef
        val color = Color(171, 205, 239)
        assertEquals("abcdef", color.rgbHexCode())
    }

    @Test
    fun rgbaHexCodeCorrect() {
        // hex #12345678
        val color = Color(18, 52, 86, 120)
        assertEquals("12345678", color.rgbaHexCode())
    }

    @Test
    fun floatValuesCorrect() {
        // hex #1f3f7fff
        val color = Color(32, 64, 128, 255)
        val delta = 0.01f
        assertEquals(0.125f, color.fRed, delta)
        assertEquals(0.25f, color.fGreen, delta)
        assertEquals(0.5f, color.fBlue, delta)
        assertEquals(1f, color.fAlpha, delta)
    }

    @Test
    fun combinedValueGivesCorrectRGBAComponents() {
        // hex #12345678
        val color = Color(2014458966)
        assertEquals(18, color.red)
        assertEquals(52, color.green)
        assertEquals(86, color.blue)
        assertEquals(120, color.alpha)
    }

    @Test
    fun rgbaComponentsGivesCorrectCombinedValue() {
        // hex #12345678
        val color = Color(18, 52, 86, 120)
        assertEquals(2014458966, color.getRGBAValue())
    }

    @Test
    fun combineColorsSuccess() {
        val color1 = Color(63, 63, 63, 255)
        val color2 = Color(255, 0, 127, 255)
        val result = Color(63, 0, 31, 255)
        assertEquals(result, color1.combine(color2))
    }

}