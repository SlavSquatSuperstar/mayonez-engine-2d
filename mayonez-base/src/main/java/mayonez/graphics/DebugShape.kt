package mayonez.graphics

import mayonez.graphics.renderer.*
import mayonez.graphics.sprites.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.math.shapes.Polygon
import mayonez.math.shapes.Rectangle
import mayonez.util.*
import java.awt.*
import java.awt.geom.*
import kotlin.math.*

/**
 * Passes shape and color information to a
 * [mayonez.graphics.renderer.DebugRenderer].
 *
 * @author SlavSquatSuperstar
 */
class DebugShape(
    @JvmField internal var shape: MShape,
    @JvmField internal val color: MColor,
    private val fill: Boolean,
    @JvmField internal val zIndex: Int
) : JRenderable, GLRenderable {

    constructor(shape: MShape, color: MColor, fill: Boolean, priority: DrawPriority) :
            this(shape, color, fill, priority.zIndex)

    internal constructor(shape: MShape, debugShape: DebugShape) : // copy from another shape
            this(shape, debugShape.color, debugShape.fill, debugShape.zIndex)

    constructor(sprite: ShapeSprite) : // create from shape sprite
            this(sprite.shape, sprite.color, sprite.fill, sprite.zIndex)

    // Renderer Methods

    override fun render(g2: Graphics2D?) {
        if (g2 == null) return
        g2.color = color.toAWT()
        val awtShape = shape.toAwt()
        if (fill) g2.fill(awtShape) else g2.draw(awtShape)
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

    /**
     * Break down this shape into its simplest components (lines or triangles).
     *
     * @return an array of primitive shapes
     */
    fun splitIntoParts(): Array<out MShape> {
        return when (val shape = this.shape) {
            is Edge -> arrayOf(shape) // add line directly
            is Polygon -> shape.splitIntoParts(fill) // else break into lines or triangles
            is Ellipse -> shape.toPolygon().splitIntoParts(fill)
            else -> emptyArray()
        }
    }

    // Renderable Methods

    override fun getBatchSize(): Int = if (fill) RenderBatch.MAX_TRIANGLES else RenderBatch.MAX_LINES

    override fun getPrimitive(): DrawPrimitive = if (fill) DrawPrimitive.TRIANGLE else DrawPrimitive.LINE

    override fun getZIndex(): Int = zIndex

    override fun isEnabled(): Boolean = true

    override fun toString(): String = "${shape.javaClass.simpleName} (fill = $fill, z = $zIndex)"

    // Helper Methods

    companion object {
        /** How many pixels wide a shape should be drawn. */
        const val STROKE_SIZE: Float = 2f

        private fun MShape.toAwt(): JShape? {
            return when (this) {
                is Edge -> {
                    Line2D.Float(start.x, start.y, end.x, end.y)
                }

                is Ellipse -> {
                    val min = this.center() - (size * 0.5f)
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

        private fun Polygon.splitIntoParts(fill: Boolean): Array<out MShape> {
            return if (fill) this.triangles else this.edges
        }
    }

}