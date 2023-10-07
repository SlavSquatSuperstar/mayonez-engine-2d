package mayonez.graphics.debug

import mayonez.graphics.*

/**
 * Defines what parameters to use while drawing a
 * [mayonez.graphics.debug.DebugShape].
 *
 * @author SlavSquatSuperstar
 */
data class ShapeBrush internal constructor(
    internal val color: MColor,
    internal val fill: Boolean,
    internal val zIndex: Int,
    internal val strokeSize: Float
) {

    private constructor(color: MColor?, priority: DrawPriority) :
            this(color ?: DebugDraw.DEFAULT_COLOR, priority.fill, priority.zIndex, DebugDraw.DEFAULT_STROKE_SIZE)

    // Copy Methods

    fun setZIndex(zIndex: Int): ShapeBrush {
        return this.copy(zIndex = zIndex)
    }

    fun setStrokeSize(strokeSize: Float): ShapeBrush {
        return this.copy(strokeSize = strokeSize)
    }

    override fun toString(): String {
        return "Brush (color = $color, fill = $fill, z-index = $zIndex, stroke size = $strokeSize)"
    }

    companion object {

        // Factory Methods

        @JvmStatic
        fun createLineBrush(color: MColor?): ShapeBrush {
            return ShapeBrush(color, DrawPriority.LINE)
        }

        @JvmStatic
        fun createOutlineBrush(color: MColor?): ShapeBrush {
            return ShapeBrush(color, DrawPriority.SHAPE_OUTLINE)
        }

        @JvmStatic
        fun createSolidBrush(color: MColor?): ShapeBrush {
            return ShapeBrush(color, DrawPriority.SOLID_SHAPE)
        }

    }

}