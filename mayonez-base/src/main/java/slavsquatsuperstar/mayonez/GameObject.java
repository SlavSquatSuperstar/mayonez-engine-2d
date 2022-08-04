package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.colliders.Collider;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A collection of {@link Component}s representing an in-game entity.
 *
 * @author SlavSquatSuperstar
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class GameObject {

    // GameObject Information
    public String name;
    public final Transform transform;
    private int zIndex;
    private final Set<String> tags;

    // Scene Information
    private Scene scene;
    private boolean destroyed = false;

    // Component Fields
    private final List<Component> components;
    private List<Class> updateOrder = null;

    public GameObject(String name) {
        this(name, new Vec2());
    }

    public GameObject(String name, Vec2 position) {
        this(name, new Transform(position));
    }

    public GameObject(String name, Transform transform) {
        this(name, transform, 0);
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.transform = transform;
        this.zIndex = zIndex;
        tags = new HashSet<>(3);
        components = new ArrayList<>();
    }

    // Game Loop Methods

    /**
     * Add necessary components and provide user-defined initialization behavior.
     */
    protected void init() {
    }

    /**
     * Initializes all components.
     */
    public final void start() {
        init();
        setUpdateOrder(Component.class, Script.class);
        components.forEach(Component::start);
    }

    /**
     * Updates all components.
     *
     * @param dt seconds since the last frame
     */
    public final void update(float dt) {
        components.forEach(c -> {
            if (c.isEnabled()) c.update(dt);
        });
        onUserUpdate(dt);
    }

    /**
     * An overridable update method for custom update behavior.
     *
     * @param dt seconds since the last frame
     */
    protected void onUserUpdate(float dt) {
    }

    /**
     * Renders all components.
     *
     * @param g2 the window's graphics object
     */
    public final void render(Graphics2D g2) {
        components.forEach(c -> {
            if (c.isEnabled()) c.render(g2);
        });
        onUserRender(g2);
    }

    /**
     * An overridable draw method for custom render behavior.
     *
     * @param g2 the window's graphics object
     */
    protected void onUserRender(Graphics2D g2) {
    }

    // Component Methods

    /**
     * Adds a component to this game object.
     *
     * @param comp the {@link Component} object
     */
    public final void addComponent(Component comp) {
        // maybe make annotation (multiple scripts should suppress warning)
//		if (null != getComponent(comp.getClass()))
//			Logger.warn("GameObject: Adding multiple components of the same type is not recommended");
        components.add(comp.setParent(this));
    }

    /**
     * Removes the component of the specified class from this game object.
     *
     * @param cls the component class
     * @param <T> a subclass of {@link Component}
     * @return if the component was removed
     */
    public <T extends Component> boolean removeComponent(Class<T> cls) {
        for (Component c : components) {
            if (cls.isInstance(c)) {
                c.destroy();
                components.remove(c);
                return true;
            }
        }
        return false;
    }

    /**
     * Finds all components with the specified class.
     *
     * @param cls a {@link Component} subclass (use null to get all objects)
     * @param <T> the components type
     * @return the list of components
     */
    public <T extends Component> List<T> getComponents(Class<T> cls) {
        return components.stream().filter(o -> cls == null || cls.isInstance(o)).map(o -> (T) o).collect(Collectors.toList());
    }

    /**
     * Finds the first component of the specified class.
     *
     * @param cls a {@link Component} subclass
     * @param <T> the component type
     * @return the component
     */
    public <T extends Component> T getComponent(Class<T> cls) {
        try {
            for (Component c : components) {
                if (cls.isAssignableFrom(c.getClass()))
                    return cls.cast(c);
            }
        } catch (ClassCastException e) { // This shouldn't happen!
            Logger.error("GameObject: Error accessing %s component", cls.getSimpleName());
            Logger.printStackTrace(e);
        }
        return null;
    }

    /**
     * Sets the update order of this object's components. A subclass's order will override the order of its superclass.
     *
     * @param order an array of component subclasses
     */
    public void setUpdateOrder(Class... order) {
        if (updateOrder == null) {
            updateOrder = new ArrayList<>();
            updateOrder.addAll(Arrays.asList(order));
        }
        components.sort(Comparator.comparingInt(c -> getUpdateOrder(c.getClass())));
    }

    @SuppressWarnings("rawtypes")
    private int getUpdateOrder(Class componentCls) {
        int i = updateOrder.indexOf(componentCls);
        if (i > -1) return i;
        i = getUpdateOrder(componentCls.getSuperclass());
        return (i > -1) ? i : updateOrder.size();
    }

    // Callback Methods

    /**
     * What to do after making contact with another physical object.
     *
     * @param other the other object in the collision
     */
    public void onCollision(GameObject other) {
    }

    /**
     * What to do after passing through a trigger area.
     *
     * @param trigger the trigger collider
     */
    public void onTrigger(Collider trigger) {
    }

    /**
     * Destroy this game object and remove it from the scene.
     */
    void onDestroy() {
        components.forEach(Component::destroy);
        components.clear();
        scene = null;
    }

    // Getters and Setters

    public Scene getScene() {
        return scene;
    }

    public GameObject setScene(Scene scene) {
        this.scene = scene;
        return this;
    }

    /**
     * Whether this object has been removed from the scene.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Mark this object to be removed from the scene.
     */
    public void setDestroyed() {
        destroyed = true;
    }

    public int getZIndex() {
        return zIndex;
    }

    public GameObject setZIndex(int zIndex) { // can't change while scene is running in GL yet
        this.zIndex = zIndex;
        return this;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public GameObject addTag(String tag) {
        tags.add(tag);
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, getClass().isAnonymousClass() ?
                "GameObject" : getClass().getSimpleName());
    }

}
