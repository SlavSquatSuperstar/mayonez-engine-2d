package mayonez.graphics.ui;

import mayonez.graphics.*;
import mayonez.graphics.renderer.gl.*;

/**
 * A visible user interface element that contains a renderable component in addition
 * to position and size.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public interface UIRenderableElement extends UIElement, GLRenderable {

    /**
     * Get the color of this UI element.
     *
     * @return the color
     */
    Color getColor();

    /**
     * Set the color of this UI element.
     *
     * @param color the color
     */
    void setColor(Color color);

}
