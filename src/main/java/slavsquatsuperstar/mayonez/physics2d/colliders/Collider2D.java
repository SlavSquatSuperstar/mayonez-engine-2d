package slavsquatsuperstar.mayonez.physics2d.colliders;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold;
import slavsquatsuperstar.mayonez.physics2d.RaycastResult;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;

/**
 * A shape that takes up space and can detect collisions. Requires a {@link Rigidbody2D} to respond to collisions
 * properly.
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

    /**
     * What percentage of energy is conserved after a collision (0-1).
     */
    private float bounce = 0.25f;

    /**
     * Whether this collider is non-physical and should not react to collisions.
     */
    private boolean trigger;

    // Game Loop Methods

    @Override
    public final void start() {
        rb = parent.getComponent(Rigidbody2D.class);
        if (rb == null) // TODO what to do if object is static
            Logger.warn("%s needs a Rigidbody to function properly!", this);
    }

    // Shape Properties

    public Vec2 center() {
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
    public abstract boolean contains(Vec2 point);

    /**
     * Returns the point on or inside the collider nearest to the given position.
     *
     * @param position a 2D vector
     * @return the point
     */
    public abstract Vec2 nearestPoint(Vec2 position);

    // Shape vs Line Collisions

    /**
     * Calculates whether the given {@link Edge2D} touches or passes through this collider.
     *
     * @param edge a line segment
     * @return if the edge intersects this shape
     */
    public boolean intersects(Edge2D edge) {
        if (contains(edge.start) || contains(edge.end))
            return true;
        return raycast(new Ray2D(edge), edge.length()) != null;
    }

    /**
     * Casts a ray onto this collider and calculates where the ray intersects the collider.
     *
     * @param ray    the {@link Ray2D}
     * @param limit  The maximum distance the ray is allowed to travel before hitting an object. Set to 0 to allow the
     *               ray to travel infinitely. Should be positive otherwise.
     * @return if the ray intersects this shape
     */
    public abstract RaycastResult raycast(Ray2D ray, float limit);

    // Shape vs Shape Collisions

    /**
     * Detects whether this collider is touching or overlapping another.
     *
     * @param collider another collider
     * @return if there is a collision
     */
    public boolean detectCollision(Collider2D collider) {
        return getCollisionInfo(collider) != null;
    }

    /**
     * Calculates the contact points, normal, and overlap between this collider and another if they are intersecting.
     *
     * @param collider another collider
     * @return the collision info, or no if there is no intersection
     */
    public abstract CollisionManifold getCollisionInfo(Collider2D collider);

    // Transform Methods

    public Vec2 toLocal(Vec2 world) {
        return transform.toLocal(world);
    }

    public Vec2 toWorld(Vec2 local) {
        return transform.toWorld(local);
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

    public boolean isTrigger() {
        return trigger;
    }

    public Collider2D setTrigger(boolean trigger) {
        this.trigger = trigger;
        return this;
    }

    /**
     * Returns whether this collider has a null or static rigidbody, and thus does not respond to collisions.
     *
     * @return if this collider is not affected by collisions.
     */
    public boolean isStatic() {
        return rb == null || rb.hasInfiniteMass();
    }

}
