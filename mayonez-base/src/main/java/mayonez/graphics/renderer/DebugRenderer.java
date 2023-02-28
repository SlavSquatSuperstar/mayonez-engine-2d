package mayonez.graphics.renderer;

import mayonez.graphics.*;

/**
 * Draws debug information such as colliders and geometric objects into the screen.
 *
 * @author SlavSquatSuperstar
 */
public interface DebugRenderer extends Renderer {

    // Debug Draw Methods

    /**
     * Submits a {@link DebugShape} for rendering.
     *
     * @param shape the shape
     */
    void addShape(DebugShape shape);
}
