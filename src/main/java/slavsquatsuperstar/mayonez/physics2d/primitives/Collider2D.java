package slavsquatsuperstar.mayonez.physics2d.primitives;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.Component;
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;
import slavsquatsuperstar.util.MathUtils;

/**
 * A shape that takes up space and can detect collisions. Requires a {@link Rigidbody2D} to respond to collisions properly.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Collider2D extends Component {

    // Object References
    /**
     * A reference to the parent object's {@link Transform}.
     */
    protected Transform transform;
    /**
     * A reference to the parent object's {@link Rigidbody2D}.
     */
    protected Rigidbody2D rb = null;

    // Physics Properties
    private float bounce = 0.25f;

    // Game Loop Methods

    @Override
    public final void start() {
        rb = parent.getComponent(Rigidbody2D.class);
        if (rb == null) // TODO what to do if object is static
            Logger.warn("%s needs a Rigidbody to function!", getClass().getSimpleName());
        // TODO disable component?
    }

    // Shape Properties

    public Vector2 center() {
        return transform.position;
    }

    public abstract AlignedBoxCollider2D getMinBounds();

    // Shape vs Point Collisions

    /**
     * Calculates whether the given point is on or inside this shape.
     *
     * @param point a vector
     * @return if the shape contains the point
     */
    public abstract boolean contains(Vector2 point);

    /**
     * Returns the point on or inside the collider nearest to the given position.
     *
     * @param position a 2D vector
     * @return the point
     */
    public abstract Vector2 nearestPoint(Vector2 position);

    // Shape vs Line Collisions

    /**
     * Calculates whether the given {@link Line2D} touches passes through this collider.
     *
     * @param line a line
     * @return if the line intersects the line
     */
    public abstract boolean intersects(Line2D line);

    /**
     * Casts a ray onto this collider and calculates whether the ray intersects the collider and where.
     *
     * @param ray    the {@link Ray2D}
     * @param result (optional) a {@link RaycastResult} object passed to store contact information if the method returns
     *               true
     * @param limit  The maximum distance the ray is allowed to travel before hitting an object. Set to 0 to allow the
     *               ray to travel infinitely. Should be positive otherwise.
     * @return if the ray intersects this shape
     */
    public abstract boolean raycast(Ray2D ray, RaycastResult result, float limit);

    // Shape vs Shape Collisions

    /**
     * Detects whether this shape is touching or overlapping another {@link Collider2D}.
     *
     * @param collider another collider
     * @return if there is a collision
     */
    public abstract boolean detectCollision(Collider2D collider);

    public CollisionManifold getCollisionInfo(Collider2D collider){
        return null;
    }

    // Field Getters and Setters

    @Override
    public Collider2D setParent(GameObject parent) {
        super.setParent(parent);
        transform = parent.transform;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Collider2D> T setTransform(Transform transform) {
        this.transform = transform;
        return (T) this;
    }

    /**
     * Returns the parent object's {@link Rigidbody2D}. A collider should always have a rigidbody.
     *
     * @return the attached rigidbody
     */
    public Rigidbody2D getRigidbody() {
        return rb;
    }

    @SuppressWarnings("unchecked")
    public <T extends Collider2D> T setRigidBody(Rigidbody2D rb) {
        this.rb = rb;
        return (T) this;
    }

    public float getBounce() {
        return bounce;
    }

    public Collider2D setBounce(float bounce) {
        this.bounce = MathUtils.clamp(bounce, 0f, 1f);
        return this;
    }

}
