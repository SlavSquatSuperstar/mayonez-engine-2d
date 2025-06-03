package mayonez.graphics.debug

import mayonez.graphics.*
import java.util.*

/**
 * Defines what parameters to use while drawing a
 * [mayonez.graphics.debug.DebugShape].
 *
 * @author SlavSquatSuperstar
 */
data class ShapeBrush(
    val color: MColor,
    val fill: Boolean,
    val zIndex: Int,
    val strokeSize: Float // TODO stroke looks twice as wide as it should be
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

        // Factory Methods

        @JvmStatic
        fun createLineBrush(color: MColor?): ShapeBrush {
            return DrawPriority.LINE.createBrush(color)
        }

        @JvmStatic
        fun createOutlineBrush(color: MColor?): ShapeBrush {
            return DrawPriority.SHAPE_OUTLINE.createBrush(color)
        }

        @JvmStatic
        fun createSolidBrush(color: MColor?): ShapeBrush {
            return DrawPriority.SOLID_SHAPE.createBrush(color)
        }

    }

}