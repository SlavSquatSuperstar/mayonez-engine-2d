package mayonez.graphics.renderer;

import mayonez.graphics.*;

import java.awt.*;

/**
 * An object or component that can be drawn to the screen using the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public interface JRenderable extends Renderable {

    // Renderer Methods

    /**
     * Draw this object to the screen.
     *
     * @param g2 the window's graphics object
     */
    void render(Graphics2D g2);

}
