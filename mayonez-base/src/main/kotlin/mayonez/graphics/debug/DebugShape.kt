package mayonez.graphics.debug

import mayonez.graphics.*
import mayonez.math.shapes.*
import mayonez.renderer.awt.*
import mayonez.renderer.batch.*
import mayonez.renderer.gl.*
import java.awt.*
import java.util.*

private const val MAX_BATCH_CIRCLES: Int = 200
private const val MAX_BATCH_LINES: Int = 500
private const val MAX_BATCH_TRIANGLES: Int = 1000


/**
 * Passes shape and color information to a [mayonez.renderer.DebugRenderer].
 *
 * @author SlavSquatSuperstar
 */
internal data class DebugShape(internal val shape: MShape, internal val brush: ShapeBrush) :
    JRenderable, GLRenderable {

    private val color: MColor
        get() = brush.color

    internal val fill: Boolean
        get() = brush.fill

    private val strokeSize: Float
        get() = brush.strokeSize

    // AWT Renderer Methods

    override fun render(g2: Graphics2D?) {
        if (g2 == null) return

        // Set brush properties
        g2.color = color.toAWT()
        g2.stroke = BasicStroke(strokeSize)

        // Draw shape
        val awtShape = shape.toAWTShape()
        if (fill) g2.fill(awtShape) else g2.draw(awtShape)
    }

    // GL Renderer Methods

    /**
     * Pushes a shape's vertices and texture to a render batch.
     *
     * @param batch the batch
     */
    override fun pushToBatch(batch: RenderBatch) {
        val color = color.toGL()
        color.w = 1f // Disable transparency due to reused vertices
        batch.pushShape(shape, color, brush)
    }

    // Renderable Methods

    override fun getBatchSize(): Int {
        return when {
            shape is Circle -> MAX_BATCH_CIRCLES
            shape is Ellipse -> MAX_BATCH_CIRCLES
            fill -> MAX_BATCH_TRIANGLES
            else -> MAX_BATCH_LINES
        }
    }

    override fun getPrimitive(): DrawPrimitive {
        return when {
            shape is Circle -> DrawPrimitive.CIRCLE
            shape is Ellipse -> DrawPrimitive.ELLIPSE
            fill -> DrawPrimitive.TRIANGLE
            else -> DrawPrimitive.LINE
        }
    }

    override fun getZIndex(): Int = brush.zIndex

    override fun isEnabled(): Boolean = true

    override fun isInUI(): Boolean = false

    // Object Overrides

    override fun equals(other: Any?): Boolean {
        return other is DebugShape &&
                this.shape == other.shape &&
                this.brush == other.brush
    }

    override fun hashCode(): Int {
        return Objects.hash(shape, brush)
    }

    override fun toString(): String {
        return "Debug $shape, $brush"
    }

}
