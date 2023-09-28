package mayonez.graphics.renderer;

import mayonez.*;

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
     * Updates screen size and background to the new scene.
     *
     * @param newScene the new scene
     */
    void setScene(Scene newScene);

}
