package mayonez;

import mayonez.util.*;

import java.util.*;

/**
 * Defines traits and behaviors of a {@link mayonez.GameObject}. Each component can be
 * enabled or disabled through {@link mayonez.Component#setEnabled}. Generally, most
 * user-defined components will be a {@link mayonez.Script} subclass.
 * <p>
 * Usage: Create a component by instantiating a subclass of {@link mayonez.Component}.
 * Any component fields through the constructor should be initialized through the
 * {@link #start} method, which allows them to be restored when the scene is reloaded.
 * Update component fields in {@link #update} or draw debug information in {@link #debugRender}.
 * The component's parent scene can be accessed through the {@link #getScene()} method,
 * and its {@link mayonez.GameObject} and transform can be accessed through the
 * {@link #gameObject} and {@link #transform} fields. To remove the component from its
 * object, call {@link #destroy}.
 * <p>
 * See {@link mayonez.GameObject} and {@link mayonez.Script} for more information.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Component {

    private static long componentCounter = 0L; // total number of components created across all scenes
    final long componentID; // internal UUID for this component

    /**
     * The parent {@link mayonez.GameObject} this component belongs to.
     */
    protected GameObject gameObject;

    /**
     * A reference to the parent object's {@link mayonez.Transform}.
     */
    protected Transform transform; // use blank transform in case no parent

    private boolean enabled; // whether this component is being updated

    protected Component() {
        componentID = componentCounter++;
        transform = new Transform();
        enabled = true;
    }

    // Game Loop Methods

    /**
     * Initialize fields after all components have been added to the parent object.
     */
    public void start() {
    }

    /**
     * Refresh the component's state and game logic. The update method is called each frame.
     *
     * @param dt seconds since the last frame
     */
    public void update(float dt) {
    }

    /**
     * Draw debug information to the screen during this frame. Any {@link mayonez.graphics.debug.DebugDraw}
     * method calls should be made here for consistent visual results.
     */
    public void debugRender() {
    }

    /**
     * Destroy this component and free up system resources once the parent {@link GameObject} is destroyed.
     */
    void destroy() {
        gameObject = null;
    }

    // Getters and Setters

    /**
     * Whether this component should be updated.
     *
     * @return if this component is enabled and not destroyed
     */
    public final boolean isEnabled() {
        return enabled && gameObject != null;
    }

    @SuppressWarnings("unchecked")
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
     * Adds this Component to a parent {@link mayonez.GameObject}.
     *
     * @param gameObject a game object
     */
    void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.transform;
    }

    /**
     * A reference to the {@link mayonez.Scene} the parent object belongs to.
     *
     * @return the parent scene
     */
    public Scene getScene() {
        return gameObject.getScene();
    }

    public Transform getTransform() {
        return transform;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T setTransform(Transform transform) {
        this.transform = (transform != null) ? transform : new Transform();
        return (T) this;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Component c) && (c.componentID == this.componentID);
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
                StringUtils.getObjectClassName(this),
                gameObject == null ? "<No GameObject>" : gameObject.getNameAndID()
        );
    }

}
