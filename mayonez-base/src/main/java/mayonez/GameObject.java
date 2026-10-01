package mayonez;

import mayonez.math.*;
import org.jspecify.annotations.Nullable;

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
 * inside the {@link Node#init} method. The object's transform can be accessed through
 * {@link #getTransform()}. To remove the object from the scene, call {@link GameObject#setDestroyed}.
 * To remove a component from the object, call {@link Node#removeChild}.
 * <p>
 * See {@link mayonez.Component} and {@link mayonez.Scene} for more information.
 *
 * @author SlavSquatSuperstar
 * @deprecated Use {@link Node} instead
 */
@Deprecated
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
     * @deprecated Use {@link #addChild} instead
     */
    @Deprecated
    public final void addComponent(@Nullable Node comp) {
        super.addChild(comp);
    }

}
