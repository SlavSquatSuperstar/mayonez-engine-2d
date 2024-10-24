package mayonez.renderer;

import mayonez.graphics.debug.*;

import java.awt.*;
import java.util.List;

/**
 * The graphical layer of the scene that sends all drawable objects to the
 * correct renderer.
 *
 * @author SlavSquatSuperstar
 */
public abstract class RenderLayer {

    private final List<Renderer> renderers;
    private final DebugDraw debugDraw;

    protected RenderLayer(List<Renderer> renderers, DebugDraw debugDraw) {
        this.renderers = renderers;
        this.debugDraw = debugDraw;
    }

    // Renderer Methods

    /**
     * Redraws all objects in the scene.
     *
     * @param g2 the window's graphics object
     */
    public void render(Graphics2D g2) {
        for (var r : renderers) r.render(g2);
    }

    /**
     * Clears all objects from all renderers.
     */
    public void clear() {
        for (var r : renderers) r.clear();
    }

    /**
     * Submits a drawable object for rendering.
     *
     * @param r the renderable
     */
    public abstract void addRenderable(Renderable r);

    /**
     * Withdraws a drawable object from rendering.
     *
     * @param r the renderable
     */
    public abstract void removeRenderable(Renderable r);

    // Helper Methods

    /**
     * Gets the object that draws debug shapes.
     *
     * @return the debug draw instance
     */
    public DebugDraw getDebugDraw() {
        return debugDraw;
    }

}
