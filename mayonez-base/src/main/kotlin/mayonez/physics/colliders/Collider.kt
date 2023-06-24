package mayonez.physics.colliders

import mayonez.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.physics.*
import mayonez.physics.manifold.*

/**
 * A shape centered around the object's position that can detect collisions
 * with other shapes. Requires a [Rigidbody] to respond to collisions
 * properly.
 *
 * @param shapeData the shape object that stores the vertices and the
 *     shape's properties
 * @constructor Constructs a collider from a [Shape] object
 * @author SlavSquatSuperstar
 */
abstract class Collider(private val shapeData: Shape) : Component() {

    // Object References

    /**
     * A reference to the parent object's [Rigidbody]. A collider should have a
     * rigidbody to react to collisions.
     */
    var rigidbody: Rigidbody? = null

//    lateinit var shapeDrawer: ShapeDrawer

    // Physics Engine Properties

    var trigger = false
        /**
         * Returns whether this collider is non-physical and should not react to
         * collisions.
         *
         * @return if this collider is a trigger
         */
        @JvmName("isTrigger") get
        private set

    fun setTrigger(trigger: Boolean): Collider {
        this.trigger = trigger
        return this
    }

    /**
     * Whether this collider has a null or infinite-mass rigidbody, and does
     * not respond to collisions.
     *
     * @return if this collider is not affected by collisions.
     */
    fun isStatic(): Boolean = rigidbody?.infiniteMass ?: true

    /**
     * If this frame's collision in [sendCollisionEvent] should not be resolved
     * by the physics engine.
     */
    var ignoreCurrentCollision: Boolean = false

    /**
     * Whether the position or velocity of this collider has been modified in
     * this frame.
     */
    var collisionResolved: Boolean = false

    // Game Loop Methods

    override fun start() {
        rigidbody = gameObject.getComponent(Rigidbody::class.java)
    }

    // Shape Properties

    // TODO convert to property
    fun center(): Vec2 = transform!!.position

    open fun getRotation(): Float = transform!!.rotation

    open fun getMinBounds(): BoundingBox = transformToWorld().boundingRectangle()

    open fun getMass(density: Float): Float = shapeData.scale(transform!!.scale).mass(density)

    open fun getAngMass(mass: Float): Float = shapeData.scale(transform!!.scale).angularMass(mass)

    // Transform Methods

    /** Transforms this shape into world space. */
    // TODO save as mutable field
    open fun transformToWorld(): Shape {
        return shapeData.rotate(getRotation()).scale(transform!!.scale).translate(center())
    }

    // inverse scale, rotate about our center
    /** Transforms another shape to this shape's local space. */
    protected open fun transformToLocal(world: Shape): Shape {
        return world.translate(-center()).scale(Vec2(1f) / (transform!!.scale), origin = Vec2())
            .rotate(-getRotation())
    }

    // Shape vs Point Collisions

    /**
     * Calculates whether the given point is on or inside this shape.
     *
     * @param point a vector
     * @return if the shape contains the point
     */
    operator fun contains(point: Vec2): Boolean = (point in transformToWorld())

    // Shape vs Shape Collisions

    /**
     * Calculates the contact points, normal, and overlap between this collider
     * and another if they are intersecting.
     *
     * @param collider another collider
     * @return the collision info, or no if there is no intersection
     */
    fun getContacts(collider: Collider?): Manifold? {
        return Collisions.getContacts(this.transformToWorld(), collider?.transformToWorld())
    }

    // Callback Methods

    /**
     * Broadcasts an event if a collision occurs between this object and
     * another.
     *
     * @param other the other object
     * @param trigger if interacting with a trigger
     * @param type the type of the collision given by the listener
     */
    fun sendCollisionEvent(other: Collider, trigger: Boolean, type: CollisionEventType) {
        gameObject?.onCollisionEvent(other.gameObject ?: return, trigger, type)
    }

}