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

    public Vector2 center() {
        return transform.position;
    }

    public abstract boolean contains(Vector2 point);

    public abstract boolean intersects(Line2D l);

//    public abstract boolean intersects(Collider2D collider);

    /**
     * Casts a ray onto this collider and calculates whether the ray
     * eventually intersects this collider.
     *
     * @param ray    A unit vector
     * @param result An object to store the point of contact
     * @return Whether the ray is pointed at the shape.
     */
    public abstract boolean raycast(Ray2D ray, RaycastResult result);

    @Override
    public void start() {
        this.transform = parent.transform;
        if (parent.followPhysics) {
            rb = parent.getComponent(RigidBody2D.class);
            if (rb == null)
                Logger.log("%s needs a RigidBody to function!", getClass().getSimpleName());
        }
    }

    public void setRigidBody(RigidBody2D rb) {
        this.rb = rb;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

}
