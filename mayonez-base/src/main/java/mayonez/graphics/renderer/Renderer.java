package mayonez.graphics.renderer;

import mayonez.*;
import mayonez.graphics.camera.*;

import java.awt.*;

/**
 * Draws objects or other program information to the screen.
 *
 * @author SlavSquatSuperstar
 */
public interface Renderer {

    // Game Loop Methods

    /**
     * Removes all objects from the renderer and clears all data.
     */
    void clear();

    /**
     * Redraws all objects in the renderer.
     *
     * @param g2 a {@link java.awt.Graphics2D} object for the AWT engine
     */
    void render(Graphics2D g2);

    // Helper Methods

    /**
     * Get the current scene's camera.
     *
     * @return the camera
     */
    default Camera getCamera() {
        return SceneManager.getCurrentScene().getCamera();
    }

}
