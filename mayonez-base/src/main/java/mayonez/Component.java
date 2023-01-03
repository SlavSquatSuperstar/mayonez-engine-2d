package mayonez;

import mayonez.util.StringUtils;

import java.util.Objects;

/**
 * A data structure representing traits and behaviors of a {@link GameObject}.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Component {

    private static long componentCounter = 0L; // total number of components created across all scenes
    final long componentID; // internal UUID for this component

    /**
     * The parent {@link GameObject} this component belongs to.
     */
    protected GameObject gameObject;

    /**
     * A reference to the parent object's {@link Transform}.
     */
    protected Transform transform; // use blank transform in case no parent

    private boolean enabled; // whether this component is being updated
    boolean destroyed; // if this component has been destroyed
    // TODO replace with gameObject == null?

    protected Component() {
        componentID = componentCounter++;
        transform = new Transform();
        enabled = true;
        destroyed = false;
    }

    // Game Loop Methods

    /**
     * Initialize fields after all components have been added to the object.
     */
    public void start() {
    }

    /**
     * Refresh the component's state and game logic.
     *
     * @param dt seconds since the last frame
     */
    public void update(float dt) {
    }

    /**
     * Destroy this component and free up system resources once the parent {@link GameObject} is destroyed.
     */
    void destroy() {
        destroyed = true;
        gameObject = null;
    }

    // Getters and Setters

    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Whether this component should be updated.
     *
     * @return if this component is enabled and not destroyed
     */
    public final boolean isEnabled() {
        return enabled && !destroyed;
    }

    @SuppressWarnings({"unchecked"})
    public <T extends Component> T setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    /**
     * Returns the parent {@link GameObject} this Component is attached to.
     *
     * @return the game object
     */
    public GameObject getGameObject() {
        return gameObject;
    }

    // should only be used by Scene class

    /**
     * Adds this Component to a parent {@link GameObject}. Should only be used by the {@link Scene} class.
     *
     * @param gameObject a game object
     * @return this component
     */
    Component setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.transform;
        return this;
    }

    /**
     * A reference to the {@link Scene} the parent object belongs to.
     *
     * @return the parent scene
     */
    public Scene getScene() {
        return gameObject.getScene();
    }

    public boolean isInScene(Scene scene) {
        return scene != null && scene.equals(getScene()); // This could cause a NPE
    }

    public Transform getTransform() {
        return transform;
    }

    @SuppressWarnings({"unchecked"})
    public <T extends Component> T setTransform(Transform transform) {
        this.transform = (transform != null) ? transform : new Transform();
        return (T) this;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentID, gameObject);
    }

    @Override
    public String toString() {
        // Use Component for class name if anonymous instance
        return String.format(
                "%s (%s)",
                StringUtils.getClassName(this, "Component"),
                gameObject == null ? "<No GameObject>" : gameObject.getNameAndID()
        );
    }
}
