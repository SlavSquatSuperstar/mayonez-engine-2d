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

    private Anchor anchorDir;
    private Vec2 anchorPos, center, size;

    /**
     * Create a UIBounds from a center and size.
     *
     * @param center the center of the UI component
     * @param size   the size of the UI component
     */
    public UIBounds(Vec2 center, Vec2 size) {
        this.anchorDir = Anchor.CENTER;
        this.anchorPos = center;
        this.center = anchorPos;
        this.size = size;
    }

    /**
     * Create a UIBounds from an anchor point and size. For example,
     * if {@code anchor} is {@link Anchor#TOP_LEFT}, then {@code position}
     * will set the location of the top left corner.
     *
     * @param position the position of the anchor point
     * @param size     the size of the UI component
     * @param anchor   the direction of the anchor point
     */
    public UIBounds(Vec2 position, Vec2 size, Anchor anchor) {
        this.anchorDir = anchor;
        this.anchorPos = position;
        this.size = size;
        this.center = anchorPos.sub(size.mul(anchorDir.getDirection()));
    }

    // Anchor Getters

    public Anchor getAnchorDir() {
        return anchorDir;
    }

    public void setAnchorDir(Anchor anchorDir) {
        this.anchorDir = anchorDir;
        setAnchorPos(getPosition(anchorDir)); // Update anchor point
    }

    public Vec2 getAnchorPos() {
        return anchorPos;
    }

    public void setAnchorPos(Vec2 anchorPos) {
        this.anchorPos = anchorPos;
        recalculateCenter(anchorPos);
    }

    // Position Getters

    public Vec2 getCenter() {
        return center;
    }

    public void setCenter(Vec2 center) {
        this.center = center;
    }

    private void recalculateCenter(Vec2 anchorPos) {
        center = anchorPos.sub(size.mul(anchorDir.getDirection())); // Recalculate center
    }

    public Vec2 getPosition(Anchor anchor) {
        return center.add(size.mul(anchor.getDirection()));
    }

    void setPosition(Vec2 position, Anchor anchor) {
        center = position.sub(size.mul(anchor.getDirection()));
    }

    // Size Getters

    public Vec2 getSize() {
        return size;
    }

    public void setSize(Vec2 size) {
        this.size = size;
        recalculateCenter(anchorPos);
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
