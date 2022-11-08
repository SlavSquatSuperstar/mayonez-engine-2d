package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.Collider;

import java.awt.*;

/**
 * A controllable and reusable behavior for a {@link GameObject}.
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
        return gameObject.getComponent(cls);
    }

    /**
     * Provides a reference to the parent object's {@link Collider} component.
     *
     * @return the collider, if it exists
     */
    public Collider getCollider() {
        return gameObject.getComponent(Collider.class);
    }

    /**
     * Provides a reference to the parent object's {@link Rigidbody} component.
     *
     * @return the rigidbody, if it exists
     */
    public Rigidbody getRigidbody() {
        return gameObject.getComponent(Rigidbody.class);
    }

    // Overrides

    @Override
    public final void render(Graphics2D g2) {
    } // Scripts shouldn't render any images

    @Override
    public String toString() {
        return String.format(
                "%s (%s)",
                getClass().isAnonymousClass() ? "Script" : getClass().getSimpleName(),
                gameObject == null ? "<No Parent>" : gameObject.name
        );
    }
}
