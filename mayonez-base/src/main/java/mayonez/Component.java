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
 * {@link Node#getParent} and {@link Node#getTransform} meehods. To remove the component from its
 * object, call {@link #setDestroyed}. Components may also be given an update order
 * to tell the game object when to update it using {@link #Component(int)}.
 * <p>
 * See {@link mayonez.GameObject} and {@link mayonez.Script} for more information.
 *
 * @author SlavSquatSuperstar
 * @deprecated Use {@link Node} instead
 */
@Deprecated
public abstract class Component extends Node {

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

    // Property Getters and Setters

    @Override
    void setParent(@Nullable Node parent) {
        super.setParent(parent);
        if (parent instanceof GameObject obj) {
            this.transform = obj.transform;
        } else if (parent == null) {
            transform = new Transform();
        }
    }

    @Override
    public Transform getGlobalTransform() {
        if (parent == null) return transform;
        else return parent.getGlobalTransform();
        // Don't combine parent transform with itself
    }

}
