package mayonez.graphics.gl

import mayonez.graphics.*
import mayonez.graphics.debug.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.renderer.gl.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.*

private const val TEST_ZOOM: Float = 1f
private const val TEST_STROKE: Float = 2f
private val DEFAULT_COlOR: Color = Colors.WHITE

/**
 * Unit tests for the [mayonez.graphics.gl.GLDebugShapeHelpersTest] file.
 *
 * @author SlavSquatSuperstar
 */
internal class GLDebugShapeHelpersTest {

    // Line Test Methods

    @Test
    fun getQuadFromLineCorrect() {
        val line = Edge(Vec2(1f, 1f), Vec2(4f, 1f))
        val brush = ShapeBrush.createSolidBrush(DEFAULT_COlOR).setStrokeSize(TEST_STROKE)
        val results = line.getQuad(brush, TEST_ZOOM)
        val expected = Quadrangle(Vec2(0f, 0f), Vec2(5f, 0f), Vec2(5f, 2f), Vec2(0f, 2f))
        assertEquals(expected, results)
    }

    // Polygon Test Methods

    @Test
    fun getQuadsFromOutlineRectangleCorrect() {
        val rect = Rectangle.fromMinAndMax(Vec2(1f, 1f), Vec2(9f, 7f))
        val outerVertices = arrayOf(
            Vec2(0f, 0f), Vec2(10f, 0f), Vec2(10f, 8f), Vec2(0f, 8f)
        )
        val innerVertices = arrayOf(
            Vec2(2f, 2f), Vec2(8f, 2f), Vec2(8f, 6f), Vec2(2f, 6f)
        )
        val expected = listOf(
            Quadrangle(outerVertices[0], outerVertices[1], innerVertices[1], innerVertices[0]),
            Quadrangle(outerVertices[1], outerVertices[2], innerVertices[2], innerVertices[1]),
            Quadrangle(outerVertices[2], outerVertices[3], innerVertices[3], innerVertices[2]),
            Quadrangle(outerVertices[3], outerVertices[0], innerVertices[0], innerVertices[3]),
        )

        val brush = ShapeBrush.createSolidBrush(DEFAULT_COlOR).setStrokeSize(TEST_STROKE)
        val results = rect.getEdgeQuads(brush, TEST_ZOOM)
        assertEquals(expected, results)
    }

    @Test
    fun getQuadsFromOutlineTriangle1Correct() {
        val sqrt3 = sqrt(3f)
        val tri = Triangle(
            Vec2(sqrt3, 1f), Vec2(16f - sqrt3, 1f), Vec2(8f, 7 * sqrt3)
        )
        val outerVertices = arrayOf(
            Vec2(0f, 0f), Vec2(16f, 0f), Vec2(8f, 8f * sqrt3)
        )
        val innerVertices = arrayOf(
            Vec2(2f * sqrt3, 2f), Vec2(16f - 2f * sqrt3, 2f), Vec2(8f, 6f * sqrt3)
        )
        val expected = listOf(
            Quadrangle(outerVertices[0], outerVertices[1], innerVertices[1], innerVertices[0]),
            Quadrangle(outerVertices[1], outerVertices[2], innerVertices[2], innerVertices[1]),
            Quadrangle(outerVertices[2], outerVertices[0], innerVertices[0], innerVertices[2]),
        )

        val brush = ShapeBrush.createSolidBrush(DEFAULT_COlOR).setStrokeSize(TEST_STROKE)
        val results = tri.getEdgeQuads(brush, TEST_ZOOM)
        assertQuadranglesAlmostEqual(expected, results)
    }

    @Test
    fun getQuadsFromOutlineTriangle2Correct() {
        val sqrt2 = sqrt(2f)
        val tri = Triangle(
            Vec2(1f + sqrt2, 1f), Vec2(16f - (1f + sqrt2), 1f), Vec2(8f, 8f - sqrt2)
        )
        val outerVertices = arrayOf(
            Vec2(0f, 0f), Vec2(16f, 0f), Vec2(8f, 8f)
        )
        val innerVertices = arrayOf(
            Vec2(2f * (1f + sqrt2), 2f), Vec2(16f - 2f * (1f + sqrt2), 2f), Vec2(8f, 8f - 2 * sqrt2)
        )
        val expected = listOf(
            Quadrangle(outerVertices[0], outerVertices[1], innerVertices[1], innerVertices[0]),
            Quadrangle(outerVertices[1], outerVertices[2], innerVertices[2], innerVertices[1]),
            Quadrangle(outerVertices[2], outerVertices[0], innerVertices[0], innerVertices[2]),
        )

        val brush = ShapeBrush.createSolidBrush(DEFAULT_COlOR).setStrokeSize(TEST_STROKE)
        val results = tri.getEdgeQuads(brush, TEST_ZOOM)
        assertQuadranglesAlmostEqual(expected, results)
    }

    // Circle Test Methods

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

    // Ellipse Test Methods

    @Test
    fun getDrawnSolidEllipseCorrect() {
        val ellipse = Ellipse(Vec2(5f), Vec2(20f, 16f), 30f)
        val brush = ShapeBrush.createSolidBrush(DEFAULT_COlOR).setStrokeSize(TEST_STROKE)
        val result = ellipse.getDrawShape(brush, TEST_ZOOM)
        val expected = DebugShape(Ellipse(Vec2(5f), Vec2(20f, 16f), 30f), brush)
        assertEquals(expected, result)
    }

    @Test
    fun getDrawnOutlineEllipseCorrect() {
        val ellipse = Ellipse(Vec2(5f), Vec2(20f, 16f), 30f)
        val brush = ShapeBrush.createOutlineBrush(DEFAULT_COlOR).setStrokeSize(TEST_STROKE)
        val result = ellipse.getDrawShape(brush, TEST_ZOOM)
        val expected = DebugShape(Ellipse(Vec2(5f), Vec2(22f, 18f), 30f), brush)
        assertEquals(expected, result)
    }

}

private fun assertQuadranglesAlmostEqual(
    expected: List<Quadrangle>, results: List<MPolygon>
) {
    for ((exp, res) in expected zip results) {
        for ((expVert, resVert) in exp.vertices zip res.vertices) {
            assertEquals(expVert.x, resVert.x, 0.33f)
            assertEquals(expVert.y, resVert.y, 0.33f)
        }
    }
}