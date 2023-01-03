package mayonez;

import mayonez.graphics.sprites.Sprite;
import mayonez.math.Vec2;
import mayonez.physics.CollisionEventType;
import mayonez.physics.colliders.Collider;
import mayonez.scripts.movement.MovementScript;
import mayonez.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An object or entity in the game that can be given an appearance and behavior through {@link Component}s.
 *
 * @author SlavSquatSuperstar
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class GameObject {

    private static long objectCounter = 0L; // total number of game objects created across all scenes

    // Object Information and State
    final long objectID; // UUID for this game object
    public final String name; // object name, may be duplicate
    public final Transform transform;
    private Scene scene;
    private boolean destroyed;
    private int zIndex; // controls 3D "layering" of objects
    private final Set<String> tags;

    // Component Fields
    private final List<Component> components;
    private List<Class> updateOrder;

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
        objectID = objectCounter++;
        this.name = name == null ? "GameObject" : name;
        this.transform = transform;
        this.zIndex = zIndex;
        destroyed = false;
        tags = new HashSet<>(3);

        components = new ArrayList<>();
        updateOrder = null;
//        changesToObject = new LinkedList<>();
    }

    // Game Loop Methods

    /**
     * Add and initializes all components.
     */
    public final void start() {
        init();
        setUpdateOrder(Component.class, MovementScript.class, Script.class, Collider.class, Sprite.class);
        components.forEach(Component::start);
    }

    /**
     * Update all components.
     *
     * @param dt seconds since the last frame
     */
    public final void update(float dt) {
        components.forEach(c -> {
            if (c.isEnabled()) c.update(dt);
            // TODO dynamic component add/remove?
        });
        onUserUpdate(dt);
//        while (!changesToObject.isEmpty()) changesToObject.poll().onReceive();
    }

    // User Defined Methods

    /**
     * Add all components and initialize fields after this object has been added to the scene.
     */
    protected void init() {
    }

    /**
     * An overridable update method for custom update behavior.
     *
     * @param dt seconds since the last frame
     */
    protected void onUserUpdate(float dt) {
    }

    // Component Methods

    /**
     * Adds a component to this game object.
     *
     * @param comp the {@link Component} object
     */
    public final void addComponent(Component comp) {
        if (comp == null) return;
        if (!comp.getClass().isAnonymousClass() && getComponent(comp.getClass()) != null)
            Logger.debug("GameObject %s already has a %s", getNameAndID(), comp.getClass().getSimpleName());
        components.add(comp.setGameObject(this));
    }

//    /**
//     * Removes the component of the specified class from this game object.
//     *
//     * @param cls the component class
//     * @param <T> a subclass of {@link Component}
//     */
//    public <T extends Component> void removeComponent(Class<T> cls) {
//        for (Component c : components) {
//            if (cls.isInstance(c)) {
//                c.destroy();
//                components.remove(c); // will cause concurrent exception
//                return;
//            }
//        }
//    }

    /**
     * Finds the first component of the specified class or any of its subclasses, or null if none exists.
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
     * Destroy this game object and remove it from the scene.
     */
    final void destroy() {
        components.forEach(Component::destroy);
        components.clear();
        scene = null;
    }

    /**
     * Receives an event when a collision occurs between this object and another.
     *
     * @param other   the other object
     * @param trigger if interacting with a trigger
     * @param type    the type of the collision given by the listener
     */
    public final void onCollisionEvent(GameObject other, boolean trigger, CollisionEventType type) {
        for (Script s : getComponents(Script.class)) {
            switch (type) {
                case ENTER -> {
                    if (trigger) s.onTriggerEnter(other);
                    else s.onCollisionEnter(other);
                }
                case STAY -> {
                    if (trigger) s.onTriggerStay(other);
                    else s.onCollisionStay(other);
                }
                case EXIT -> {
                    if (trigger) s.onTriggerExit(other);
                    else s.onCollisionExit(other);
                }
            }
        }
    }

    // Property Getters and Setters

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

    public GameObject getParent() {
        return parent;
    }

    public GameObject setParent(GameObject parent) {
        this.parent = parent;
        return this;
    }

    public final Scene getScene() {
        return scene;
    }

    final GameObject setScene(Scene scene) {
        this.scene = scene;
        return this;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public int getZIndex() {
        return zIndex;
    }

    public GameObject setZIndex(int zIndex) { // can't change while scene is running in GL yet
        this.zIndex = zIndex;
        return this;
    }

    // Object Overrides

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectID, name, scene);
    }

    /**
     * Returns this object's name and ID as a single string.
     *
     * @return the name and ID
     */
    public String getNameAndID() {
        return String.format("%s [ID %d]", name, objectID);
    }

    @Override
    public String toString() {
        // Use GameObject for class name if anonymous instance
        return String.format(
                "%s (%s)",
                getNameAndID(), StringUtils.getClassName(this, "GameObject")
        );
    }

}
