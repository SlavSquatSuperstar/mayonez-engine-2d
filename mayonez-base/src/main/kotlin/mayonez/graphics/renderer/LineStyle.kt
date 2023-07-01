package mayonez.graphics.renderer

/**
 * Defines how to draw an [mayonez.math.shapes.Edge] (line) in a
 * [mayonez.graphics.renderer.DebugRenderer].
 *
 * @author SlavSquatSuperstar
 */
internal enum class LineStyle {
    /**
     * Draw a single line and set the thickness using glLineWidth() (does not
     * work on all platforms).
     */
    SINGLE,

    /** Draw each line as multiple adjacent lines to simulate stroke size. */
    MULTIPLE,

    /**
     * Draw each line using a thin quad (rectangle), using the width to
     * simulate stroke size.
     */
    QUADS
}