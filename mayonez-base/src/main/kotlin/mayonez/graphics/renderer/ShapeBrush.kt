package mayonez.graphics.renderer

import mayonez.*
import mayonez.util.*

/**
 * Defines what parameters to use while drawing a
 * [mayonez.graphics.DebugShape].
 *
 * @author SlavSquatSuperstar
 */
internal data class ShapeBrush constructor(
    internal val color: MColor,
    internal val fill: Boolean,
    internal val zIndex: Int,
    internal val strokeSize: Float
) {

//    constructor(color: MColor, fill: Boolean, priority: DrawPriority) :
//            this(color, fill, priority.zIndex)

    constructor(color: MColor, priority: DrawPriority) :
            this(color, priority.fill, priority.zIndex, DebugDraw.DEFAULT_STROKE_SIZE)

    constructor(color: MColor, style: LineStyle) :
            this(color, style.fill, DrawPriority.LINE.zIndex, DebugDraw.DEFAULT_STROKE_SIZE)

//    companion object {
//
//        // Factory Methods
//
//        internal fun createLineBrush(color: MColor, style: LineStyle): ShapeBrush {
//            return ShapeBrush(color, style.fill, DrawPriority.LINE)
//        }
//
//        internal fun createOutlineBrush(color: MColor, strokeSize: Float): ShapeBrush {
//            return ShapeBrush(color, false, DrawPriority.SHAPE_OUTLINE)
//        }
//
//        internal fun createSolidBrush(color: MColor): ShapeBrush {
//            return ShapeBrush(color, true, DrawPriority.SOLID_SHAPE)
//        }
//
//    }

}