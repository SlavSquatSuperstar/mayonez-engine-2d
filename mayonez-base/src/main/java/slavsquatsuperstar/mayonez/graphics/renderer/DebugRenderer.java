package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;

import java.awt.*;

/**
 * Draws debug information such as colliders and vectors into the screen.
 *
 * @author SlavSquatSuperstar
 */
public abstract class DebugRenderer extends Renderer {

    protected final float STROKE_SIZE = 2f;
    protected final Stroke STROKE = new BasicStroke(STROKE_SIZE);

    // DebugDraw Methods
    public abstract void addShape(DebugShape shape);

    // GameObject Methods (unused)
    @Override
    public final void addObject(GameObject o) {
    }

    @Override
    public final void removeObject(GameObject o) {
    }

    @Override
    public final void setScene(Scene newScene) {
    }


}
