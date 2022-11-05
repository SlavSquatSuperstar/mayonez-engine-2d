package slavsquatsuperstar.mayonez.graphics.renderer;

/**
 * Draws debug information such as colliders and vectors into the screen.
 *
 * @author SlavSquatSuperstar
 */
public interface DebugRenderer extends Renderer {

    // DebugDraw Methods
    void addShape(DebugShape shape);
}
