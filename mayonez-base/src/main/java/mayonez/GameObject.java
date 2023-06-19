package mayonez;

import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.movement.*;
import mayonez.util.*;

import java.util.*;
import java.util.stream.*;

/**
 * An object or entity in the game that can be given an appearance and behavior through {@link Component}s.
 *
 * @author SlavSquatSuperstar
 */
public class GameObject {

    private static long objectCounter = 0L; // total number of game objects created across all scenes

    // Object Information and State
    final long objectID; // UUID for this game object
    private String name; // object name, does not have to be unique
    public final Transform transform; // transform in world
    final Transform localTransform; // transform offset from parent
    private Scene scene;
    private boolean destroyed;
    private int zIndex; // controls 3D "layering" of objects
    private final Set<String> tags;

    // Component Fields
    private final List<Component> components;
    private List<Class<?>> updateOrder;

    // Connected Objects
//    private GameObject parent; // parent object
//    private final List<GameObject> children; // child (nested) objects

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

        this.name = (name == null) ? "GameObject" : name;
        this.transform = transform;
        localTransform = new Transform();
        this.zIndex = zIndex;

        destroyed = false;
        tags = new HashSet<>(3);

        components = new ArrayList<>();
        updateOrder = null;
//        changesToObject = new LinkedList<>();
//        parent = null;
//        children = new LinkedList<>();
    }

    // Game Loop Methods

    /**
     * Add and initializes all components.
     */
    public final void start() {
        init();
//        children.forEach(getScene()::addObject);
        setUpdateOrder(Component.class, MovementScript.class, Script.class, Collider.class, Sprite.class);
        components.forEach(Component::start);
    }

    /**
     * Update all components.
     *
     * @param dt seconds since the last frame
     */
    public final void update(float dt) {
        // Combine with parent transform
//        Transform oldXf = transform.copy();
//        if (parent != null) transform.set(parent.transform.combine(localTransform));
        // Update
        components.forEach(c -> {
            if (c.isEnabled()) c.update(dt);
        });
        onUserUpdate(dt);
//        while (!changesToObject.isEmpty()) changesToObject.poll().run();
//        transform.set(oldXf); // Reset transform
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
        checkForDuplicateComponentClass(comp);
        components.add(comp.setGameObject(this));
    }

    private void checkForDuplicateComponentClass(Component comp) {
        var anonymous = comp.getClass().isAnonymousClass();
        var hasComponentOfSameClass = getComponent(comp.getClass()) != null;
        if (!anonymous && hasComponentOfSameClass) {
            Logger.debug("GameObject %s already has a %s", getNameAndID(), comp.getClass().getSimpleName());
        }
    }

//    /**
//     * Removes the component of the specified class from this game object.
//     *
//     * @param cls the component class
//     * @param <T> a subclass of {@link Component}
//     */
//    public <T extends Component> void removeComponent(Class<T> cls) {
//        for (var comp : components) {
//            if (cls.isInstance(comp)) {
//                comp.destroy();
//                components.remove(comp); // will cause concurrent exception
//                return;
//            }
//        }
//    }

    /**
     * Counts how many components this object has.
     *
     * @return the number of components
     */
    public int numComponents() {
        return components.size();
    }

    /**
     * Finds the first component of the specified class or any of its subclasses, or null if none exists.
     *
     * @param cls a {@link Component} subclass
     * @param <T> the component type
     * @return the component, or null if not present
     */
    public <T extends Component> T getComponent(Class<T> cls) {
        for (var comp : components) {
            if (cls.isAssignableFrom(comp.getClass())) return cls.cast(comp);
        }
        return null;
    }

    /**
     * Finds all components with the specified class or its subclasses.
     *
     * @param cls a {@link Component} subclass
     * @param <T> the component type
     * @return the list of components, or empty if none are present
     */
    @SuppressWarnings({"unchecked"})
    public <T extends Component> List<T> getComponents(Class<T> cls) {
        return components.stream()
                .filter(c -> cls != null && cls.isInstance(c))
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }

    /**
     * Returns copy of the list of all this object's components.
     *
     * @return the list of all components
     */
    public List<Component> getComponents() {
        return List.copyOf(components);
    }

    /**
     * Sets the update order of this object's components from first to last. A subclass's order will override the order
     * of its superclass.
     *
     * @param order an array of component subclasses
     */
    public void setUpdateOrder(Class<?>... order) {
        if (updateOrder == null) { // if not defined
            updateOrder = new ArrayList<>(filterComponentSubclasses(order));
        }
        components.sort(Comparator.comparingInt(c -> getComponentUpdateOrder(c.getClass())));
    }

    private static List<Class<?>> filterComponentSubclasses(Class<?>[] order) {
        return Arrays.stream(order)
                .filter(cls -> (cls != null) && Component.class.isAssignableFrom(cls))
                .toList();
    }

    private int getComponentUpdateOrder(Class<?> componentCls) {
        var i = updateOrder.indexOf(componentCls);
        if (i > -1) return i; // class present
        i = getComponentUpdateOrder(componentCls.getSuperclass()); // keep searching for super
        return (i > -1) ? i : updateOrder.size(); // just update last
    }

    // Child Object Methods

//    /**
//     * Adds a child GameObject, connecting its transform to this object's transform and setting its parent as this.
//     *
//     * @param child the child object
//     */
//    public void addChild(GameObject child) {
//        children.add(child.setParent(this));
//        if (scene != null) scene.addObject(child);
//    }

//    public void removeChild(GameObject child) {
//        children.remove(child.setParent(null));
//        if (scene != null) scene.addObject(child);
//    }

//    /**
//     * Finds the child GameObject at the given index, or null if the index is invalid.
//     * The index is the same as the order the child was added.
//     *
//     * @param index the child index
//     * @return the child object
//     */
//    public GameObject getChild(int index) {
//        if (index < 0 || index >= children.size()) return null;
//        else return children.get(index);
//    }

//    /**
//     * Finds the first child GameObject with the given name, or null if none exists.
//     *
//     * @param name the child name
//     * @return the child object
//     */
//    public GameObject getChild(String name) {
//        for (var child : children) {
//            if (child.name.equals(name)) return child;
//        }
//        return null;
//    }

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
        for (var scr : getComponents(Script.class)) {
            switch (type) {
                case ENTER -> {
                    if (trigger) scr.onTriggerEnter(other);
                    else scr.onCollisionEnter(other);
                }
                case STAY -> {
                    if (trigger) scr.onTriggerStay(other);
                    else scr.onCollisionStay(other);
                }
                case EXIT -> {
                    if (trigger) scr.onTriggerExit(other);
                    else scr.onCollisionExit(other);
                }
            }
        }
    }

    // Property Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Whether this object has been removed from the scene.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Remove this object from the scene and destroy all its components and children.
     */
    public void setDestroyed() {
        destroyed = true;
    }

//    public GameObject getParent() {
//        return parent;
//    }

//    final GameObject setParent(GameObject parent) {
//        this.parent = parent;
//        if (parent != null) localTransform.set(transform);
//        else localTransform.set(new Transform());
//        return this;
//    }

//    final boolean isChild() {
//        return parent != null;
//    }

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
//        if (parent != null) return parent.getZIndex() + this.zIndex;
//        else return zIndex;
        return zIndex;
    }

    public GameObject setZIndex(int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    // Object Overrides

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof GameObject o) && (o.objectID == this.objectID);
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
                getNameAndID(), StringUtils.getObjectClassName(this)
        );
    }

}
