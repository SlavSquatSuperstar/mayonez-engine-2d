package mayonez.graphics

import mayonez.graphics.renderer.*
import mayonez.math.shapes.*
import mayonez.util.*
import java.awt.*

/**
 * Passes shape and color information to a
 * [mayonez.graphics.renderer.DebugRenderer].
 *
 * @author SlavSquatSuperstar
 */
internal data class DebugShape(
    var shape: MShape,
    internal val color: MColor,
    internal val fill: Boolean,
    private val zIndex: Int
) : JRenderable, GLRenderable {

    companion object {
        /** How many pixels wide a shape should be drawn. */
        const val STROKE_SIZE: Float = 2f
    }

    constructor(shape: MShape, color: MColor, fill: Boolean, priority: DrawPriority) :
            this(shape, color, fill, priority.zIndex)

    // Renderer Methods

    override fun render(g2: Graphics2D?) {
        if (g2 != null) {
            g2.color = color.toAWT()
            val awtShape = shape.toAWTShape()
            if (fill) g2.fill(awtShape) else g2.draw(awtShape)
        }
    }

    /**
     * Pushes a shape's vertices and texture to a render batch.
     *
     * @param batch the batch
     */
    override fun pushToBatch(batch: RenderBatch) {
        val color = color.toGL()
        when (val shape = this.shape) {
            is Edge -> batch.pushLine(shape, color)
            is Triangle -> batch.pushTriangle(shape, color)
        }
    }

    private fun RenderBatch.pushLine(line: Edge, color: GLColor) {
        pushVec2(line.start)
        pushVec4(color)
        pushVec2(line.end)
        pushVec4(color)
    }

    private fun RenderBatch.pushTriangle(tri: Triangle, color: GLColor) {
        for (v in tri.vertices) {
            pushVec2(v)
            pushVec4(color)
        }
    }

    // Renderable Methods

    override fun getBatchSize(): Int = if (fill) RenderBatch.MAX_TRIANGLES else RenderBatch.MAX_LINES

    override fun getPrimitive(): DrawPrimitive = if (fill) DrawPrimitive.TRIANGLE else DrawPrimitive.LINE

    override fun getZIndex(): Int = zIndex

    override fun isEnabled(): Boolean = true

    override fun toString(): String = "${shape.javaClass.simpleName} (fill = $fill, z = $zIndex)"

}