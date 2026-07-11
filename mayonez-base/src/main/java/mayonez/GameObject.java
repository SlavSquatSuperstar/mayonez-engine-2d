package mayonez;

import mayonez.math.*;
import mayonez.util.BufferedList;
import mayonez.util.CallbackBuffer;
import mayonez.util.StringUtils;
import org.jspecify.annotations.Nullable;

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
 * {@link #transform}. To remove the object from the scene, call {@link GameObject#destroy}.
 * To remove a component from the object, call {@link GameObject#removeComponent}.
 * <p>
 * See {@link mayonez.Component} and {@link mayonez.Scene} for more information.
 *
 * @author SlavSquatSuperstar
 */
public class GameObject {

    private static long objectCounter = 0L; // total number of game objects created across all scenes

    // Object Information and State
    final long objectID; // UUID for this game object
    private String name;
    public final Transform transform; // transform in world
    private @Nullable Scene scene;
    private boolean destroyed, enabled, visible;
    private int zIndex; // controls 3D "layering" of objects
    private @Nullable SceneLayer layer;

    // Component Fields
    private final BufferedList<Component> components;

    /**
     * Creates an empty game object with a name and default transform. If the name
     * is {@code null}, it will default to the class name.
     *
     * @param name the object name
     */
    public GameObject(@Nullable String name) {
        this(name, new Vec2());
    }

    /**
     * Creates an empty game object with a name position, and a default rotation
     * and scale. If the name is {@code null}, it will default to the class name.
     *
     * @param name     the object name
     * @param position the object starting position
     */
    public GameObject(@Nullable String name, Vec2 position) {
        this(name, new Transform(position));
    }

    /**
     * Creates an empty game object with a name, transform, and a z-index of zero.
     * If the name is {@code null}, it will default to the class name.
     *
     * @param name      the object name
     * @param transform the object starting transform
     */
    public GameObject(@Nullable String name, Transform transform) {
        this(name, transform, 0);
    }

    /**
     * Creates an empty game object with a name, transform, and z-index. If the name
     * is {@code null}, it will default to the class name.
     *
     * @param name      the object name
     * @param transform the object starting transform
     * @param zIndex    the object z-index
     */
    public GameObject(@Nullable String name, Transform transform, int zIndex) {
        objectID = objectCounter++;

        this.name = validateName(name);
        this.transform = transform;
        this.zIndex = zIndex;
        this.layer = null;

        destroyed = false;
        enabled = true;
        visible = true;
        components = new BufferedList<>();
    }

    // Game Loop Methods

    /**
     * Adds all components to this object and then initializes them. Calls
     * {@link mayonez.Component#start()} for all components added on start.
     */
    final void start() {
        // Add all components
        init();
        components.processBuffer();
        // Start all components
        components.sort(Comparator.comparingInt(Component::getUpdateOrder));
        components.forEach(Component::start);
    }

    /**
     * Updates all enabled components on a fixed tick.
     *
     * @param dt seconds between fixed ticks
     */
    final void fixedUpdate(float dt) {
        components.stream()
                .filter(Component::isEnabled)
                .forEach(c -> c.fixedUpdate(dt));
    }

    /**
     * Updates all enabled components on a render frame.
     *
     * @param dt seconds since the last frame
     */
    final void update(float dt) {
        components.stream()
                .filter(Component::isEnabled)
                .forEach(c -> c.update(dt));
        // Add or remove components
        components.processBuffer();
    }

    /**
     * Draws debug information for all enabled components.
     */
    final void debugRender() {
        components.stream()
                .filter(Component::isVisible)
                .forEach(Component::debugRender);
    }

    // User Defined Methods

    /**
     * Add components and initializes fields after this object has been added to the scene.
     * The {@link #transform} field and {@link #getScene} method will return non-null here.
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
     * The component will not be added if it already has a parent object.
     *
     * @param comp the component
     */
    public final void addComponent(@Nullable Component comp) {
        if (comp == null || comp.getGameObject() != null) return;
        comp.setGameObject(this);
        if (scene != null && scene.isRunning()) {
            components.addBuffered(comp); // Add component later if scene running
        } else {
            components.addUnbuffered(comp); // Add component now
        }
    }

    /**
     * Removes and destroys a component from this game object if the component is not null.
     * The component will only be removed if its parent is this object.
     *
     * @param comp the component
     */
    public final void removeComponent(@Nullable Component comp) {
        if (comp == null || comp.getGameObject() != this) return;
        comp.destroy();
        if (scene != null && scene.isRunning()) {
            components.removeBuffered((comp)); // Remove component later if scene running
        } else {
            components.removeUnbuffered(comp); // Remove component now
        }
    }

    /**
     * Counts how many components this object has.
     *
     * @return the number of components
     */
    public int numComponents() {
        return components.size();
    }

    /**
     * Finds the first component of the specified class or any of its subclasses,
     * or null if none exists.
     *
     * @param cls a {@link mayonez.Component} subclass
     * @param <T> the component type
     * @return the component, or null if not present
     */
    public <T extends Component> @Nullable T getComponent(@Nullable Class<T> cls) {
        if (cls == null) return null;
        return components.stream()
                .filter(cls::isInstance)
                .map(cls::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds all components with the specified class or its subclasses.
     *
     * @param cls a {@link mayonez.Component} subclass
     * @param <T> the component type
     * @return the list of components, or empty if none are present
     */
    public <T extends Component> List<T> getComponents(@Nullable Class<T> cls) {
        if (cls == null) return List.of();
        return components.stream()
                .filter(cls::isInstance)
                .map(cls::cast)
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

    // Callback Methods

    final void onDestroy() {
        components.forEach(Component::destroy);
        components.clear();
        layer = null;
        scene = null;
    }

    // Property Getters and Setters

    /**
     * Get this object's name, which is non-null and does not need to be unique.
     *
     * @return the object name
     */
    public String getName() {
        return name;
    }

    /**
     * Set this object's name, which does not need to be unique. If the parameter is null,
     * then the name will be set to the object's class name.
     *
     * @param name the object name
     */
    public void setName(@Nullable String name) {
        this.name = validateName(name);
    }

    private String validateName(@Nullable String name) {
        return Objects.requireNonNullElse(name, StringUtils.getObjectClassName(this));
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
     * Remove this object from the scene and destroy all its components.
     * The {@link #getScene} method will return after the object is destroyed.
     * <p>
     * Warning: Destroying a game object is permanent and cannot be reversed!
     */
    public void destroy() {
        destroyed = true;
    }

    /**
     * Whether this object and all its components should be updated.
     *
     * @return if this object is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enable or disable whether this object and all its components should be updated.
     *
     * @param enabled if the object is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Whether this object and all its components should be rendered.
     *
     * @return if this object is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Enable or disable whether this object and all its components should be rendered.
     *
     * @param visible if the object is visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Get the game object's {@link mayonez.SceneLayer}, which specifies which objects
     * it interacts with. If the layer is null, the object will interact with all other
     * objects.
     *
     * @return the layer
     */
    public @Nullable SceneLayer getLayer() {
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
    public void setLayer(@Nullable SceneLayer layer) {
        this.layer = layer;
    }

    /**
     * Get the {@link mayonez.Scene} that contains this game object. The parent scene
     * will be non-null from the start of {@link #init} to the end of {@link #destroy}
     *
     * @return the parent scene
     */
    public final @Nullable Scene getScene() {
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

    @Override
    public String toString() {
        return String.format("%s [%d]", name, objectID);
    }

}
