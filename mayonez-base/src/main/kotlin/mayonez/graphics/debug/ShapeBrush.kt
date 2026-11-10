package mayonez.graphics.debug

import mayonez.graphics.*
import java.util.*

/**
 * Defines what style to use while drawing a [DebugShape].
 *
 * @author SlavSquatSuperstar
 */
data class ShapeBrush(
    /** The color of the shape. */
    val color: MColor,
    /** Whether the shape interior should be filled. */
    val fill: Boolean,
    /** The z-index of the shape. */
    val zIndex: Int,
    /** The stroke width of the shape outline in pixels. */
    val strokeSize: Float
) {

    // Copy Methods

    /**
     * Create a copy of this brush with a new color.
     *
     * @param color the new color
     */
    fun setColor(color: MColor): ShapeBrush {
        return this.copy(color = color)
    }

    /**
     * Create a copy of this brush with a new fill.
     *
     * @param fill the new fill
     */
    fun setFill(fill: Boolean): ShapeBrush {
        return this.copy(fill = fill)
    }

    /**
     * Create a copy of this brush with a new z-index.
     *
     * @param zIndex the new z-index
     */
    fun setZIndex(zIndex: Int): ShapeBrush {
        return this.copy(zIndex = zIndex)
    }

    /**
     * Create a copy of this brush with a new stroke size.
     *
     * @param strokeSize the new stroke size
     */
    fun setStrokeSize(strokeSize: Float): ShapeBrush {
        return this.copy(strokeSize = strokeSize)
    }

    // Object Overrides

    override fun equals(other: Any?): Boolean {
        return other is ShapeBrush &&
                this.color == other.color &&
                this.fill == other.fill &&
                this.zIndex == other.zIndex &&
                this.strokeSize == other.strokeSize
    }

    override fun hashCode(): Int {
        return Objects.hash(color, fill, zIndex, strokeSize)
    }

    override fun toString(): String {
        return "Brush (color = $color, fill = $fill, z-index = $zIndex, stroke size = $strokeSize)"
    }

    companion object {

        internal const val DEFAULT_STROKE_SIZE: Float = 2f
        internal val DEFAULT_COLOR: MColor = Colors.BLACK

        // Factory Methods

        @JvmStatic
        fun createPointBrush(color: MColor? = DEFAULT_COLOR): ShapeBrush {
            return DrawPriority.POINT.createBrush(color ?: DEFAULT_COLOR)
        }

        @JvmStatic
        fun createLineBrush(color: MColor? = DEFAULT_COLOR): ShapeBrush {
            return DrawPriority.LINE.createBrush(color ?: DEFAULT_COLOR)
        }

        @JvmStatic
        fun createOutlineBrush(color: MColor? = DEFAULT_COLOR): ShapeBrush {
            return DrawPriority.SHAPE_OUTLINE.createBrush(color ?: DEFAULT_COLOR)
        }

        @JvmStatic
        fun createSolidBrush(color: MColor? = DEFAULT_COLOR): ShapeBrush {
            return DrawPriority.SOLID_SHAPE.createBrush(color ?: DEFAULT_COLOR)
        }

    }

}