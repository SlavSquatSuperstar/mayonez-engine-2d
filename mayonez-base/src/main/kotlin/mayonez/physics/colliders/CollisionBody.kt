package mayonez.physics.colliders

import mayonez.math.shapes.*
import mayonez.physics.*
import mayonez.physics.dynamics.*
import mayonez.physics.manifold.*

/**
 * An object's physical shape in the world that responds to collisions with
 * other shapes.
 *
 * @author SlavSquatSuperstar
 */
interface CollisionBody {

    // Collider Properties

    /** The [PhysicsBody] attached to this shape. */
    var physicsBody: PhysicsBody?

    /**
     * Whether this collider is non-physical and should not react to
     * collisions.
     */
    val trigger: Boolean

    /**
     * Whether the position or velocity of this collider has been modified in
     * this frame.
     */
    var collisionResolved: Boolean

    // Shape Properties

    /**
     * Get the axis-aligned bounding box (AABB) of this shape in world
     * coordinates.
     *
     * @return the bounding box
     */
    fun getMinBounds(): BoundingBox

    /**
     * Get the mass of this shape in world space given a density.
     *
     * @param density the density, equal to mass / area
     * @return the mass
     */
    fun getMass(density: Float): Float

    /**
     * Get the angular mass (moment of inertia) of this shape in world space
     * given a mass.
     *
     * @param mass the mass
     * @return the angular mass
     */
    fun getAngMass(mass: Float): Float

    /**
     * Transforms this shape into world space.
     *
     * @return the shape
     */
    fun getShape(): Shape


    // Collision Methods

    /**
     * If this collision body should be checked against another body.
     *
     * @param collider another collider
     * @return if the two colliders should collide
     */
    fun canCollide(collider: CollisionBody): Boolean

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
     * Executed when this object collides with another or enters a trigger.
     * Instances can override this to provide custom behavior.
     *
     * @param event the information about the collision
     */
    fun onCollisionEvent(event: CollisionEvent)
    // TODO allow adding callbacks from script

    // Object Overrides

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int

}