package mayonez.graphics.renderer

import mayonez.*
import mayonez.util.*

/**
 * Defines what parameters to use while drawing a
 * [mayonez.graphics.DebugShape].
 *
 * @author SlavSquatSuperstar
 */
internal data class ShapeBrush(
    internal val color: MColor,
    internal val fill: Boolean,
    internal val zIndex: Int,
    internal val strokeSize: Float
) {

    constructor(color: MColor, fill: Boolean, priority: DrawPriority) :
            this(color, fill, priority.zIndex, DebugDraw.DEFAULT_STROKE_SIZE)

    companion object {

        internal fun createLineBrush(color: MColor, style: LineStyle): ShapeBrush {
            return ShapeBrush(color, style.fill, DrawPriority.LINE)
        }

        internal fun createBoundaryBrush(color: MColor): ShapeBrush {
            return ShapeBrush(color, true, DrawPriority.LINE)
        }

        internal fun createSolidBrush(color: MColor): ShapeBrush {
            return ShapeBrush(color, true, DrawPriority.LINE)
        }

    }

}