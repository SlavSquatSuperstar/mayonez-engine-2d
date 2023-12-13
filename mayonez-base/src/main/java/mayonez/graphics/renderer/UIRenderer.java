package mayonez.graphics.renderer;

/**
 * Draws user interface elements to the scene.
 *
 * @author SlavSquatSuperstar
 */
public interface UIRenderer extends Renderer {

    /**
     * Adds a UI element to the renderer.
     *
     * @param r the renderable
     */
    void addUIElement(Renderable r);

    /**
     * Removes a UI element from the renderer.
     *
     * @param r the renderable
     */
    void removeUIElement(Renderable r);

}
