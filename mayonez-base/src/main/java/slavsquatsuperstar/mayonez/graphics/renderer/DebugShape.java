package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.mayonez.physics.shapes.Shape;
import slavsquatsuperstar.util.Color;

/**
 * Passes debug shape and color information to a {@link DebugRenderer}.
 *
 * @author SlavSquatSuperstar
 */
public class DebugShape {

    final Shape shape;
    final Color color;
    final boolean fill;
    final Priority priority;
    final int zIndex;

    public DebugShape(Shape shape, Color color, boolean fill, Priority priority) {
        this.shape = shape;
        this.color = color;
        this.fill = fill;
        this.priority = priority;
        zIndex = priority.ordinal();
    }

    DebugShape(Shape shape, DebugShape debugShape) {
        this(shape, debugShape.color, debugShape.fill, debugShape.priority);
    }

    /**
     * The order an object should be drawn. Higher priority objects will be drawn later to be more visible.
     */
    public enum Priority {
        /**
         * Solid shapes, drawn first.
         */
        FILL,
        /**
         * Shape boundaries, after solid shapes and before lines.
         */
        SHAPE,
        /**
         * Lines, drawn after shapes and before points.
         */
        LINE,
        /**
         * Single points, drawn last.
         */
        POINT
    }

}
