package mayonez;

import mayonez.graphics.sprite.Sprite;
import mayonez.math.Vec2;
import mayonez.physics.colliders.Collider;
import mayonez.scripts.MovementScript;

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

    // Object Information
    public String name;
    public final Transform transform;
    private int zIndex; // controls 3D "layering" of objects
    private final Set<String> tags;
    private Scene scene;

    // Component Fields
    private final List<Component> components;
    private List<Class> updateOrder;

    // Object State
//    private final Queue<Receivable> changesToObject; // Dynamic add/remove components
    private boolean destroyed;

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
        updateOrder = null;

        destroyed = false;
//        changesToObject = new LinkedList<>();
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
        setUpdateOrder(Component.class, MovementScript.class, Script.class, Sprite.class);
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
            // TODO remove destroyed objects
            // TODO dynamic component add
        });
        onUserUpdate(dt);
//        while (!changesToObject.isEmpty()) changesToObject.poll().onReceive();
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
        if (comp == null) return;
        // maybe make annotation (multiple scripts should suppress warning)
//		if (null != getComponent(comp.getClass()))
//			Logger.warn("GameObject: Adding multiple components of the same type is not recommended");
        components.add(comp.setObject(this));
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
     * Finds the first component of the specified class or its subclasses. For example, getComponent(Script.class)
     * returns the first instance of Script, KeyMovement, etc., but not Collider.
     *
     * @param cls a {@link Component} subclass
     * @param <T> the component type
     * @return the component, or null if not present
     */
    public <T extends Component> T getComponent(Class<T> cls) {
        for (Component c : components) {
            if (cls.isAssignableFrom(c.getClass())) return cls.cast(c);
        }
        return null;
    }

    /**
     * Finds all components with the specified class or its subclasses.
     *
     * @param cls a {@link Component} subclass, or null to get all components
     * @param <T> the component type
     * @return the list of components, or empty if none are present
     */
    public <T extends Component> List<T> getComponents(Class<T> cls) {
        return components.stream().filter(c -> cls == null || cls.isInstance(c)).map(c -> (T) c).collect(Collectors.toList());
    }

    /**
     * Sets the update order of this object's components from first to last. A subclass's order will override the order
     * of its superclass.
     *
     * @param order an array of component subclasses
     */
    public void setUpdateOrder(Class... order) {
        if (updateOrder == null) { // if not defined
            updateOrder = new ArrayList<>();
            updateOrder.addAll(Arrays.stream(order).filter(
                    // Include non-nulls and Component subclasses only
                    cls -> cls != null && Component.class.isAssignableFrom(cls)
            ).toList());
        }
        components.sort(Comparator.comparingInt(c -> getUpdateOrder(c.getClass())));
    }

    private int getUpdateOrder(Class componentCls) {
        int i = updateOrder.indexOf(componentCls);
        if (i > -1) return i; // present
        i = getUpdateOrder(componentCls.getSuperclass()); // keep searching for super
        return (i > -1) ? i : updateOrder.size(); // just update last
    }

    // Callback Methods

    /**
     * Custom user behavior after making contact with another physical object.
     *
     * @param other the other object in the collision
     */
    public void onCollision(GameObject other) {
    }

    /**
     * Custom user behavior after contacting a trigger area.
     *
     * @param trigger the trigger collider
     */
    public void onTrigger(Collider trigger) {
    }

    /**
     * Destroy this game object and remove it from the scene.
     */
    final void onDestroy() {
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
        return String.format(
                "%s (%s)", name,
                getClass().isAnonymousClass() ? "GameObject" : getClass().getSimpleName()
        );
    }

}
