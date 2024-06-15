package mayonez.renderer;

import mayonez.*;
import mayonez.graphics.camera.*;

import java.awt.Graphics2D;

/**
 * Draws objects or other program information to the screen.
 *
 * @author SlavSquatSuperstar
 */
public interface Renderer {

    // Game Loop Methods

    /**
     * Removes all objects from the renderer and frees any memory resources.
     */
    void clear();

    /**
     * Redraws all objects in the renderer.
     *
     * @param g2 a {@link java.awt.Graphics2D} object for the AWT engine
     */
    void render(Graphics2D g2);

    // Camera Methods

    /**
     * Get the current scene's camera.
     *
     * @return the camera
     */
    default Viewport getViewport() {
        return SceneManager.getCurrentScene().getCamera();
    }

}