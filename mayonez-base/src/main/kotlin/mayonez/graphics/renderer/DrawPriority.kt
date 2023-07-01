package mayonez.graphics.renderer

enum class DrawPriority(val zIndex: Int) {

    /** Solid shapes, drawn first. */
    SOLID_SHAPE(-5),

    /** Shape outlines, after solid shapes and before lines. */
    SHAPE_OUTLINE(5),

    /** Lines, drawn after shapes and before points. */
    LINE(8),

    /** Single points, drawn last. */
    POINT(10)

}