package mayonez.graphics.renderer;

import mayonez.graphics.sprites.*;
import mayonez.math.*;

/**
 * Draws game objects and sprites in a {@link mayonez.Scene} to the screen.
 *
 * @author SlavSquatSuperstar
 */
public interface SceneRenderer extends Renderer {

    /**
     * Sets the background color or image for the scene.
     *
     * @param background the background sprite
     */
    void setBackground(Sprite background);

    /**
     * Updates screen size and scale to the new scene.
     *
     * @param sceneSize  the scene dimensions
     * @param sceneScale the scene scale
     */
    void setSceneSize(Vec2 sceneSize, float sceneScale);

    /**
     * Submits a drawable object for rendering.
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
