package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;

import java.awt.*;

/**
 * Draws objects in a scene to the screen.
 *
 * @author SlavSquatSuperstar
 */
public sealed abstract class Renderer permits JRenderer, GLRenderer {

    public abstract void render(Graphics2D g2);

    /**
     * Submit a {@link GameObject} for rendering.
     *
     * @param o the game object
     */
    public abstract void addObject(GameObject o);

    /**
     * Remove a {@link GameObject} from the renderer.
     *
     * @param o the game object
     */
    public abstract void removeObject(GameObject o);

    public abstract void setScene(Scene newScene);

}
