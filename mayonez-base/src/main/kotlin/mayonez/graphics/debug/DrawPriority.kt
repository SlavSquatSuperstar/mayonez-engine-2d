package mayonez.graphics.debug

/**
 * Defines what type of geometric object a
 * [mayonez.graphics.debug.DebugShape] represents and which order to draw
 * it.
 *
 * @author SlavSquatSuperstar
 */
internal enum class DrawPriority(internal val zIndex: Int, internal val fill: Boolean) {

    /** Solid shapes, drawn first. */
    SOLID_SHAPE(-5, true),

    /** Shape outlines, after solid shapes and before lines. */
    SHAPE_OUTLINE(5, false),

    /** Lines, drawn after shapes and before points. */
    LINE(8, false),

    /** Single points, drawn last. */
    POINT(10, true)

}