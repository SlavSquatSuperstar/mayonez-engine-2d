package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.mayonez.physics.shapes.Shape;

import java.awt.*;

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

    public DebugShape(Shape shape, Color color, boolean fill, Priority priority) {
        this.shape = shape;
        this.color = color;
        this.fill = fill;
        this.priority = priority;
    }

    /**
     * The order an object should be drawn. Higher priority objects will be drawn later to be more visible.
     */
    public enum Priority {
        SHAPE, LINE, POINT
    }

}
