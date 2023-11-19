package mayonez.physics.colliders

import mayonez.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.physics.*
import mayonez.physics.dynamics.*
import mayonez.physics.manifold.*

/**
 * A shape centered around the object's position
 * that detects collisions. Colliders require a
 * [mayonez.physics.dynamics.Rigidbody] to respond to collisions properly.
 *
 * @param shape the shape object that stores the vertices and the shape's
 *     properties
 * @constructor Constructs a collider from a [Shape] object
 * @author SlavSquatSuperstar
 */
abstract class Collider(private val shape: Shape) : Component(UpdateOrder.COLLISION), CollisionBody {

    // Component References

    override var physicsBody: PhysicsBody? = null

    /**
     * A reference to the parent object's [mayonez.physics.dynamics.Rigidbody].
     * A collider should have a rigidbody to react to collisions.
     */
    fun getRigidbody(): Rigidbody? = physicsBody as Rigidbody?

    // Physics Engine Properties

    final override var trigger: Boolean = false
        private set

    /** Set this collider to be a non-physical trigger area. */
    fun setTrigger(trigger: Boolean): Collider {
        this.trigger = trigger
        return this
    }

    /**
     * Whether the position or velocity of this collider has been modified in
     * this frame.
     */
    override var collisionResolved: Boolean = false

    // Game Loop Methods

    override fun start() {
        physicsBody = gameObject.getComponent(Rigidbody::class.java)
    }

    // Shape Properties

    // TODO convert to property
    fun center(): Vec2 = transform!!.position

    open fun getRotation(): Float = transform!!.rotation

    override fun getMinBounds(): BoundingBox = getShape().boundingRectangle()

    override fun getMass(density: Float): Float {
        return shape.scale(transform!!.scale).mass(density)
    }

    override fun getAngMass(mass: Float): Float {
        return shape.scale(transform!!.scale).angularMass(mass)
    }

    // Transform Methods

    // TODO save as mutable field
    override fun getShape(): Shape {
        return shape.rotate(getRotation())
            .scale(transform!!.scale)
            .translate(center())
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

    override fun getContacts(collider: CollisionBody?): Manifold? {
        return Collisions.getContacts(this.getShape(), collider?.getShape())
    }

    override fun canCollide(collider: CollisionBody): Boolean {
        if (collider is Collider) {

            return this.isEnabled && collider.isEnabled // Both enabled
                    || this.gameObject.canInteract(collider.gameObject) // Layers interact
                    || this.physicsBody != null || collider.physicsBody != null // At most one is static
        }
        return false
    }

    private fun GameObject.canInteract(other: GameObject): Boolean {
        return (this.layer == null) || this.layer.canInteract(other.layer)
    }

    // Callback Methods

    override fun sendCollisionEvent(
        other: CollisionBody, trigger: Boolean,
        type: CollisionEventType, direction: Vec2?
    ) {
        if (gameObject == null) return
        if (other is Collider) {
            if (other.gameObject == null) return

            this.gameObject.onCollisionEvent(
                CollisionEvent(
                    other.gameObject, trigger, type, direction
                )
            )
        }
    }

}