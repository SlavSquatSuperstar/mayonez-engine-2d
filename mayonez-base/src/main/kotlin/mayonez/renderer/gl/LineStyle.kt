package mayonez.renderer.gl

/**
 * Defines how to draw an [mayonez.math.shapes.Edge] (line) in a
 * [mayonez.renderer.DebugRenderer].
 *
 * @author SlavSquatSuperstar
 */
internal enum class LineStyle(val fill: Boolean) {

    /** Draw a single line and set the thickness using glLineWidth(). */
    @Deprecated("Not supported on some platforms.")
    SINGLE(false),

    /** Draw each line as multiple adjacent lines to simulate stroke size. */
    @Deprecated("Drawn lines are too transparent.")
    MULTIPLE(false),

    /**
     * Draw each line using a thin quad (rectangle), using the width to
     * simulate stroke size.
     */
    QUADS(true)

}