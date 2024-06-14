package mayonez.graphics.ui;

import mayonez.math.*;

/**
 * A rectangular axis-aligned bounding box for UI elements whose position and
 * dimensions can easily be manipulated.
 *
 * @author SlavSquatSuperstar
 */
// TODO clamp positive only
public class UIBounds {

    private Vec2 center, size;

    /**
     * Create a UIBounds from a center and size.
     *
     * @param center the center of the UI component
     * @param size   the size of the UI component
     */
    public UIBounds(Vec2 center, Vec2 size) {
        this.center = center;
        this.size = size;
    }

    /**
     * Create a UIBounds from an anchor point and size. For example,
     * if {@code anchor} is {@link Anchor#TOP_LEFT}, then {@code position}
     * will set the location of the top left corner.
     *
     * @param position the position of the anchor point
     * @param size     the size of the UI component
     * @param anchor   the direction anchor point
     */
    public UIBounds(Vec2 position, Vec2 size, Anchor anchor) {
        this.center = position.sub(size.mul(anchor.getDirection()));
        this.size = size;
    }

    // Center Getters

    public Vec2 getCenter() {
        return center;
    }

    public void setCenter(Vec2 center) {
        this.center = center;
    }

    public Vec2 getPosition(Anchor anchor) {
        return center.add(size.mul(anchor.getDirection()));
    }

    public void setPosition(Vec2 position, Anchor anchor) {
        center = position.sub(size.mul(anchor.getDirection()));
    }

    // Size Getters

    public Vec2 getSize() {
        return size;
    }

    public void setSize(Vec2 size) {
        this.size = size;
    }

    public float getWidth() {
        return size.x;
    }

    public void setWidth(float width) {
        size.x = width;
    }

    public float getHeight() {
        return size.y;
    }

    public void setHeight(float height) {
        size.y = height;
    }

}
