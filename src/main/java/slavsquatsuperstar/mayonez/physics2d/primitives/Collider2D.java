package slavsquatsuperstar.mayonez.physics2d.primitives;

import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.Component;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;

public abstract class Collider2D extends Component {

    // TODO debug draw
    /**
     * A reference to the parent object's {@link Transform}.
     */
    protected Transform transform;
    /**
     * A reference to the parent object's {@link Rigidbody2D}.
     */
    protected Rigidbody2D rb = null;

    // Game Loop Methods

    @Override
    public final void start() {
        this.transform = parent.transform;
        rb = parent.getComponent(Rigidbody2D.class);
        if (rb == null) // TODO what to do if object is static
            Logger.log("%s needs a RigidBody to function!", getClass().getSimpleName());
    }

    // Shape Properties

    public Vector2 getCenter() {
        return transform.position;
    }

    public abstract AlignedBoxCollider2D getMinBounds();

    // Collision Methods

    public abstract boolean contains(Vector2 point);

    public abstract boolean intersects(Line2D line);

    /**
     * Casts a ray onto this collider and calculates whether the ray
     * eventually intersects this collider.
     *
     * @param ray    A unit vector
     * @param result An object to store the point of contact
     * @return Whether the ray is pointed at the shape.
     */
    public abstract boolean raycast(Ray2D ray, RaycastResult result);

    /**
     * Detects whether this shape is touching or intersecting another {@link Collider2D}.
     *
     * @param collider another collider
     * @return if there is a collision
     */
    public abstract boolean detectCollision(Collider2D collider);

    // Getters and Setters

    public void setRigidBody(Rigidbody2D rb) {
        this.rb = rb;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

}
