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
 * <p></p>
 * The component's parent scene can be accessed through the {@link #getScene()} method,
 * and its {@link mayonez.GameObject} and transform can be accessed through the
 * {@link #gameObject} and {@link #transform} fields. To remove the component from its
 * object, call {@link #destroy}. Components may also be given an {@link mayonez.UpdateOrder}
 * to tell the game object when to update it using {@link #Component(UpdateOrder)}.
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

    private final UpdateOrder updateOrder;

    protected Component() {
        this(UpdateOrder.SCRIPT);
    }

    public Component(UpdateOrder updateOrder) {
        componentID = componentCounter++;
        transform = new Transform();
        enabled = true;
        this.updateOrder = updateOrder;
    }

    // Game Loop Methods

    /**
     * Initialize fields after all components have been added to the parent object, meaning
     * {@link #gameObject}, {@link #transform}, and {@link mayonez.GameObject#getComponent}
     * will be accessible. The start method will be called even if this component has been
     * disabled through {@link #setEnabled}.
     * <p>
     * Usage: Subclasses may override this method and can also call {@code super.start()}.
     * <p>
     * Warning: Calling {@code start()} at any other point in time may lead to unintended errors
     * and should be avoided!
     */
    protected void start() {
    }

    /**
     * Refresh the component's state and game logic. The update method is called each frame.
     * <p>
     * Usage: Subclasses may override this method and can also call {@code super.update()}.
     *
     * @param dt seconds since the last frame
     */
    protected void update(float dt) {
    }

    /**
     * Draw debug information to the screen during this frame. Any {@link mayonez.graphics.debug.DebugDraw}
     * method calls should be made here for consistent visual results.<p>
     * <p>
     * Usage: Subclasses may override this method and can also call {@code super.debugRender()}.
     */
    protected void debugRender() {
    }

    // Scene Methods

    /**
     * Destroy this component, disabling it and removing it from its parent {@link GameObject}.
     * The fields {@link #gameObject} and {@link #transform} will be set to null.
     * <p>
     * Warning: Destroying a component is permanent and cannot be reversed!
     */
    public void destroy() {
        gameObject = null;
        transform = null;
    }

    /**
     * Whether this component should be updated.
     *
     * @return if this component is enabled and not destroyed
     */
    public final boolean isEnabled() {
        return enabled && gameObject != null;
    }

    /**
     * Enable or disable whether this component should be updated.
     *
     * @param enabled if the component is enabled
     * @param <T>     the component subclass type
     * @return this component
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    // Getters and Setters

    /**
     * Returns the parent {@link GameObject} this Component is attached to.
     *
     * @return the game object
     */
    public GameObject getGameObject() {
        return gameObject;
    }

    /**
     * Adds this component to a parent {@link mayonez.GameObject}. Should only
     * be used by the {@link mayonez.GameObject}.
     *
     * @param gameObject a game object
     */
    void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.transform;
    }

    /**
     * Get a reference to the {@link mayonez.Scene} the parent object belongs to.
     *
     * @return the parent scene
     */
    public Scene getScene() {
        return gameObject.getScene();
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = (transform != null) ? transform : new Transform();
    }

    int getUpdateOrder() {
        return updateOrder.order;
    }

    // Object Overrides

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
