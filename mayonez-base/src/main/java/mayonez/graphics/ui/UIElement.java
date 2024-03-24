package mayonez.graphics.ui;

import mayonez.graphics.*;
import mayonez.graphics.renderer.gl.*;

/**
 * A basic user interface element.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public interface UIElement extends GLRenderable {

    /**
     * Get the anchor point for this UI element.
     *
     * @return the anchor direction
     */
    default Anchor getAnchor() {
        return Anchor.CENTER;
    }

    /**
     * Set the anchor point for this UI element.
     *
     * @param anchor the direction to anchor
     */
    default void setAnchor(Anchor anchor) {
    }

}
