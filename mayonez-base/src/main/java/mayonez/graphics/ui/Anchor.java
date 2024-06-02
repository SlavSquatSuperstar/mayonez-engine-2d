package mayonez.graphics.ui;

import mayonez.math.*;

/**
 * Which direction to set the origin of a {@link mayonez.graphics.ui.UIElement}.
 * A UI element can be scaled while the anchor point stays the same.
 *
 * @author SlavSquatSuperstar
 */
public enum Anchor {

    /**
     * Use the center point as the origin.
     */
    CENTER(new Vec2(0f, 0f)),

    /**
     * Use the top left corner as the origin.
     */
    TOP_LEFT(new Vec2(-0.5f, 0.5f)),

    /**
     * Use the center left point as the origin.
     */
    LEFT(new Vec2(-0.5f, 0f)),

    /**
     * Use the bottom left corner as the origin.
     */
    BOTTOM_LEFT(new Vec2(-0.5f, -0.5f)),

    /**
     * Use the bottom center point as the origin.
     */
    BOTTOM(new Vec2(0f, -0.5f)),

    /**
     * Use the bottom right point as the origin.
     */
    BOTTOM_RIGHT(new Vec2(0.5f, -0.5f)),

    /**
     * Use the center right point as the origin.
     */
    RIGHT(new Vec2(0.5f, 0f)),

    /**
     * Use the top right point as the origin.
     */
    TOP_RIGHT(new Vec2(0.5f, 0.5f)),

    /**
     * Use the top center point as the origin.
     */
    TOP(new Vec2(0f, 0.5f));

    private final Vec2 direction;

    Anchor(Vec2 direction) {
        this.direction = direction;
    }

    /**
     * Get the direction of the anchor in relation to the UI element's center point.
     *
     * @return the direction
     */
    Vec2 getDirection() {
        return direction;
    }
}
