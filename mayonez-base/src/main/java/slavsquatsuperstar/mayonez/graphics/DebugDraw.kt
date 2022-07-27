package slavsquatsuperstar.mayonez.graphics

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Colors
import slavsquatsuperstar.mayonez.Mayonez
import slavsquatsuperstar.mayonez.annotations.EngineType
import slavsquatsuperstar.mayonez.annotations.UsesEngine
import slavsquatsuperstar.mayonez.graphics.renderer.Renderable
import slavsquatsuperstar.mayonez.physics.colliders.CircleCollider
import slavsquatsuperstar.mayonez.physics.colliders.Collider
import slavsquatsuperstar.mayonez.physics.colliders.PolygonCollider
import slavsquatsuperstar.mayonez.physics.shapes.*
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
object DebugDraw {

    private const val STROKE_SIZE = 2
    private val stroke: Stroke = BasicStroke(STROKE_SIZE.toFloat())
    private val shapes: MutableList<ShapeDrawer> = ArrayList() // map to Color

    // Game Loop Method
    fun render(g2: Graphics2D) {
        if (shapes.isNotEmpty()) {
            shapes.sortByDescending { s: ShapeDrawer -> s.order } // Sort shapes by priority, may case lag
            g2.stroke = stroke
            shapes.forEach { s: Renderable -> s.draw(g2) }
            shapes.clear()
        }
    }

    // Draw Point
    /**
     * Draws a point onto the screen
     *
     * @param position where the point is located, in world coordinates
     * @param color    the color to use
     */
    @JvmStatic
    fun drawPoint(position: Vec2, color: Color?) {
        val radiusPx = STROKE_SIZE.toFloat() // Fill a circle with radius "STROKE_SIZE" in pixels
        shapes.add(ShapeDrawer(DrawPriority.POINT) { g2: Graphics2D ->
            g2.color = color ?: Colors.BLACK
            g2.fill(
                Ellipse2D.Float(
                    position.x.toScreen() - radiusPx, position.y.toScreen() - radiusPx,
                    radiusPx * 2, radiusPx * 2
                )
            )
        })
    }

    // Draw Lines

    /**
     * Draws a line segment onto the screen.
     *
     * @param start the segment's starting point, in world coordinates
     * @param end   the segment's ending point in, world coordinates
     * @param color the color to use
     */
    @JvmStatic
    fun drawLine(start: Vec2, end: Vec2, color: Color?) {
        val startPx = start.toScreen()
        val endPx = end.toScreen()
        shapes.add(ShapeDrawer(DrawPriority.LINE) { g2: Graphics2D ->
            g2.color = color ?: Colors.BLACK
            g2.draw(Line2D.Float(startPx.x, startPx.y, endPx.x, endPx.y))
        })
    }

    /**
     * Draws a vector onto the screen.
     *
     * @param origin    the vector's starting point, in world coordinates
     * @param direction how far away the vector's end point is, in world coordinates
     * @param color     the color to use
     */
    @JvmStatic
    fun drawVector(origin: Vec2, direction: Vec2, color: Color?) {
        drawLine(origin, origin.add(direction), color)
//        drawPoint(origin.add(direction), color) // draw the "arrowhead"
    }

    // Draw Colliders

    /**
     * Draws a collider onto the screen.
     *
     * @param shape a [Collider]
     * @param color the color to use
     */
    @JvmStatic
    fun drawShape(shape: Collider?, color: Color?) {
        when (shape) {
            is CircleCollider -> drawCircle(shape, color ?: Colors.BLACK)
            is PolygonCollider -> drawPolygon(shape, color ?: Colors.BLACK)
        }
    }

    private fun drawCircle(circle: CircleCollider, color: Color) {
        val minPx = (circle.center() - Vec2(circle.radius, circle.radius)).toScreen()
        val diameterPx = (circle.radius * 2).toScreen()
        shapes.add(ShapeDrawer(DrawPriority.SHAPE) { g2: Graphics2D ->
            g2.color = color
            g2.draw(Ellipse2D.Float(minPx.x, minPx.y, diameterPx, diameterPx))
        })
    }

    private fun drawPolygon(polygon: PolygonCollider, color: Color) {
        val shape = java.awt.Polygon()
        for (point in polygon.getVertices()) shape.addPoint(
            point.x.toScreen().roundToInt(),
            point.y.toScreen().roundToInt()
        )
        shapes.add(ShapeDrawer(DrawPriority.SHAPE) { g2: Graphics2D ->
            g2.color = color
            g2.drawPolygon(shape)
        })
    }

    // Draw Shapes
    /**
     * Draws a shape onto the screen.
     *
     * @param shape a [Collider]
     * @param color the color to use
     */
    @JvmStatic
    fun drawShape(shape: Shape?, color: Color?) {
        when (shape) {
            is Edge -> drawLine(shape.start, shape.end, color ?: Colors.BLACK)
            is Circle -> drawCircle(shape, color ?: Colors.BLACK)
            is Ellipse -> drawEllipse(shape, color ?: Colors.BLACK)
            is Rectangle -> drawRectangle(shape, color ?: Colors.BLACK)
            is Polygon -> drawPolygon(shape, color ?: Colors.BLACK)
        }
    }

    private fun drawCircle(circle: Circle, color: Color) {
        val minPx = (circle.center() - Vec2(circle.radius)).toScreen()
        val diameterPx = (circle.radius * 2).toScreen()
        shapes.add(ShapeDrawer(DrawPriority.SHAPE) { g2: Graphics2D ->
            g2.color = color
            g2.draw(Ellipse2D.Float(minPx.x, minPx.y, diameterPx, diameterPx))
        })
    }

    private fun drawEllipse(ellipse: Ellipse, color: Color) {
        val sizePx = ellipse.size.toScreen()
        val centerPx = ellipse.center.toScreen()
        val minPx = centerPx - (sizePx * 0.5f)
        val rotXf = AffineTransform.getRotateInstance(
            Math.toRadians(ellipse.angle.toDouble()),
            centerPx.x.toDouble(),
            centerPx.y.toDouble()
        )
        shapes.add(ShapeDrawer(DrawPriority.SHAPE) { g2: Graphics2D ->
            g2.color = color
            g2.draw(rotXf.createTransformedShape(Ellipse2D.Float(minPx.x, minPx.y, sizePx.x, sizePx.y)))
        })
    }

    private fun drawPolygon(polygon: Polygon, color: Color) {
        val shape = java.awt.Polygon()
        for (point in polygon.vertices) shape.addPoint(
            point.x.toScreen().roundToInt(),
            point.y.toScreen().roundToInt()
        )
        shapes.add(ShapeDrawer(DrawPriority.SHAPE) { g2: Graphics2D ->
            g2.color = color
            g2.drawPolygon(shape)
        })
    }

    private fun drawRectangle(rect: Rectangle, color: Color) {
        val minPx = rect.min().toScreen()
        shapes.add(ShapeDrawer(DrawPriority.SHAPE) { g2: Graphics2D ->
            g2.color = color
            g2.draw(Rectangle2D.Float(minPx.x, minPx.y, rect.width.toScreen(), rect.height.toScreen()))
        })
    }

    // Fill Shapes

    /**
     * Fills in a shape onto the screen.
     *
     * @param shape a [Collider] instance
     * @param color the color to use
     */
    @JvmStatic
    fun fillShape(shape: Collider?, color: Color?) {
        when (shape) {
            is CircleCollider -> fillCircle(shape, color ?: Colors.BLACK)
            is PolygonCollider -> fillPolygon(shape, color ?: Colors.BLACK)
        }
    }

    private fun fillCircle(circle: CircleCollider, color: Color) {
        val minPx = (circle.center() - Vec2(circle.radius, circle.radius)).toScreen()
        val diameterPx = (circle.radius * 2).toScreen()
        shapes.add(ShapeDrawer(DrawPriority.SHAPE) { g2: Graphics2D ->
            g2.color = color
            g2.fill(Ellipse2D.Float(minPx.x, minPx.y, diameterPx, diameterPx))
        })
    }

    private fun fillPolygon(box: PolygonCollider, color: Color) {
        val shape = java.awt.Polygon()
        for (point in box.getVertices())
            shape.addPoint(point.x.toScreen().roundToInt(), point.y.toScreen().roundToInt())
        shapes.add(ShapeDrawer(DrawPriority.SHAPE) { g2: Graphics2D ->
            g2.color = color
            g2.fillPolygon(shape)
        })
    }

    // Helper Methods/Classes

    /**
     * Converts a coordinate from world to screen units.
     *
     * @return the corresponding screen pixel
     */
    private fun Float.toScreen(): Float = this * Mayonez.scene.cellSize

    /**
     * Converts a pair of coordinates from world to screen units.
     *
     * @return the corresponding screen pixels
     */
    private fun Vec2.toScreen(): Vec2 = this * Mayonez.scene.cellSize

    /**
     * The order an object should be drawn. Higher priority objects (smaller enum order) will be drawn last to be more visible.
     */
    private enum class DrawPriority {
        POINT, LINE, SHAPE
    }

    private class ShapeDrawer(drawPriority: DrawPriority, val drawFunc: (Graphics2D) -> (Unit)) : Renderable {
        val order = drawPriority.ordinal
        override fun draw(g2: Graphics2D) = drawFunc(g2)
    }

}