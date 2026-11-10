package mayonez.graphics.debug;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.math.shapes.*;
import mayonez.physics.colliders.*;

/**
 * Draws an object's {@link mayonez.physics.colliders.Collider} to the screen.
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

    @Override
    protected void start() {
        collider = getSibling(Collider.class);
    }

    @Override
    protected void debugRender() {
        var colliderShape = (collider == null)
                ? getGlobalTransform().getBoundingRectangle()
                : collider.getShape();
        var drawBrush = brush.setZIndex(getParent().getZIndex());
        getScene().getDebugDraw().drawShape(colliderShape, drawBrush);
    }

    // Getter and Setter Methods

    public Color getColor() {
        return brush.getColor();
    }

    public void setColor(Color color) {
        brush = brush.setColor(color);
    }

    public boolean isFill() {
        return brush.getFill();
    }

    public void setFill(boolean fill) {
        brush = brush.setFill(fill);
    }

    public float getStrokeSize() {
        return brush.getStrokeSize();
    }

    public void setStrokeSize(float strokeSize) {
        brush = brush.setStrokeSize(strokeSize);
    }

    // Shape Helper Methods

    private Shape getColliderShape() {
        if (collider == null) {
            return getGlobalTransform().getBoundingRectangle();
        } else {
            return collider.getShape();
        }
    }

}
