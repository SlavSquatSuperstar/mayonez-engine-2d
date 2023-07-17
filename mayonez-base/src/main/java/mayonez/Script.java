package mayonez;

import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;

/**
 * A controllable and reusable behavior for a {@link GameObject} which provides additional
 * callback functions that the user can override.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Script extends Component {

    // Game Loop Methods

    /**
     * Add any necessary components to the game object before other components have been added.
     * This method is called before {@link mayonez.GameObject#init} and {@link mayonez.Component#start}.
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

    /**
     * Destroy this component and free up system resources once the parent {@link GameObject} is destroyed.
     */
    final void destroy() {
        onDestroy();
        super.destroy();
    }

    // Scene Callbacks

    /**
     * Custom user behavior for when this script is enabled.
     */
    public void onEnable() {
    }

    /**
     * Custom user behavior for when this script is disabled.
     */
    public void onDisable() {
    }

    /**
     * Custom behavior for when this script or its game object is destroyed.
     */
    public void onDestroy() {
    }

    // Collision Callbacks

    /**
     * Custom user behavior after starting contact with another physical object.
     *
     * @param other     the other object in the collision
     * @param direction the direction the collision is happening
     */
    public void onCollisionEnter(GameObject other, Vec2 direction) {
    }

    /**
     * Custom user behavior after staying in contact with another physical object.
     *
     * @param other the other object in the collision
     */
    public void onCollisionStay(GameObject other) {
    }

    /**
     * Custom user behavior after stopping contact with another physical object.
     *
     * @param other the other object in the collision
     */
    public void onCollisionExit(GameObject other) {
    }

    /**
     * Custom user behavior after entering a trigger area.
     *
     * @param other the other game object
     */
    public void onTriggerEnter(GameObject other) {
    }

    /**
     * Custom user behavior after staying with a trigger area.
     *
     * @param other the other game object
     */
    public void onTriggerStay(GameObject other) {
    }

    /**
     * Custom user behavior after exiting a trigger area.
     *
     * @param other the other game object
     */
    public void onTriggerExit(GameObject other) {
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
     * Provides a reference to the parent object's {@link Rigidbody} component.
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
