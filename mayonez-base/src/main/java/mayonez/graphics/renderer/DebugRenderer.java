package mayonez.graphics.renderer;

import mayonez.graphics.debug.*;

/**
 * Draws debug information such as colliders and geometric objects into the screen.
 *
 * @author SlavSquatSuperstar
 */
public interface DebugRenderer extends Renderer {

    /**
     * Submits a {@link mayonez.graphics.debug.DebugShape} for rendering.
     *
     * @param shape the shape
     */
    void addShape(DebugShape shape);

}
