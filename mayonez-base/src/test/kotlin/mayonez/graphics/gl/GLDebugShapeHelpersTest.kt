package mayonez.graphics.gl

import mayonez.graphics.*
import mayonez.graphics.debug.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.renderer.gl.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private const val TEST_ZOOM: Float = 1f
private const val TEST_STROKE: Float = 2f
private val DEFAULT_COlOR: Color = Colors.WHITE

/**
 * Unit tests for the [mayonez.graphics.gl.GLDebugShapeHelpersTest] file.
 *
 * @author SlavSquatSuperstar
 */
internal class GLDebugShapeHelpersTest {

    @Test
    fun getDrawnSolidCircleCorrect() {
        val circle = Circle(Vec2(5f), 10f)
        val brush = ShapeBrush.createSolidBrush(DEFAULT_COlOR).setStrokeSize(TEST_STROKE)
        val result = circle.getDrawShape(brush, TEST_ZOOM)
        val expected = DebugShape(Circle(Vec2(5f), 10f), brush)
        assertEquals(expected, result)
    }

    @Test
    fun getDrawnOutlineCircleCorrect() {
        val circle = Circle(Vec2(5f), 10f)
        val brush = ShapeBrush.createOutlineBrush(DEFAULT_COlOR).setStrokeSize(TEST_STROKE)
        val result = circle.getDrawShape(brush, TEST_ZOOM)
        val expected = DebugShape(Circle(Vec2(5f), 11f), brush)
        assertEquals(expected, result)
    }

}