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
    private Shape shape;
    private Color color;
    private boolean fill;

    /**
     * Create a new ShapeSprite that draws the object's collider.
     *
     * @param color the color to draw as
     * @param fill  whether to fill the shape interior
     */
    public ShapeSprite(Color color, boolean fill) {
        super(UpdateOrder.RENDER);
        this.color = color;
        this.fill = fill;
    }

    @Override
    protected void start() {
        collider = gameObject.getComponent(Collider.class);
        shape = getColliderShape();
    }

    @Override
    protected void debugRender() {
        shape = getColliderShape();

        if (fill) {
            var shapeBrush = ShapeBrush.createSolidBrush(color).setZIndex(gameObject.getZIndex());
            getScene().getDebugDraw().fillShape(shape, shapeBrush);
        } else {
            var shapeBrush = ShapeBrush.createOutlineBrush(color).setZIndex(gameObject.getZIndex());
            getScene().getDebugDraw().drawShape(shape, shapeBrush);
        }
    }

    // Getter and Setter Methods

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    // Shape Helper Methods

    private Shape getColliderShape() {
        if (collider == null) {
            return new Rectangle(transform.getPosition(), transform.getScale());
        } else {
            return collider.getShape();
        }
    }

}
