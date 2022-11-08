package slavsquatsuperstar.mayonez.graphics.renderer

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.DebugDraw
import slavsquatsuperstar.mayonez.annotations.EngineType
import slavsquatsuperstar.mayonez.annotations.UsesEngine
import slavsquatsuperstar.mayonez.physics.shapes.*
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D
import kotlin.math.roundToInt

typealias JShape = java.awt.Shape

/**
 * Draws debug information using AWT.
 */
@UsesEngine(EngineType.AWT)
class JDebugRenderer : DebugRenderer {

    private val shapes = ArrayList<DebugShape>()
    private val stroke = BasicStroke(DebugDraw.STROKE_SIZE)

    override fun addShape(shape: DebugShape) {
        shapes.add(shape)
    }

    // Game Loop Methods

    override fun start() = shapes.clear()

    override fun render(g2: Graphics2D?) {
        // Move screen to camera position
        val oldXf = g2?.transform ?: return
        if (camera != null) {
            val camOffset = camera.offset
            g2.translate(-camOffset.x.toDouble(), -camOffset.y.toDouble())
        }

        // Draw shapes
        if (shapes.isNotEmpty()) {
            shapes.sortWith(Comparator.comparingInt { s: DebugShape -> s.zIndex })
            g2.stroke = stroke
            for (shape in shapes) {
                g2.color = shape.color.toAWT()
                val awtShape: JShape? = shape.shape.toAwt()
                if (shape.fill) g2.fill(awtShape) else g2.draw(awtShape)
            }
            shapes.clear()
        }
        g2.transform = oldXf
    }

    override fun stop() = shapes.clear()

    // Shape Helper Methods

    private fun Shape.toAwt(): JShape? {
        return when (this) {
            is Edge -> Line2D.Float(start.x, start.y, end.x, end.y)
            is Ellipse -> {
                val min = this.center() - size * 0.5f
                val ellipse = Ellipse2D.Float(min.x, min.y, size.x, size.y)
                return if (this is Circle || this.isAxisAligned) ellipse
                else ellipse.rotate(this.center(), angle)
            }

            is Rectangle -> {
                val min = this.min()
                val rect = Rectangle2D.Float(min.x, min.y, this.width, this.height)
                return if (this.isAxisAligned) rect else rect.rotate(this.center(), this.angle)
            }

            is Polygon -> {
                val poly = java.awt.Polygon()
                this.vertices.forEach { poly.addPoint(it.x.roundToInt(), it.y.roundToInt()) }
                return poly
            }

            else -> null
        }
    }

    private fun JShape.rotate(center: Vec2, angle: Float): JShape {
        val rotXf = AffineTransform.getRotateInstance(
            Math.toRadians(angle.toDouble()),
            center.x.toDouble(), center.y.toDouble()
        )
        return rotXf.createTransformedShape(this)
    }

}