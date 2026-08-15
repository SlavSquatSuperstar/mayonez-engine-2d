package mayonez;

import org.jspecify.annotations.Nullable;

/**
 * Defines traits and behaviors of a {@link mayonez.GameObject}. Each component can be
 * enabled or disabled through {@link #setEnabled}. Any rendering behavior can be toggled
 * through {@link #setVisible}. Generally, most user-defined components will be a
 * {@link mayonez.Script} subclass.
 * <p>
 * Usage: Create a component by instantiating a subclass of {@link mayonez.Component}.
 * Any component fields through the constructor should be initialized through the
 * {@link #start} method, which allows them to be restored when the scene is reloaded.
 * Update component fields in {@link #fixedUpdate} or {@link #update}.
 * <p>
 * The component's parent scene can be accessed through the {@link #getScene} method,
 * and its {@link mayonez.GameObject} and transform can be accessed through the
 * {@link #gameObject} and {@link #transform} fields. To remove the component from its
 * object, call {@link #setDestroyed}. Components may also be given an update order
 * to tell the game object when to update it using {@link #Component(int)}.
 * <p>
 * See {@link mayonez.GameObject} and {@link mayonez.Script} for more information.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Component extends Node {

    /**
     * The parent {@link mayonez.GameObject} this component belongs to. The parent
     * object will be non-null from the start of {@link #init} to the end of
     * {@link #onDestroy()}.
     */
    protected @Nullable GameObject gameObject;

    protected Component() {
        this(UpdateOrder.SCRIPT);
    }

    public Component(int updateOrder) {
        this(null, updateOrder);
    }

    public Component(@Nullable String name, int updateOrder) {
        super(name);
        transform = new Transform();
        setUpdateOrder(updateOrder);
    }

    // Scene Methods

    /**
     * Destroy this component, disabling it and removing it from its parent {@link GameObject}.
     * The fields {@link #gameObject} and {@link #transform} will be set to null.
     * <p>
     * Warning: Destroying a component is permanent and cannot be reversed!
     */
    @Override
    public void setDestroyed() {
        super.setDestroyed();
        transform = new Transform();
    }

    /**
     * Whether this component should be updated. If the parent object is
     * disabled, then this component will not be updated.
     *
     * @return if this component is enabled and not destroyed
     */
    @Override
    public final boolean isEnabled() {
        return super.isEnabled() && gameObject != null && gameObject.isEnabled();
        // TODO only check object if non null
        // TODO fix UI usages
        // TODO fix animator usages
        // TODO use shouldUpdate
    }

    /**
     * Whether this component should be rendered. If the parent object is
     * visible, then this component will not be rendered.
     *
     * @return if this component is visible
     */
    @Override
    public final boolean isVisible() {
        // Check parent visible if parent exists
        return super.isVisible() &&
                (gameObject == null || gameObject.isVisible());
        // TODO use shouldRender
    }

    // Property Getters and Setters

    /**
     * Returns the parent {@link GameObject} this Component is attached to.
     *
     * @return the game object
     */
    public @Nullable GameObject getGameObject() {
        return gameObject;
    }

    @Override
    void setParent(@Nullable Node parent) {
        super.setParent(parent);
        if (parent instanceof GameObject obj) {
            this.gameObject = obj;
            this.transform = obj.transform;
        }
    }

    /**
     * Get a reference to the parent object's {@link mayonez.Transform}.
     *
     * @return the parent transform
     */
    public Transform getTransform() {
        return transform;
    }

}
