package mayonez;

import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;

/**
 * A controllable and reusable behavior for a {@link mayonez.GameObject} which provides
 * additional callback functions that the user can override.
 * <p>
 * Usage: Scripts have an additional {@link #init} function, which allows them to add
 * additional components to the game object before the scene starts. Scripts also receive
 * callback functions, such as {@link #onEnable()} and {@link #onDestroy}, which are
 * called during game and collision events.
 * <p>
 * Scripts receive an order of {@link mayonez.UpdateOrder#SCRIPT} by default, but the order
 * can be changed using {@link #Script(UpdateOrder)}.
 * <p>
 * See {@link mayonez.GameObject} and {@link mayonez.Script} for more information.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Script extends Component {

    public Script() {
        super(UpdateOrder.SCRIPT);
    }

    public Script(UpdateOrder updateOrder) {
        super(updateOrder);
    }

    // Component Callbacks

    @SuppressWarnings("unchecked")
    public final <T extends Component> T setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) onEnable();
        else onDisable();
        return (T) this;
    }

    /**
     * Custom user behavior for when this script is enabled. Calling {@code onEnable()}
     * directly can lead to unpredictable behavior. It is better to call
     * {@link #setEnabled}({@code true}) instead.
     */
    protected void onEnable() {
    }

    /**
     * Custom user behavior for when this script is disabled. Calling {@code onDisable()}
     * directly can lead to unpredictable behavior. It is better to call
     * {@link #setEnabled}({@code false}) instead.
     */
    protected void onDisable() {
    }

    // Component Getters

    /**
     * Provides a reference to the parent object's {@link Collider} component.
     *
     * @return the collider, if it exists
     */
    protected Collider getCollider() {
        return gameObject.getComponent(Collider.class);
    }

    /**
     * Provides a reference to the parent object's {@link mayonez.physics.dynamics.Rigidbody} component.
     *
     * @return the rigidbody, if it exists
     */
    protected Rigidbody getRigidbody() {
        return gameObject.getComponent(Rigidbody.class);
    }

    @Override
    public String toString() {
        return String.format(
                "%s (%s)",
                getClass().isAnonymousClass() ? "Script" : getClass().getSimpleName(),
                gameObject == null ? "<No GameObject>" : gameObject.getNameAndID()
        );
    }

}
