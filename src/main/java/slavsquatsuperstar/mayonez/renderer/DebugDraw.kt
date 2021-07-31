package slavsquatsuperstar.mayonez.renderer

import slavsquatsuperstar.mayonez.Game
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics2d.colliders.*
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
                    toScreen(position.x) - radiusPx, toScreen(position.y) - radiusPx,
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
        val startPx = toScreen(start)
        val endPx = toScreen(end)
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
            is BoxCollider2D -> drawBox(shape, color)
        }
    }

    private fun drawCircle(circle: CircleCollider, color: Color) {
        val minPx = toScreen(circle.center().sub(Vec2(circle.radius(), circle.radius())))
        val diameterPx = toScreen(circle.radius() * 2)
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.draw(Ellipse2D.Float(minPx.x, minPx.y, diameterPx, diameterPx))
        })
    }

    private fun drawAABB(aabb: AlignedBoxCollider2D, color: Color) {
        val minPx = toScreen(aabb.min())
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.draw(Rectangle2D.Float(minPx.x, minPx.y, toScreen(aabb.width()), toScreen(aabb.height())))
        })
    }

    private fun drawBox(box: BoxCollider2D, color: Color) {
        val obb = Polygon()
        for (point in box.vertices) obb.addPoint(toScreen(point.x).roundToInt(), toScreen(point.y).roundToInt())
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.drawPolygon(obb)
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
            is BoxCollider2D -> fillBox(shape, color)
        }
    }

    private fun fillCircle(circle: CircleCollider, color: Color) {
        val minPx = toScreen(circle.center().sub(Vec2(circle.radius(), circle.radius())))
        val diameterPx = toScreen(circle.radius() * 2)
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.fill(Ellipse2D.Float(minPx.x, minPx.y, diameterPx, diameterPx))
        })
    }

    private fun fillAABB(aabb: AlignedBoxCollider2D, color: Color) {
        val minPx = toScreen(aabb.min())
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.fill(Rectangle2D.Float(minPx.x, minPx.y, toScreen(aabb.width()), toScreen(aabb.height())))
        })
    }

    private fun fillBox(box: BoxCollider2D, color: Color) {
        val obb = Polygon()
        for (point in box.vertices) obb.addPoint(toScreen(point.x).roundToInt(), toScreen(point.y).roundToInt())
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.fillPolygon(obb)
        })
    }

    // Helper Methods

    /**
     * Converts a world coordinate to screen coordinates.
     *
     * @param world the location of something in the world along an axis
     * @return the corresponding screen pixel
     */
    private fun toScreen(world: Float): Float {
        return world * Game.currentScene().cellSize
    }

    /**
     * Converts a world position to screen coordinates.
     *
     * @param world the location of something in the world along both axes
     * @return the corresponding screen pixels
     */
    private fun toScreen(world: Vec2): Vec2 {
        return world.mul(Game.currentScene().cellSize.toFloat())
    }
}