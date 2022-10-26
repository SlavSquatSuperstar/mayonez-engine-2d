package slavsquatsuperstar.mayonez.graphics

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Mayonez
import slavsquatsuperstar.mayonez.annotations.EngineType
import slavsquatsuperstar.mayonez.annotations.UsesEngine
import slavsquatsuperstar.mayonez.event.Receivable
import slavsquatsuperstar.mayonez.physics.colliders.Collider
import slavsquatsuperstar.mayonez.physics.shapes.*
import slavsquatsuperstar.util.Colors
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Stroke
import java.awt.geom.AffineTransform
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D
import kotlin.math.roundToInt

/**
 * Draws colliders and mathematical objects onto the screen. All methods use world coordinates, and shapes are centered
 * around the collider's position.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
// TODO need z-indexing to work with renderer
object DebugDraw {

    private const val STROKE_SIZE = 2
    private val stroke: Stroke = BasicStroke(STROKE_SIZE.toFloat())
    private val shapes: MutableList<ShapeDrawer> = ArrayList() // map to Color

    // Game Loop Method
    fun render(g2: Graphics2D) {
        if (shapes.isNotEmpty()) {
            shapes.sortBy { s: ShapeDrawer -> s.priority.ordinal } // Sort shapes by priority, may case lag
            g2.stroke = stroke
            shapes.forEach { s: ShapeDrawer -> s.draw(g2) }
            shapes.clear()
        }
    }

    // Public Draw Methods
    /**
     * Draws a point onto the screen
     *
     * @param position where the point is located, in world coordinates
     * @param color    the color to use
     */
    @JvmStatic
    fun drawPoint(position: Vec2, color: Color?) {
        if (!Mayonez.started) return
        // Fill a circle with radius "STROKE_SIZE" in pixels
        val diameterPx = Vec2(STROKE_SIZE.toFloat() * 2f)
        shapes.add(ShapeDrawer(DrawPriority.POINT, ellipse(position.toScreen(), diameterPx), color, true))
    }

    /**
     * Draws a line segment onto the screen.
     *
     * @param start the segment's starting point, in world coordinates
     * @param end   the segment's ending point in, world coordinates
     * @param color the color to use
     */
    @JvmStatic
    fun drawLine(start: Vec2, end: Vec2, color: Color?) {
        if (!Mayonez.started) return
        val startPx = start.toScreen()
        val endPx = end.toScreen()
        shapes.add(ShapeDrawer(DrawPriority.LINE, Line2D.Float(startPx.x, startPx.y, endPx.x, endPx.y), color, false))
    }

    /**
     * Draws a vector onto the screen.
     *
     * @param origin    the vector's starting point, in world coordinates
     * @param direction how far away the vector's end point is, in world coordinates
     * @param color     the color to use
     */
    @JvmStatic
    fun drawVector(origin: Vec2, direction: Vec2, color: Color?) = drawLine(origin, origin.add(direction), color)

    // Draw Shapes
    /**
     * Draws the outline of a shape onto the screen.
     *
     * @param shape a [Collider]
     * @param color the color to use
     */
    @JvmStatic
    fun drawShape(shape: Shape?, color: Color?) {
        if (!Mayonez.started) return
        val drawColor = color ?: Colors.BLACK
        when (shape) {
            is Edge -> drawLine(shape.start, shape.end, drawColor)
            is Circle -> drawCircle(shape, drawColor, false)
            is Ellipse -> drawEllipse(shape, drawColor, false)
            is Rectangle -> drawRectangle(shape, drawColor, false)
            is Polygon -> drawPolygon(shape, drawColor, false)
        }
    }

    /**
     * Draws a shape onto the screen and fills in the interior.
     *
     * @param shape a [Collider]
     * @param color the color to use
     */
    @JvmStatic
    fun fillShape(shape: Shape?, color: Color?) {
        if (!Mayonez.started) return
        val drawColor = color ?: Colors.BLACK
        when (shape) {
            is Edge -> drawLine(shape.start, shape.end, drawColor)
            is Circle -> drawCircle(shape, drawColor, true)
            is Ellipse -> drawEllipse(shape, drawColor, true)
            is Rectangle -> drawRectangle(shape, drawColor, true)
            is Polygon -> drawPolygon(shape, drawColor, true)
        }
    }

    // Internal Draw methods

    private fun drawToScreen(g2: Graphics2D, color: Color?, shape: java.awt.Shape, fill: Boolean) {
        g2.color = color ?: Colors.BLACK
        if (fill) g2.fill(shape)
        else g2.draw(shape)
    }

    private fun drawCircle(circle: Circle, color: Color, fill: Boolean) {
        val diameterPx = Vec2(circle.radius * 2).toScreen()
        shapes.add(ShapeDrawer(DrawPriority.SHAPE, ellipse(circle.center().toScreen(), diameterPx), color, fill))
    }

    private fun drawEllipse(ellipse: Ellipse, color: Color, fill: Boolean) {
        if (ellipse.isCircle) return drawCircle(ellipse.boundingCircle(), color, fill)
        val centerPx = ellipse.center().toScreen()
        val shape = ellipse(centerPx, ellipse.size.toScreen())
        val rot = if (ellipse.isAxisAligned) shape else shape.rotate(centerPx, ellipse.angle)
        shapes.add(ShapeDrawer(DrawPriority.SHAPE, rot, color, fill))
    }

    private fun drawPolygon(polygon: Polygon, color: Color, fill: Boolean) {
        val shape = java.awt.Polygon()
        polygon.vertices.forEach { shape.addPoint(it.x.toScreen().roundToInt(), it.y.toScreen().roundToInt()) }
        shapes.add(ShapeDrawer(DrawPriority.SHAPE, shape, color, fill))
    }

    private fun drawRectangle(rect: Rectangle, color: Color, fill: Boolean) {
        val minPx = rect.min().toScreen()
        val centerPx = rect.center().toScreen()
        val shape = Rectangle2D.Float(minPx.x, minPx.y, rect.width.toScreen(), rect.height.toScreen())
        val rot = if (rect.isAxisAligned) shape else shape.rotate(centerPx, rect.angle)
        shapes.add(ShapeDrawer(DrawPriority.SHAPE, rot, color, fill))
    }

    // Helper Methods/Classes

    private fun ellipse(center: Vec2, size: Vec2): Ellipse2D {
        val min = center - size * 0.5f
        return Ellipse2D.Float(min.x, min.y, size.x, size.y)
    }

    private fun java.awt.Shape.rotate(center: Vec2, angle: Float): java.awt.Shape {
        val rotXf = AffineTransform.getRotateInstance(
            Math.toRadians(angle.toDouble()),
            center.x.toDouble(), center.y.toDouble()
        )
        return rotXf.createTransformedShape(this)
    }

    /**
     * Converts a coordinate from world to screen units.
     *
     * @return the corresponding screen pixel
     */
    private fun Float.toScreen(): Float = this * Mayonez.scene.scale

    /**
     * Converts a pair of coordinates from world to screen units.
     *
     * @return the corresponding screen pixels
     */
    private fun Vec2.toScreen(): Vec2 = this * Mayonez.scene.scale

    /**
     * The order an object should be drawn. Higher priority objects will be drawn later to be more visible.
     */
    private enum class DrawPriority {
        SHAPE, LINE, POINT
    }

    /**
     * Maps a shape to a draw function.
     */
    private class ShapeDrawer(
        val priority: DrawPriority,
        private val shape: java.awt.Shape,
        private val color: Color?,
        private val fill: Boolean
    ) : Receivable {
        fun draw(g2: Graphics2D) = onReceive(g2)

        override fun onReceive(vararg args: Any?) {
            val g2 = args[0]!! as Graphics2D
            g2.color = color ?: Colors.BLACK
            if (fill) g2.fill(shape)
            else g2.draw(shape)
        }
    }

}