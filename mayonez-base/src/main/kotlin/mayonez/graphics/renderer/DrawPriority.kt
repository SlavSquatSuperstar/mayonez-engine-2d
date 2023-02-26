package mayonez.graphics.renderer

enum class DrawPriority(val zIndex: Int) {

    /** Solid shapes, drawn first. */
    FILL(-5),

    /** Shape boundaries, after solid shapes and before lines. */
    SHAPE(5),

    /** Lines, drawn after shapes and before points. */
    LINE(8),

    /** Single points, drawn last. */
    POINT(10)

}