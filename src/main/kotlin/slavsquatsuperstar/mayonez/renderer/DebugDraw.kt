package slavsquatsuperstar.mayonez.renderer

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Game
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D
import slavsquatsuperstar.mayonez.physics2d.colliders.CircleCollider
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D
import slavsquatsuperstar.mayonez.physics2d.colliders.PolygonCollider2D
import java.awt.*
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D
import java.util.function.Consumer
import kotlin.math.roundToInt

/**
 * Draws colliders and mathematical objects onto the screen. All methods use world coordinates, and shapes are centered
 * around the collider's position.
 */
object DebugDraw {

    private const val STROKE_SIZE = 2
    private val stroke: Stroke = BasicStroke(STROKE_SIZE.toFloat())
    private val shapes: MutableList<Renderable> = ArrayList() // map to color?

    // Game Loop Method
    fun render(g2: Graphics2D) {
        if (shapes.isNotEmpty()) {
            g2.stroke = stroke
            shapes.forEach(Consumer { s: Renderable -> s.draw(g2) })
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
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
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
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
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
    fun drawVector(origin: Vec2, direction: Vec2?, color: Color?) {
        drawLine(origin, origin.add(direction!!), color)
        //        drawPoint(origin.add(direction), color); // draw the "arrowhead"
    }

    // Draw Shapes

    /**
     * Draws a shape onto the screen.
     *
     * @param shape a [Collider2D] instance
     * @param color the color to use
     */
    @JvmStatic
    fun drawShape(shape: Collider2D?, color: Color) {
        when (shape) {
            is CircleCollider -> drawCircle(shape, color)
            is AlignedBoxCollider2D -> drawAABB(shape, color)
            is PolygonCollider2D -> drawPolygon(shape, color)
        }
    }

    private fun drawCircle(circle: CircleCollider, color: Color) {
        val minPx = (circle.center() - Vec2(circle.radius(), circle.radius())).toScreen()
        val diameterPx = (circle.radius() * 2).toScreen()
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.draw(Ellipse2D.Float(minPx.x, minPx.y, diameterPx, diameterPx))
        })
    }

    private fun drawAABB(aabb: AlignedBoxCollider2D, color: Color) {
        val minPx = aabb.min().toScreen()
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.draw(Rectangle2D.Float(minPx.x, minPx.y, aabb.width().toScreen(), aabb.height().toScreen()))
        })
    }

    private fun drawPolygon(polygon: PolygonCollider2D, color: Color) {
        val shape = Polygon()
        for (point in polygon.getVertices())
            shape.addPoint(point.x.toScreen().roundToInt(), point.y.toScreen().roundToInt())
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.drawPolygon(shape)
        })
    }

    // Fill Shapes

    /**
     * Fills in a shape onto the screen.
     *
     * @param shape a [Collider2D] instance
     * @param color the color to use
     */
    @JvmStatic
    fun fillShape(shape: Collider2D?, color: Color) {
        when (shape) {
            is CircleCollider -> fillCircle(shape, color)
            is AlignedBoxCollider2D -> fillAABB(shape, color)
            is PolygonCollider2D -> fillPolygon(shape, color)
        }
    }

    private fun fillCircle(circle: CircleCollider, color: Color) {
        val minPx = (circle.center() - Vec2(circle.radius(), circle.radius())).toScreen()
        val diameterPx = (circle.radius() * 2).toScreen()
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.fill(Ellipse2D.Float(minPx.x, minPx.y, diameterPx, diameterPx))
        })
    }

    private fun fillAABB(aabb: AlignedBoxCollider2D, color: Color) {
        val minPx = aabb.min().toScreen()
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.fill(Rectangle2D.Float(minPx.x, minPx.y, aabb.width().toScreen(), aabb.height().toScreen()))
        })
    }

    private fun fillPolygon(box: PolygonCollider2D, color: Color) {
        val shape = Polygon()
        for (point in box.getVertices())
            shape.addPoint(point.x.toScreen().roundToInt(), point.y.toScreen().roundToInt())
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.fillPolygon(shape)
        })
    }

    // Helper Methods

    /**
     * Converts a coordinate from world to screen units.
     *
     * @return the corresponding screen pixel
     */
    private fun Float.toScreen(): Float = this * Game.currentScene().cellSize

    /**
     * Converts a pair of coordinates from world to screen units.
     *
     * @return the corresponding screen pixels
     */
    private fun Vec2.toScreen(): Vec2 = this * Game.currentScene().cellSize.toFloat()
}