package mayonez.graphics;

import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;

import java.awt.*;

/**
 * An object or component that can be drawn to the screen.
 */
@UsesEngine(EngineType.AWT)
public interface Renderable {
    /**
     * Draw this object to the screen.
     *
     * @param g2 the window's graphics object
     */
    void render(Graphics2D g2);

    int getZIndex();
}
