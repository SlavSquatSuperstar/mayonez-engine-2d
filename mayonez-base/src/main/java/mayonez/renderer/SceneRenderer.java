package mayonez.renderer;

import mayonez.graphics.sprites.*;
import mayonez.math.*;

/**
 * Draws game objects and sprites in a {@link mayonez.Scene} to the screen.
 *
 * @author SlavSquatSuperstar
 */
public interface SceneRenderer extends Renderer {

    /**
     * Sets the background color or image, and sets the background
     * size to match the new scene.
     *
     * @param background the background sprite
     * @param sceneSize  the scene dimensions
     * @param sceneScale the scene scale
     */
    void setBackground(Sprite background, Vec2 sceneSize, float sceneScale);

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
