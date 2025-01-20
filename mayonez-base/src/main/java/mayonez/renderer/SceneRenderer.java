package mayonez.renderer;

/**
 * Draws game objects and sprites in a {@link mayonez.Scene} to the screen.
 *
 * @author SlavSquatSuperstar
 */
public interface SceneRenderer extends Renderer {

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
