package mayonez;

import mayonez.util.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

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
public abstract class Component extends Node {

    /**
     * The parent {@link mayonez.GameObject} this component belongs to. The parent
     * object will be non-null from the start of {@link #init} to the end of
     * {@link #onDestroy()}.
     */
    protected @Nullable GameObject gameObject;

    /**
     * A reference to the parent object's {@link mayonez.Transform}.
     */
    protected Transform transform; // use blank transform in case no parent

    // TODO make changeable
    private final UpdateOrder updateOrder;

    protected Component() {
        this(UpdateOrder.SCRIPT);
    }

    public Component(@Nullable UpdateOrder updateOrder) {
        this(null, updateOrder);
    }

    public Component(@Nullable String name, @Nullable UpdateOrder updateOrder) {
        super(name);
        transform = new Transform();
        this.updateOrder = Objects.requireNonNullElse(updateOrder, UpdateOrder.SCRIPT);
    }

    // Game Loop Methods

    /**
     * Add any necessary components to the game object before other components have
     * been added. This method is called before {@link mayonez.GameObject#init} and
     * {@link mayonez.Component#start}. The fields {@link #gameObject} and
     * {@link #transform} are non-null here.
     * <p>
     * Usage: Subclasses may override this method and can also call {@code super.init()}.
     * <p>
     * Warning: Calling {@code init()} at any other point in time may lead to unintended
     * errors and should be avoided!
     */
    protected void init() {
    }

    /**
     * Initialize fields after all components have been added to the parent object. The
     * fields {@link #gameObject} and {@link #transform} and the method
     * {@link mayonez.GameObject#getComponent} are accessible here. The start method
     * will be called even if this component has been disabled through
     * {@link #setEnabled}.
     * <p>
     * Usage: Subclasses may override this method and can also call {@code super.start()}.
     * <p>
     * Warning: Calling {@code start()} at any other point in time may lead to unintended
     * errors and should be avoided!
     */
    protected void start() {
    }

    /**
     * Refresh the component's state and game logic. This method is called each fixed
     * tick, between physics and  {@link #update}, and {@code dt} is generally consistent.
     * The {@code fixedUpdate} method should be used for frame rate-sensitive behavior, such as
     * movement, collision, and AI.
     * <p>
     * Usage: Subclasses may override this method and can also call {@code super.fixedUpdate()}.
     *
     * @param dt seconds between fixed ticks
     */
    protected void fixedUpdate(float dt) {
    }

    /**
     * Refresh the component's state and game logic. This method is called each drawn
     * frame, between {@link #fixedUpdate} and rendering, and {@code dt} may vary. The
     * {@code update} method may be used for general behavior, such as input, timers,
     * and animations.
     * <p>
     * Usage: Subclasses may override this method and can also call {@code super.update()}.
     *
     * @param dt seconds since the last frame
     */
    protected void update(float dt) {
    }

    /**
     * Draw debug information to the screen during this frame after all objects have
     * been updated. Any {@link mayonez.graphics.debug.DebugDraw} method calls should
     * be made here. This method is called even if the scene is paused or the component
     * is not enabled.
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
    final void destroy() {
        setDestroyed();
        onDestroy();
        gameObject = null;
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

    /**
     * Adds this component to a parent {@link mayonez.GameObject}. Should only
     * be used by the {@link mayonez.GameObject}.
     *
     * @param gameObject a game object
     */
    final void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.transform;
        setScene(gameObject.getScene()); // Scene will be non-null here
        init();
    }

    /**
     * Get a reference to the parent object's {@link mayonez.Transform}.
     *
     * @return the parent transform
     */
    public Transform getTransform() {
        return transform;
    }

    int getUpdateOrder() {
        return updateOrder.order();
    }

}
