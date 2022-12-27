package mayonez.graphics.renderer

import mayonez.graphics.renderable.JRenderable
import mayonez.graphics.sprite.ShapeSprite
import mayonez.math.Vec2
import mayonez.physics.shapes.*
import mayonez.util.JShape
import mayonez.util.MColor
import mayonez.util.MShape
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D
import kotlin.math.roundToInt

/**
 * Passes shape and color information to a [DebugRenderer].
 *
 * @author SlavSquatSuperstar
 */
class DebugShape(
    @JvmField internal val shape: MShape,
    @JvmField internal val color: MColor,
    @JvmField internal val fill: Boolean,
    @JvmField internal val zIndex: Int
) : JRenderable {

    constructor(shape: MShape, color: MColor, fill: Boolean, priority: DrawPriority) :
            this(shape, color, fill, priority.zIndex)

    internal constructor(shape: MShape, debugShape: DebugShape) : // copy from another shape
            this(shape, debugShape.color, debugShape.fill, debugShape.zIndex)

    constructor(sprite: ShapeSprite) : // create from shape sprite
            this(sprite.shape, sprite.color, sprite.fill, sprite.zIndex)

    override fun render(g2: Graphics2D?) {
        if (g2 == null) return
        g2.color = color.toAWT()
        val awtShape = shape.toAwt()
        if (fill) g2.fill(awtShape) else g2.draw(awtShape)
    }

    override fun getZIndex(): Int = zIndex

    override fun toString(): String = "${shape.javaClass.simpleName} (fill = $fill, z = $zIndex)"

    // Helper Methods

    companion object {
        private fun MShape.toAwt(): JShape? {
            return when (this) {
                is Edge -> Line2D.Float(start.x, start.y, end.x, end.y)
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
    }

    // Enum

    /**
     * The order an object should be drawn. Higher priority objects will be drawn later to be more visible.
     */
    enum class DrawPriority(val zIndex: Int) {
        /**
         * Solid shapes, drawn first.
         */
        FILL(-1),

        /**
         * Shape boundaries, after solid shapes and before lines.
         */
        SHAPE(1),

        /**
         * Lines, drawn after shapes and before points.
         */
        LINE(2),

        /**
         * Single points, drawn last.
         */
        POINT(3);
    }

}