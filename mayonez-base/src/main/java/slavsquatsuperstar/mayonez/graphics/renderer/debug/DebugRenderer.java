package slavsquatsuperstar.mayonez.graphics.renderer.debug;

import slavsquatsuperstar.mayonez.graphics.renderer.Renderer;

/**
 * Draws debug information such as colliders and geometric objects into the screen.
 *
 * @author SlavSquatSuperstar
 */
public interface DebugRenderer extends Renderer {
    // Debug Draw Methods
    void addShape(DebugShape shape);
}
