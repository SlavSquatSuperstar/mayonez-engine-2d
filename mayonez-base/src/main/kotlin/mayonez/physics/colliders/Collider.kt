package mayonez.physics.colliders

import mayonez.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.physics.*
import mayonez.physics.dynamics.*
import mayonez.physics.manifold.*

/**
 * A shape centered around the object's position that detects collisions.
 * Colliders require a [Rigidbody] to respond to collisions properly.
 *
 * @param shape the shape object that stores the vertices and the shape's
 *     properties
 * @constructor Constructs a collider from a [Shape] object
 * @author SlavSquatSuperstar
 */
open class Collider(shape: Shape) :
    Node(), CollisionBody {

    // Component References

    override var physicsBody: PhysicsBody? = null

    // Physics Engine Properties

    // Make sure shape centroid equals center of mass
    private val shape: Shape = shape.translate(-shape.center())

    final override var trigger: Boolean = false
        private set

    /** Set this collider to be a non-physical trigger area. */
    fun setTrigger(trigger: Boolean): Collider {
        this.trigger = trigger
        return this
    }

    override var layer: PhysicsLayer? = null

    /**
     * Whether the position or velocity of this collider has been modified in
     * this frame.
     */
    override var collisionResolved: Boolean = false

    // Game Loop Methods

    override fun start() {
        // We want the physics body to be the parent of the collider
        physicsBody = parent as? PhysicsBody?

        // The old architecture has the physics body and collider as siblings
        if (physicsBody == null) {
            physicsBody = getSibling(Rigidbody::class.java)
            setUseParentTransform(true)
        }
    }

    // Shape Properties

    fun center(): Vec2 = globalTransform.position

    open fun getRotation(): Float = globalTransform.rotation

    override fun getMinBounds(): BoundingBox = getShape().boundingBox()

    override fun getMass(density: Float): Float {
        return shape.scale(globalTransform.scale).mass(density)
    }

    override fun getAngMass(mass: Float): Float {
        return shape.scale(globalTransform.scale).angularMass(mass)
    }

    override fun getShape(): Shape {
        return shape.transform(globalTransform)
    }

    // Shape vs Point Collisions

    /**
     * Calculates whether the given point is on or inside this shape.
     *
     * @param point a vector
     * @return if the shape contains the point
     */
    operator fun contains(point: Vec2): Boolean = (point in getShape())

    // Collision Methods

    override fun shouldUpdate(): Boolean = super.shouldUpdate()

    override fun getContacts(collider: CollisionBody?): Manifold? {
        return Collisions.getContacts(this.getShape(), collider?.getShape())
    }

    override fun canCollide(collider: CollisionBody): Boolean {
        return (this.shouldUpdate() && collider.shouldUpdate()) // Both enabled
                && (this.layer?.canInteract(collider.layer) ?: true) // Layers interact
                && !(this.physicsBody == null && collider.physicsBody == null) // At most one is static
    }

    // Collision Event Methods

    override fun onCollisionEvent(event: CollisionEvent) {
    }

}