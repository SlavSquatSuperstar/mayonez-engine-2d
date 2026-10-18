package mayonez.graphics.ui;

import mayonez.*;
import mayonez.math.*;

/**
 * A basic user interface element with position and size but no renderable
 * components. Components may be used as a container for other elements. An
 * element may also be stored in a {@link UIContainer}, which is also its
 * parent node.
 *
 * @author SlavSquatSuperstar
 */
public abstract class UIElement extends Node {

    private UIContainer container;

    // Node Methods

    @Override
    protected void start() {
        container = null;
    }

    @Override
    protected void onDestroy() {
        container = null;
    }

    // UI Element Properties

    /**
     * This element's parent {@link UIContainer}.
     *
     * @return the container, or null
     */
    public UIContainer getContainer() {
        return container;
    }

    /**
     * Set this element's parent {@link UIContainer}.
     *
     * @param container the container, or null
     */
    void setContainer(UIContainer container) {
        this.container = container;
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
