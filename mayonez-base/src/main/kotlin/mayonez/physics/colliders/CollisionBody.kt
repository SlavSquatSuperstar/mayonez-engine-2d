package mayonez.physics.colliders

import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.physics.*
import mayonez.physics.dynamics.*
import mayonez.physics.manifold.*

/**
 * An object's physical shape in the world that responds to collision.
 *
 * @author SlavSquatSuperstar
 */
interface CollisionBody {

    // Collider Properties

    /** The [PhysicsBody] attached to this shape. */
    var rigidbody: PhysicsBody?

    /**
     * Whether this collider is non-physical and should not react to
     * collisions.
     */
    val trigger: Boolean

    /** If this frame's collision should not be resolved by the physics engine. */
    var ignoreCurrentCollision: Boolean

    /**
     * Whether the position or velocity of this collider has been modified in
     * this frame.
     */
    var collisionResolved: Boolean

    // Shape Properties

    fun getMinBounds(): BoundingBox

    fun getMass(density: Float): Float

    fun getAngMass(mass: Float): Float

    /** Transforms this shape into world space. */
    fun getShape(): Shape

    // Collision Methods

    /**
     * If this collision body should not be checked against another body.
     *
     * @param collider another collider
     * @return if the two colliders should collide
     */
    fun doNotCollideWith(collider: CollisionBody): Boolean

    /**
     * Calculates the contact points, normal, and overlap between this collider
     * and another if they are intersecting.
     *
     * @param collider another collider
     * @return the collision info, or no if there is no intersection
     */
    fun getContacts(collider: CollisionBody?): Manifold?

    // Collision Event Methods

    /**
     * Broadcasts an event if a collision occurs between this object and
     * another.
     */
    fun sendCollisionEvent(
        other: CollisionBody, trigger: Boolean,
        type: CollisionEventType, direction: Vec2?
    )

    // Object Overrides

    override fun equals(other: Any?): Boolean;

    override fun hashCode(): Int

}