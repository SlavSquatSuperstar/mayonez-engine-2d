package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;

import java.awt.*;

/**
 * A controllable behavior for a GameObject.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Script extends Component {

    // Common Getters

    /**
     * Provides a reference to the parent object's component of the given type.
     *
     * @return the component, if it exists
     */
    public <T extends Component> T getComponent(Class<T> cls) {
        return parent.getComponent(cls);
    }

    /**
     * Provides a reference to the parent object's {@link Collider2D} component.
     *
     * @return the collider, if it exists
     */
    public Collider2D getCollider() {
        return parent.getComponent(Collider2D.class);
    }

    /**
     * Provides a reference to the parent object's {@link Rigidbody2D} component.
     *
     * @return the rigidbody, if it exists
     */
    public Rigidbody2D getRigidbody() {
        return parent.getComponent(Rigidbody2D.class);
    }

    // Overrides

    @Override
    public final void render(Graphics2D g2) {} // Scripts shouldn't render any images

    @Override
    public Script setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }

    @Override
    public String toString() {
        return String.format("Script %s (%s)", getClass().isAnonymousClass() ?
                "Script" : getClass().getSimpleName(), parent.name);
    }
}
