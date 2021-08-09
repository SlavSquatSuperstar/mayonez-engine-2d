package slavsquatsuperstar.mayonez.renderer

import slavsquatsuperstar.math.Vec2
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Stroke
import java.util.function.Consumer
import kotlin.math.roundToInt

/**
 * Draws all user interface components and overlays onto the screen. Note: all shape methods use position as the top-left.
 */
object IMGUI {

    private const val STROKE_SIZE = 4
    private val stroke: Stroke = BasicStroke(STROKE_SIZE.toFloat())
    private val shapes: MutableList<Renderable> = ArrayList() // map shapes to color?

    // Game Loop Method
    fun render(g2: Graphics2D) {
        if (shapes.isNotEmpty()) {
            g2.stroke = stroke
            shapes.forEach(Consumer { s: Renderable -> s.draw(g2) })
            shapes.clear()
        }
    }

    // Draw Shapes

    @JvmStatic
    fun drawCircle(position: Vec2, radius: Float, color: Color?) {
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.drawOval(round(position.x), round(position.y), round(radius * 2), round(radius * 2))
        })
    }

    @JvmStatic
    fun drawRect(position: Vec2, width: Float, height: Float, color: Color?) {
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.drawRect(round(position.x), round(position.y), round(width), round(height))
        })
    }

    // Fill Shapes

    @JvmStatic
    fun fillCircle(position: Vec2, radius: Float, color: Color?) {
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.fillOval(round(position.x), round(position.y), round(radius * 2), round(radius * 2))
        })
    }

    @JvmStatic
    fun fillRect(position: Vec2, width: Float, height: Float, color: Color?) {
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.fillRect(round(position.x), round(position.y), round(width), round(height))
        })
    }

    // Draw Other Objects

    @JvmStatic
    fun drawPoint(position: Vec2, color: Color?) {
        // Fill a circle with diameter "STROKE_SIZE" centered at "position"
        val min = position.sub(Vec2(STROKE_SIZE.toFloat(), STROKE_SIZE.toFloat()).div(2f))
        fillCircle(min, STROKE_SIZE * 0.5f, color)
    }

    @JvmStatic
    fun drawLine(start: Vec2, end: Vec2, color: Color?) {
        shapes.add(Renderable { g2: Graphics2D ->
            g2.color = color
            g2.drawLine(round(start.x), round(start.y), round(end.x), round(end.y))
        })
    }

    // Helper Method
    private fun round(value: Float): Int = value.roundToInt()
}