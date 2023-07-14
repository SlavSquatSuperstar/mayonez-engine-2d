package mayonez.graphics.debug;


import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.shapes.*;
import mayonez.physics.colliders.*;

/**
 * Draws an object's {@link mayonez.physics.colliders.Collider} to the screen.
 *
 * @author SlavSquatSuperstar
 */
public class ShapeSprite extends Component {

    private Collider collider;
    private Shape shape;

    public Color color;
    public boolean fill;

    public ShapeSprite(Color color, boolean fill) {
        this.color = color;
        this.fill = fill;
    }

    @Override
    public void start() {
        collider = gameObject.getComponent(Collider.class);
        if (collider == null) this.setEnabled(false);
        shape = getColliderShape();
    }

    @Override
    public void debugRender() {
        if (fill) {
            var shapeBrush = ShapeBrush.createSolidBrush(color).setZIndex(gameObject.getZIndex());
            getScene().getDebugDraw().fillShape(shape, shapeBrush);
        } else {
            var shapeBrush = ShapeBrush.createOutlineBrush(color).setZIndex(gameObject.getZIndex());
            getScene().getDebugDraw().drawShape(shape, shapeBrush);
        }
    }

    @Override
    public void update(float dt) {
        shape = getColliderShape();
    }

    // Shape Methods

    public Shape getColliderShape() {
        if (collider == null) {
            return new Rectangle(transform.getPosition(), transform.getScale());
        } else {
            return collider.transformToWorld();
        }
    }

}
