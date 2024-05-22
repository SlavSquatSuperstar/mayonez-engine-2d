package mayonez.graphics.ui;

import mayonez.graphics.*;
import mayonez.graphics.renderer.gl.*;
import mayonez.math.*;

/**
 * A basic user interface element with position and size but no renderable components.
 * This component may still be a container for renderable elements.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public interface UIElement extends GLRenderable {

    /**
     * Get the position of this UI element's top left corner.
     *
     * @return the position
     */
    Vec2 getPosition();

    /**
     * Set the position of this UI element's top left corner.
     *
     * @param position the position
     */
    void setPosition(Vec2 position);

    /**
     * Get the dimensions of this UI element.
     *
     * @return the size
     */
    Vec2 getSize();

    /**
     * Set the dimensions of this UI element.
     *
     * @param size the size
     */
    void setSize(Vec2 size);

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
