package mayonez.graphics.renderer;

import mayonez.GameObject;
import mayonez.Scene;

/**
 * Draws game objects and sprites in a {@link mayonez.Scene} to the screen.
 *
 * @author SlavSquatSuperstar
 */
public interface SceneRenderer extends Renderer {

    /**
     * Submits a {@link mayonez.GameObject} for rendering.
     *
     * @param o the game object
     */
    void addObject(GameObject o);

    /**
     * Removes a {@link mayonez.GameObject} from the renderer.
     *
     * @param o the game object
     */
    void removeObject(GameObject o);

    /**
     * Clears this renderer and submits all objects from the given {@link mayonez.Scene} for rendering.
     *
     * @param newScene a scene
     */
    void setScene(Scene newScene);

}
