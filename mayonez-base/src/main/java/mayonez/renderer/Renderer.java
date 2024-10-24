package mayonez.renderer;

import java.awt.*;

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
     * Get the current viewport into the window.
     *
     * @return the viewport
     */
    Viewport getViewport();

}
