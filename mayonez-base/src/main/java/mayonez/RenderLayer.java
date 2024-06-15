package mayonez;

import mayonez.graphics.debug.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import mayonez.renderer.*;

import java.awt.*;
import java.util.List;

/**
 * The graphical layer of the scene that contains all the renderers and defines
 * which renderers receive which object.
 *
 * @author SlavSquatSuperstar
 */
class RenderLayer {

    // Renderers
    private final SceneRenderer sceneRenderer;
    private final UIRenderer uiRenderer;
    private final DebugDraw debugDraw;
    private final List<Renderer> renderers;

    RenderLayer(Sprite background, Vec2 sceneSize, float sceneScale) {
        // Scene
        sceneRenderer = RendererFactory.createSceneRenderer();
        sceneRenderer.setBackground(background, sceneSize, sceneScale);

        // Debug
        DebugRenderer debugRenderer = (DebugRenderer) sceneRenderer;
        debugDraw = new DebugDraw(sceneScale, debugRenderer);

        // UI
        uiRenderer = RendererFactory.createUIRenderer();

        // List
        renderers = List.of(sceneRenderer, uiRenderer);
    }

    // Renderer Methods

    /**
     * Redraw all objects in the scene.
     *
     * @param g2 the window's graphics object
     */
    void render(Graphics2D g2) {
        for (var r : renderers) r.render(g2);
    }

    /**
     * Clears all objects from all renderers.
     */
    void clear() {
        for (var r : renderers) r.clear();
    }

    /**
     * Submits a drawable object for rendering.
     *
     * @param r the renderable
     */
    void addRenderable(Renderable r) {
        if (r instanceof UIRenderableElement e) uiRenderer.addUIElement(e);
        else sceneRenderer.addRenderable(r);
    }

    /**
     * Withdraws a drawable object from rendering.
     *
     * @param r the renderable
     */
    void removeRenderable(Renderable r) {
        if (r instanceof UIRenderableElement e) uiRenderer.removeUIElement(e);
        else sceneRenderer.removeRenderable(r);
    }

    // Helper Methods

    DebugDraw getDebugDraw() {
        return debugDraw;
    }

}
