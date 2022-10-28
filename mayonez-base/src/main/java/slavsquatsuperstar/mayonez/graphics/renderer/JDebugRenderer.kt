package slavsquatsuperstar.mayonez.graphics.renderer

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.annotations.EngineType
import slavsquatsuperstar.mayonez.annotations.UsesEngine
import slavsquatsuperstar.mayonez.event.Receivable
import slavsquatsuperstar.mayonez.physics.shapes.*
import slavsquatsuperstar.util.Colors
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D
import kotlin.math.roundToInt

/**
 * Draws debug information using AWT.
 */
@UsesEngine(EngineType.AWT)
class JDebugRenderer : DebugRenderer() {
    private val shapes = ArrayList<JShapeDrawer>()

    override fun addShape(shape: DebugShape) {
        shapes.add(JShapeDrawer(shape.shape.toAwt() ?: return, shape.color, shape.fill, shape.priority))
    }

    // Game Loop Methods

    override fun render(g2: Graphics2D?) {
        // Move screen to camera position
        val oldXf = g2?.transform ?: return
        if (camera != null) {
            val camOffset = camera.offset
            g2.translate(-camOffset.x.toDouble(), -camOffset.y.toDouble())
        }

        // Draw shapes
        if (shapes.isNotEmpty()) {
            shapes.sortWith(Comparator.comparingInt { s: JShapeDrawer -> s.priority.ordinal })
            g2.stroke = STROKE
            shapes.forEach { s: JShapeDrawer -> s.draw(g2) }
            shapes.clear()
        }
        g2.transform = oldXf
    }

    override fun stop() {
        shapes.clear()
    }

    // Shape Helper Methods

    private fun Shape.toAwt(): java.awt.Shape? {
        return when (this) {
            is Edge -> Line2D.Float(start.x, start.y, end.x, end.y)
            is Ellipse -> {
                val min = this.center() - size * 0.5f
                val ellipse = Ellipse2D.Float(min.x, min.y, size.x, size.y)
                if (this is Circle || this.isAxisAligned) ellipse
                else ellipse.rotate(this.center(), angle)
            }

            is Rectangle -> {
                val min = this.min()
                val rect = Rectangle2D.Float(min.x, min.y, this.width, this.height)
                if (this.isAxisAligned) rect else rect.rotate(this.center(), this.angle)
            }

            is Polygon -> {
                val poly = java.awt.Polygon()
                this.vertices.forEach { poly.addPoint(it.x.roundToInt(), it.y.roundToInt()) }
                poly
            }

            else -> null
        }
    }

    private fun java.awt.Shape.rotate(center: Vec2, angle: Float): java.awt.Shape {
        val rotXf = AffineTransform.getRotateInstance(
            Math.toRadians(angle.toDouble()),
            center.x.toDouble(), center.y.toDouble()
        )
        return rotXf.createTransformedShape(this)
    }

    // Helper Class

    /**
     * Maps a shape to an AWT draw function.
     *
     * @author SlavSquatSuperstar
     */
    class JShapeDrawer(
        private val shape: java.awt.Shape,
        private val color: Color?,
        private val fill: Boolean,
        val priority: DebugShape.Priority
    ) : Receivable {
        fun draw(g2: Graphics2D?) = onReceive(g2)

        override fun onReceive(vararg args: Any?) {
            val g2 = (args[0] as Graphics2D?) ?: return
            g2.color = color ?: Colors.BLACK
            if (fill) g2.fill(shape)
            else g2.draw(shape)
        }
    }
}