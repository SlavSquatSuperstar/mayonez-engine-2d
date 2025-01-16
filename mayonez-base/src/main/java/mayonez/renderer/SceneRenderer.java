package mayonez.renderer;

import mayonez.graphics.*;

/**
 * Draws game objects and sprites in a {@link mayonez.Scene} to the screen.
 *
 * @author SlavSquatSuperstar
 */
public interface SceneRenderer extends Renderer {

    /**
     * Sets the background color of the renderer.
     *
     * @param backgroundColor the background color
     */
    void setBackgroundColor(Color backgroundColor);

    /**
     * Adds a drawable object to the render.
     *
     * @param r the renderable
     */
    void addRenderable(Renderable r);

    /**
     * Removes a drawable object from the renderer.
     *
     * @param r the renderable
     */
    void removeRenderable(Renderable r);

}
