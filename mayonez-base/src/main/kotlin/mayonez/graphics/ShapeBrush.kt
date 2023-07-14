package mayonez.graphics

import mayonez.*
import mayonez.graphics.renderer.*
import mayonez.util.*

/**
 * Defines what parameters to use while drawing a
 * [mayonez.graphics.DebugShape].
 *
 * @author SlavSquatSuperstar
 */
class ShapeBrush internal constructor(
    color: MColor?,
    internal val fill: Boolean,
    zIndex: Int,
    strokeSize: Float
) {

    private constructor(color: MColor?, priority: DrawPriority) :
            this(color, priority.fill, priority.zIndex, DebugDraw.DEFAULT_STROKE_SIZE)

    // Properties
    internal val color: MColor = color ?: DebugDraw.DEFAULT_COLOR

    internal var zIndex: Int = zIndex
        private set

    internal var strokeSize: Float = strokeSize
        private set

    // Mutators

    fun setZIndex(zIndex: Int): ShapeBrush {
        this.zIndex = zIndex
        return this
    }

    fun setStrokeSize(strokeSize: Float): ShapeBrush {
        this.strokeSize = strokeSize
        return this
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