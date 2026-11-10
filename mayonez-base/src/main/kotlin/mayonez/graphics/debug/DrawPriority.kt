package mayonez.graphics.debug

import mayonez.graphics.*

/**
 * Defines the default brush characteristics for a [DebugShape].
 *
 * @author SlavSquatSuperstar
 */
internal enum class DrawPriority(
    private val zIndex: Int, private val fill: Boolean
) {

    /** Solid shapes, drawn first. */
    SOLID_SHAPE(-5, true),

    /** Shape outlines, drawn after solid shapes and before lines. */
    SHAPE_OUTLINE(5, false),

    /** Lines, drawn after shapes and before points. */
    LINE(10, false),

    /** Single points, drawn last. */
    POINT(15, true);

    /**
     * Create a [ShapeBrush] for the given color with pre-defined fill,
     * z-index, and stroke size.
     *
     * @param color the brush color
     * @return the brush
     */
    internal fun createBrush(color: MColor): ShapeBrush {
        return ShapeBrush(
            color, this.fill, this.zIndex,
            if (this.fill) 0f else ShapeBrush.DEFAULT_STROKE_SIZE
        )
    }

}