package mayonez.graphics.renderer.debug

import mayonez.util.MColor
import mayonez.util.MShape

/**
 * Passes debug shape and color information to a [DebugRenderer].
 *
 * @author SlavSquatSuperstar
 */
class DebugShape(
    @JvmField val shape: MShape,
    @JvmField val color: MColor,
    @JvmField val fill: Boolean,
    private val priority: Priority
) {
    @JvmField
    val zIndex: Int = priority.zIndex

    internal constructor(shape: MShape, debugShape: DebugShape) :
            this(shape, debugShape.color, debugShape.fill, debugShape.priority)

    /**
     * The order an object should be drawn. Higher priority objects will be drawn later to be more visible.
     */
    enum class Priority(val zIndex: Int) {
        /**
         * Solid shapes, drawn first.
         */
        FILL(0),

        /**
         * Shape boundaries, after solid shapes and before lines.
         */
        SHAPE(1),

        /**
         * Lines, drawn after shapes and before points.
         */
        LINE(2),

        /**
         * Single points, drawn last.
         */
        POINT(3);
    }
}