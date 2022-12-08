package mayonez.graphics.renderer

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
    @JvmField val zIndex: Int
) {
    // Use preset priority
    constructor(shape: MShape, color: MColor, fill: Boolean, priority: Priority) :
            this(shape, color, fill, priority.zIndex)

    internal constructor(shape: MShape, debugShape: DebugShape) :
            this(shape, debugShape.color, debugShape.fill, debugShape.zIndex)

    override fun toString(): String = "${shape.javaClass.simpleName} (fill = $fill, z = $zIndex)"

    /**
     * The order an object should be drawn. Higher priority objects will be drawn later to be more visible.
     */
    enum class Priority(val zIndex: Int) {
        /**
         * Solid shapes, drawn first.
         */
        FILL(-1),

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