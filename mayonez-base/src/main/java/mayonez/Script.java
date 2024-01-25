package mayonez;

import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;

/**
 * A controllable and reusable behavior for a {@link mayonez.GameObject} which provides
 * additional callback functions that the user can override.
 * <p>
 * Usage: Scripts have an additional {@link #init} function, which allows them to add
 * additional components to the game object before the scene starts. Scripts also receive
 * callback functions, such as {@link #onEnable()} and {@link #onCollisionEnter}, which are
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

    // Game Loop Methods

    /**
     * Add any necessary components to the game object before other components have been added.
     * This method is called before {@link mayonez.GameObject#init} and {@link mayonez.Component#start}.
     * The fields {@link #gameObject} and {@link #transform} are accessible here.
     * <p>
     * Usage: Subclasses may override this method and can also call {@code super.init()}.
     * <p>
     * Warning: Calling {@code init()} at any other point in time may lead to unintended errors
     * and should be avoided!
     */
    public void init() {
    }

    // Component Methods

    @Override
    final void setGameObject(GameObject gameObject) {
        super.setGameObject(gameObject);
        init();
    }

    @SuppressWarnings("unchecked")
    public final <T extends Component> T setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) onEnable();
        else onDisable();
        return (T) this;
    }

    public final void destroy() {
        setEnabled(false);
        onDestroy();
        super.destroy();
    }

    // Scene Callbacks

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

    /**
     * Custom behavior for when this script or its game object is destroyed. The fields
     * {@link #gameObject} and {@link #transform} will still be accessible. Calling
     * {@code onDestroy()}  directly can lead to unpredictable behavior. It is better to
     * call {@link #destroy} instead.
     */
    protected void onDestroy() {
    }

    // Collision Callbacks

    /**
     * Custom user behavior after starting contact with another physical object.
     *
     * @param other     the other object in the collision
     * @param direction the direction the collision is happening
     * @param velocity  the relative velocity of the objects.
     */
    protected void onCollisionEnter(GameObject other, Vec2 direction, Vec2 velocity) {
    }

    /**
     * Custom user behavior while staying in contact with another physical object.
     *
     * @param other the other object in the collision
     */
    protected void onCollisionStay(GameObject other) {
    }

    /**
     * Custom user behavior after stopping contact with another physical object.
     *
     * @param other the other object in the collision
     */
    protected void onCollisionExit(GameObject other) {
    }

    /**
     * Custom user behavior after entering a trigger area.
     *
     * @param other the other game object
     */
    protected void onTriggerEnter(GameObject other) {
    }

    /**
     * Custom user behavior while staying inside a trigger area.
     *
     * @param other the other game object
     */
    protected void onTriggerStay(GameObject other) {
    }

    /**
     * Custom user behavior after exiting a trigger area.
     *
     * @param other the other game object
     */
    protected void onTriggerExit(GameObject other) {
    }

    // Component Getters

    /**
     * Provides a reference to the parent object's {@link Collider} component.
     *
     * @return the collider, if it exists
     */
    public Collider getCollider() {
        return gameObject.getComponent(Collider.class);
    }

    /**
     * Provides a reference to the parent object's {@link mayonez.physics.dynamics.Rigidbody} component.
     *
     * @return the rigidbody, if it exists
     */
    public Rigidbody getRigidbody() {
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
