package mayonez.graphics.sprites;


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
        getScene().getDebugDraw().setZIndex(gameObject.getZIndex());
        if (fill) {
            getScene().getDebugDraw().fillShape(shape, color);
        } else {
            getScene().getDebugDraw().drawShape(shape, color);
        }
        getScene().getDebugDraw().resetZIndex();
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
