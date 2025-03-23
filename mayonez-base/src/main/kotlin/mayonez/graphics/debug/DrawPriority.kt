package mayonez.graphics.debug

import mayonez.graphics.*

/**
 * Defines what type of geometric object a
 * [mayonez.graphics.debug.DebugShape] represents and which order to draw
 * it.
 *
 * @author SlavSquatSuperstar
 */
internal enum class DrawPriority(val zIndex: Int, val fill: Boolean) {

    /**
     * Solid shapes, drawn first.
     */
    SOLID_SHAPE(-5, true),

    /**
     * Shape outlines, drawn after solid shapes and before lines.
     */
    SHAPE_OUTLINE(5, false),

    /**
     * Lines, drawn after shapes and before points.
     */
    LINE(10, false),

    /**
     * Single points, drawn last.
     */
    POINT(15, true);

    internal fun createBrush(color: MColor?): ShapeBrush {
        return ShapeBrush(
            color ?: DebugDraw.DEFAULT_COLOR,
            this.fill, this.zIndex,
            if (this.fill) 0f else DebugDraw.DEFAULT_STROKE_SIZE
        )
    }

}