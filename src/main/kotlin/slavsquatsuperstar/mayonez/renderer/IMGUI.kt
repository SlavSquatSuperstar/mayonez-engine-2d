package slavsquatsuperstar.mayonez.renderer

import slavsquatsuperstar.math.Vec2
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Stroke
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D
import java.util.function.Consumer

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
            shapes.forEach(Consumer { s: Renderable -> s.render(g2) })
            shapes.clear()
        }
    }

    // Draw Shapes

    @JvmStatic
    fun drawCircle(position: Vec2, radius: Float, color: Color?) {
        shapes.add(Renderable { g2 ->
            g2.color = color
            g2.draw(Ellipse2D.Float(position.x, position.y, radius * 2, radius * 2))
        })
    }

    @JvmStatic
    fun drawRect(position: Vec2, size: Vec2, color: Color?) {
        shapes.add(Renderable { g2 ->
            g2.color = color
            g2.draw(Rectangle2D.Float(position.x, position.y, size.x, size.y))
        })
    }

    // Fill Shapes

    @JvmStatic
    fun fillCircle(position: Vec2, radius: Float, color: Color?) {
        shapes.add(Renderable { g2 ->
            g2.color = color
            g2.fill(Ellipse2D.Float(position.x, position.y, radius * 2, radius * 2))
        })
    }

    @JvmStatic
    fun fillRect(position: Vec2, size: Vec2, color: Color?) {
        shapes.add(Renderable { g2 ->
            g2.color = color
            g2.fill(Rectangle2D.Float(position.x, position.y, size.x, size.y))
        })
    }

    // Draw Other Objects

    @JvmStatic
    fun drawPoint(position: Vec2, color: Color?) {
        // Fill a circle with diameter "STROKE_SIZE" centered at "position"
        val min = position - (Vec2(STROKE_SIZE.toFloat(), STROKE_SIZE.toFloat()) / 2f)
        fillCircle(min, STROKE_SIZE * 0.5f, color)
    }

    @JvmStatic
    fun drawLine(start: Vec2, end: Vec2, color: Color?) {
        shapes.add(Renderable { g2 ->
            g2.color = color
            g2.draw(Line2D.Float(start.x, start.y, end.x, end.y))
        })
    }
}