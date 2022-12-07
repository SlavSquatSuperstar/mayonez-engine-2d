package mayonez;

import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.Collider;

/**
 * A controllable and reusable behavior for a {@link GameObject}.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Script extends Component {

    // Game Loop Methods

    /**
     * Add any necessary components to the game object before other components have been added.
     */
    public void init() {

    }

    // Callback Methods

    /**
     * Custom user behavior after starting contact with another physical object.
     *
     * @param other the other object in the collision
     */
    public void onCollisionEnter(GameObject other) {
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
     * Provides a reference to the parent object's component of the given type.
     * @param cls the component class
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

    @Override
    public String toString() {
        return String.format(
                "%s (%s)",
                getClass().isAnonymousClass() ? "Script" : getClass().getSimpleName(),
                gameObject == null ? "<No Parent>" : gameObject.name
        );
    }
}
