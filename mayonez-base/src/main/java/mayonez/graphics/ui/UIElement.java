package mayonez.graphics.ui;

import mayonez.*;
import mayonez.math.*;

/**
 * A basic user interface element with position and size but no renderable components.
 * This component may be used as a container for renderable elements.
 *
 * @author SlavSquatSuperstar
 */
public abstract class UIElement extends Script {

    public UIElement() {
        super(UpdateOrder.RENDER);
    }

    /**
     * Get the position of this UI element's anchor point.
     *
     * @return the position
     */
    public abstract Vec2 getPosition();

    /**
     * Set the position of this UI element's anchor point while keeping
     * the size the same.
     *
     * @param position the position
     */
    public abstract void setPosition(Vec2 position);

    /**
     * Get the dimensions of this UI element.
     *
     * @return the size
     */
    public abstract Vec2 getSize();

    /**
     * Set the dimensions of this UI element while keeping the anchor point
     * the same.
     *
     * @param size the size
     */
    public abstract void setSize(Vec2 size);

    /**
     * Get the anchor direction for this UI element. Defaults to center.
     *
     * @return the anchor direction
     */
    public Anchor getAnchor() {
        return Anchor.CENTER;
    }

    /**
     * Set the anchor direction for this UI element and update the anchor point
     * to reflect the new direction.
     *
     * @param anchor the direction to anchor
     */
    public void setAnchor(Anchor anchor) {
    }

}
