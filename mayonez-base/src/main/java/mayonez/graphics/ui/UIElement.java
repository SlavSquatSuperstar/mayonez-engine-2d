package mayonez.graphics.ui;

import mayonez.math.*;

/**
 * A basic user interface element with position and size but no renderable components.
 * This component may still be a container for renderable elements.
 *
 * @author SlavSquatSuperstar
 */
public interface UIElement {

    /**
     * Get the position of this UI element's anchor point.
     *
     * @return the position
     */
    Vec2 getPosition();

    /**
     * Set the position of this UI element's anchor point while keeping
     * the size the same.
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
     * Set the dimensions of this UI element while keeping the anchor point
     * the same.
     *
     * @param size the size
     */
    void setSize(Vec2 size);

    /**
     * Get the anchor direction for this UI element. Defaults to center.
     *
     * @return the anchor direction
     */
    default Anchor getAnchor() {
        return Anchor.CENTER;
    }

    /**
     * Set the anchor direction for this UI element and update the anchor point
     * to reflect the new direction.
     *
     * @param anchor the direction to anchor
     */
    default void setAnchor(Anchor anchor) {
    }

}
