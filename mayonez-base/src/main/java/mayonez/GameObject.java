package mayonez;

import mayonez.math.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

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
 * {@link #transform}. To remove the object from the scene, call {@link GameObject#setDestroyed}.
 * To remove a component from the object, call {@link GameObject#removeComponent}.
 * <p>
 * See {@link mayonez.Component} and {@link mayonez.Scene} for more information.
 *
 * @author SlavSquatSuperstar
 */
public class GameObject extends Node {

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
        super(name, transform);
    }

    // Component Methods

    /**
     * Adds a component to this game object if the component is not null.
     * The component will not be added if it already has a parent object.
     *
     * @param comp the component
     */
    public final void addComponent(@Nullable Component comp) {
        super.addChild(comp);
    }

    /**
     * Removes and destroys a component from this game object if the component is not null.
     * The component will only be removed if its parent is this object.
     *
     * @param comp the component
     */
    public final void removeComponent(@Nullable Component comp) {
        super.removeChild(comp);
    }

    /**
     * Counts how many components this object has.
     *
     * @return the number of components
     */
    public int numComponents() {
        return super.numChildren();
    }

    /**
     * Finds the first component with the specified name (case-sensitive), or null if none exists.
     *
     * @param name the component's name
     * @return the component, or null if not present
     */
    public @Nullable Component getComponent(@Nullable String name) {
        return (Component) super.getChild(name);
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
        return super.getChild(cls);
    }

    /**
     * Finds all components with the specified class or its subclasses.
     *
     * @param cls a {@link mayonez.Component} subclass
     * @param <T> the component type
     * @return the list of components, or empty if none are present
     */
    public <T extends Component> List<T> getComponents(@Nullable Class<T> cls) {
        return super.getChildren(cls);
    }

    /**
     * Get a copy of the list of all this object's components.
     *
     * @return the list of all components
     */
    public List<Component> getComponents() {
        return super.getChildren()
                .stream().map(Component.class::cast)
                .toList();
    }

    // Callback Methods

    protected final void onDestroy() {
        children.forEach(Node::setDestroyed);
        children.clear();
        scene = null;
    }

}
