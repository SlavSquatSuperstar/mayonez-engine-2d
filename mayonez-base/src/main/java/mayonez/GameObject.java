package mayonez;

import mayonez.math.*;
import mayonez.physics.*;
import mayonez.util.*;

import java.util.*;
import java.util.stream.*;

/**
 * An object or entity inside a scene whose appearance and behavior can be defined by adding
 * {@link mayonez.Component}s. Each game object has a name and {@link mayonez.Transform}.
 * <p>
 * Generally, with entity-component-system, the GameObject (entity) class should not be extended,
 * as most of the functionality should be provided with Component and Script subclasses.
 * However, the GameObject class may still be extended to provide reusable prefab objects.
 * <p>
 * Usage: Create a game object by instantiating a subclass or anonymous instance of
 * {@link mayonez.GameObject}. Add components to the object by calling {@link #addComponent}
 * inside the {@link #init} method. The object's transform can be referenced through the field
 * {@link #transform}. To remove the object from the scene, call {@link #destroy} from any
 * of its components.
 * <p>
 * See {@link mayonez.Component} and {@link mayonez.Scene} for more information.
 *
 * @author SlavSquatSuperstar
 */
// TODO enable/disable object
public class GameObject {

    private static long objectCounter = 0L; // total number of game objects created across all scenes

    // Object Information and State
    final long objectID; // UUID for this game object
    private final String name;
    public final Transform transform; // transform in world
    //    final Transform localTransform; // transform offset from parent
    private Scene scene;
    private boolean destroyed;
    private int zIndex; // controls 3D "layering" of objects
    private SceneLayer layer;

    // Component Fields
    private final List<Component> components;

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
//        localTransform = new Transform();
        this.zIndex = zIndex;
        this.layer = null;

        destroyed = false;

        components = new ArrayList<>();
//        changesToObject = new LinkedList<>();
//        parent = null;
//        children = new LinkedList<>();
    }

    // Game Loop Methods

    /**
     * Adds all components to this object and then initializes them. Calls
     * {@link mayonez.Component#start()} for all components added on start.
     * The method {@link mayonez.Scene#getObject} is accessible here.
     */
    final void start() {
        init();
//        children.forEach(getScene()::addObject);
        components.sort(Comparator.comparingInt(Component::getUpdateOrder));
        components.forEach(Component::start);
    }

    /**
     * Updates all enabled components.
     *
     * @param dt seconds since the last frame
     */
    final void update(float dt) {
        // Combine with parent transform
//        Transform oldXf = transform.copy();
//        if (parent != null) transform.set(parent.transform.combine(localTransform));
        // Update
        components.stream()
                .filter(Component::isEnabled)
                .forEach(c -> c.update(dt));
//        while (!changesToObject.isEmpty()) changesToObject.poll().run();
//        transform.set(oldXf); // Reset transform
    }

    /**
     * Draws debug information for all enabled components.
     */
    final void debugRender() {
        components.stream()
                .filter(Component::isEnabled)
                .forEach(Component::debugRender);
    }

    // User Defined Methods

    /**
     * Add components and initializes fields after this object has been added to the scene.
     * The {@link #transform} field and {@link #scene} method are accessible here.
     * <p>
     * Usage: Subclasses may override this method and can also call {@code super.init()}.
     * <p>
     * Warning: Calling {@code init()} at any other point in time may lead to unintended errors
     * and should be avoided!
     */
    protected void init() {
    }

    // Component Methods

    /**
     * Adds a component to this game object if the component is not null.
     *
     * @param comp the {@link mayonez.Component} instance
     */
    public final void addComponent(Component comp) {
        if (comp == null) return;
//        checkForDuplicateComponentClass(comp);
        comp.setGameObject(this);
        components.add(comp);
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
//     * @param <T> a subclass of {@link mayonez.Component}
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
     * @param cls a {@link mayonez.Component} subclass
     * @param <T> the component type
     * @return the component, or null if not present
     */
    public <T extends Component> T getComponent(Class<T> cls) {
        for (var comp : components) {
            // Component has same class or is subclass
            if (cls.isAssignableFrom(comp.getClass())) return cls.cast(comp);
        }
        return null;
    }

    /**
     * Finds all components with the specified class or its subclasses.
     *
     * @param cls a {@link mayonez.Component} subclass
     * @param <T> the component type
     * @return the list of components, or empty if none are present
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> List<T> getComponents(Class<T> cls) {
        return components.stream()
                .filter(c -> cls != null && cls.isInstance(c))
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }

    /**
     * Get a copy of the list of all this object's components.
     *
     * @return the list of all components
     */
    public List<Component> getComponents() {
        return List.copyOf(components);
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

    final void onDestroy() {
        components.forEach(Component::destroy);
        components.clear();
        layer = null;
        scene = null;
    }

    /**
     * Send an event to all components when a collision occurs between this object and another.
     *
     * @param event the collision event
     */
    // TODO make into event callback
    public final void onCollisionEvent(CollisionEvent event) {
        for (var script : getComponents(Script.class)) {
            switch (event.type) {
                case ENTER -> {
                    if (event.trigger) script.onTriggerEnter(event.other);
                    else script.onCollisionEnter(event.other, event.direction, event.velocity);
                }
                case STAY -> {
                    if (event.trigger) script.onTriggerStay(event.other);
                    else script.onCollisionStay(event.other);
                }
                case EXIT -> {
                    if (event.trigger) script.onTriggerExit(event.other);
                    else script.onCollisionExit(event.other);
                }
            }
        }
    }

    // Property Getters and Setters

    /**
     * Get the object's name, which does not have to be unique.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Whether this object has been removed from the scene.
     *
     * @return if the object is destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Remove this object from the scene and destroy all its components and children.
     * The {@link #scene} field will be set to null.
     * <p>
     * Warning: Destroying a game object is permanent and cannot be reversed!
     */
    public void destroy() {
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


    /**
     * Get the game object's {@link mayonez.SceneLayer}, which specifies which objects
     * it interacts with. If the layer is null, the object will interact with all other
     * objects.
     *
     * @return the layer
     */
    public SceneLayer getLayer() {
        return layer;
    }

    /**
     * If game object has the layer with the given name.
     *
     * @param layerName the layer name
     * @return if the layer matches the name
     */
    public boolean hasLayer(String layerName) {
        return layer != null && layer.getName().equals(layerName);
    }

    /**
     * If game object has the layer with the given index.
     *
     * @param layerIndex the layer index
     * @return if the layer matches the index
     */
    public boolean hasLayer(int layerIndex) {
        return layer != null && layer.getIndex() == layerIndex;
    }

    /**
     * Set the game object's {@link mayonez.SceneLayer}, which specifies which objects
     * it interacts with. If the layer is null, the object will interact with all other
     * objects.
     *
     * @param layer the layer
     */
    public void setLayer(SceneLayer layer) {
        this.layer = layer;
    }

    /**
     * Get the {@link mayonez.Scene} that contains this game object.
     *
     * @return the parent scene
     */
    public final Scene getScene() {
        return scene;
    }

    /**
     * Adds this GameObject to a parent {@link mayonez.Scene}.
     *
     * @param scene a scene
     */
    final void setScene(Scene scene) {
        this.scene = scene;
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
     * Get this object's name and ID as a single string.
     *
     * @return the name and ID
     */
    String getNameAndID() {
        return String.format("%s [ID %d]", name, objectID);
    }

    @Override
    public String toString() {
        // Use GameObject for class name if anonymous instance
        return String.format(
                "%s (%s)", getNameAndID(), StringUtils.getObjectClassName(this)
        );
    }

}
