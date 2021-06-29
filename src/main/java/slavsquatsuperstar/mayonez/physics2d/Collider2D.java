package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.Component;

public abstract class Collider2D extends Component {

    /**
     * A reference to the parent object's transform.
     */
    protected Transform transform;
    /**
     * A reference to the parent object's rigidbody.
     */
    protected RigidBody2D rb = null;

    @Override
    public void start() {
        this.transform = parent.transform;
        rb = parent.getComponent(RigidBody2D.class);
        if (rb == null) // TODO what to do if object is static
            Logger.log("%s needs a RigidBody to function!", getClass().getSimpleName());
    }

    // Collision Methods
    public abstract boolean contains(Vector2 point);

    public boolean intersects(Line2D line) {
        if (contains(line.start) || contains(line.end))
            return true;
        return raycast(new Ray2D(line), null);
    }

    public abstract boolean detectCollision(Collider2D collider);

    /**
     * Casts a ray onto this collider and calculates whether the ray
     * eventually intersects this collider.
     *
     * @param ray    A unit vector
     * @param result An object to store the point of contact
     * @return Whether the ray is pointed at the shape.
     */
    public abstract boolean raycast(Ray2D ray, RaycastResult result);

    // Properties

    public Vector2 center() {
        return transform.position;
    }

    public void setRigidBody(RigidBody2D rb) {
        this.rb = rb;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

}
