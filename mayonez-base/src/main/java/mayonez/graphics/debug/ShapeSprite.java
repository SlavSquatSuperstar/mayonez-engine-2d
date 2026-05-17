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
public class ShapeSprite extends Component {

    // Component References
    private Collider collider;

    // Shape Draw Fields
    private final Shape shape;
    private ShapeBrush brush;

    /**
     * Create a new ShapeSprite that draws the given shape using the object's
     * transform.
     *
     * @param shape the shape to draw
     * @param color what color to draw the shape
     * @param fill  whether to fill the shape interior
     */
    public ShapeSprite(Shape shape, Color color, boolean fill) {
        this.shape = shape;
        if (fill) {
            brush = ShapeBrush.createSolidBrush(color);
        } else {
            brush = ShapeBrush.createOutlineBrush(color);
        }
    }

    /**
     * Create a new ShapeSprite that draws the object's collider.
     *
     * @param color the color to draw as
     * @param fill  whether to fill the shape interior
     */
    public ShapeSprite(Color color, boolean fill) {
        this(null, color, fill);
    }

    @Override
    protected void start() {
        collider = gameObject.getComponent(Collider.class);
    }

    @Override
    protected void debugRender() {
        var drawShape = (shape == null) ? getColliderShape() : getWorldShape();
        var drawBrush = brush.setZIndex(gameObject.getZIndex());
        if (brush.getFill()) {
            getScene().getDebugDraw().fillShape(drawShape, drawBrush);
        } else {
            getScene().getDebugDraw().drawShape(drawShape, drawBrush);
        }
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

    private Shape getWorldShape() {
        return shape.rotate(transform.getRotation(), null)
                .scale(transform.getScale(), null)
                .translate(transform.getPosition());
    }

    private Shape getColliderShape() {
        if (collider == null) {
            return new Rectangle(transform.getPosition(), transform.getScale());
        } else {
            return collider.getShape();
        }
    }

}
