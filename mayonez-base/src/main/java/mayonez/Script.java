package mayonez;

import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import org.jspecify.annotations.Nullable;

/**
 * A controllable and reusable behavior for a {@link mayonez.GameObject}.
 * <p>
 * See {@link mayonez.GameObject} and {@link mayonez.Script} for more information.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Script extends Component {

    public Script() {
        super(UpdateOrder.SCRIPT);
    }

    public Script(int updateOrder) {
        super(updateOrder);
    }

    // Component Getters

    /**
     * Provides a reference to the parent object's {@link Collider} component.
     *
     * @return the collider, if it exists
     */
    protected @Nullable Collider getCollider() {
        if (gameObject == null) return null;
        else return gameObject.getComponent(Collider.class);
    }

    /**
     * Provides a reference to the parent object's {@link mayonez.physics.dynamics.Rigidbody} component.
     *
     * @return the rigidbody, if it exists
     */
    protected @Nullable Rigidbody getRigidbody() {
        if (gameObject == null) return null;
        else return gameObject.getComponent(Rigidbody.class);
    }

}
