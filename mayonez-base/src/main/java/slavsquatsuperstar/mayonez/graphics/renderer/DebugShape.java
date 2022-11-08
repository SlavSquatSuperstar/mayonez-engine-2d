package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.mayonez.physics.shapes.Shape;
import slavsquatsuperstar.mayonez.util.Color;

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
        zIndex = priority.zIndex;
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

        private final int zIndex;

        Priority(int zIndex) {
            this.zIndex = zIndex;
        }
    }

}
