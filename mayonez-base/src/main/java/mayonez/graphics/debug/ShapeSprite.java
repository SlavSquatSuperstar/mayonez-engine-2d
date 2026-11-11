package mayonez.graphics.debug;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.physics.colliders.*;

/**
 * Draws the parent node's {@link Collider} to the screen.
 *
 * @author SlavSquatSuperstar
 */
public class ShapeSprite extends Node {

    private Collider collider;
    private ShapeBrush brush;

    /**
     * Create a new ShapeSprite that draws the object's collider.
     *
     * @param color the color to draw as
     * @param fill  whether to fill the shape interior
     */
    public ShapeSprite(Color color, boolean fill) {
        if (fill) {
            brush = ShapeBrush.createSolidBrush(color);
        } else {
            brush = ShapeBrush.createOutlineBrush(color);
        }
    }

    // Node Methods

    @Override
    protected void start() {
        collider = getSibling(Collider.class);
    }

    @Override
    protected void debugRender() {
        var colliderShape = (collider == null)
                ? getGlobalTransform().getBoundingRectangle()
                : collider.getShape();
        var drawBrush = brush.setZIndex(getZIndex());
        getScene().getDebugDraw().drawShape(colliderShape, drawBrush);
    }

    // Getter and Setter Methods

    /**
     * Get the brush's color.
     *
     * @return the color
     */
    public Color getColor() {
        return brush.color();
    }

    /**
     * Set the brush's color.
     *
     * @param color the color
     */
    public void setColor(Color color) {
        brush = brush.setColor(color);
    }

    /**
     * Get the brush's fill.
     *
     * @return if filled
     */
    public boolean isFill() {
        return brush.fill();
    }

    /**
     * Set the brush's fill.
     *
     * @param fill if filled
     */
    public void setFill(boolean fill) {
        brush = brush.setFill(fill);
    }

    /**
     * Get the brush's stroke size.
     *
     * @return the stroke size, in pixels
     */
    public float getStrokeSize() {
        return brush.strokeSize();
    }

    /**
     * Set the brush's stroke size.
     *
     * @param strokeSize the stroke size, in pixels
     */
    public void setStrokeSize(float strokeSize) {
        brush = brush.setStrokeSize(strokeSize);
    }

}
