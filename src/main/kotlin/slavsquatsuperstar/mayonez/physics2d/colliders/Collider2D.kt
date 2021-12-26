package slavsquatsuperstar.mayonez.physics2d.colliders

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Component
import slavsquatsuperstar.mayonez.Logger
import slavsquatsuperstar.mayonez.Transform
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold
import slavsquatsuperstar.mayonez.physics2d.PhysicsMaterial
import slavsquatsuperstar.mayonez.physics2d.RaycastResult
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D

/**
 * A shape that takes up space and can detect collisions. Requires a [Rigidbody2D] to respond to collisions
 * properly.
 *
 * @author SlavSquatSuperstar
 */
@Suppress("UNCHECKED_CAST")
abstract class Collider2D : Component() {

    var ignoreCurrentCollision = false

    // Object References

    fun <T : Collider2D?> setTransform(transform: Transform?): T {
        this.transform = transform
        return this as T
    }

    /**
     * A reference to the parent object's [Rigidbody2D]
     */
    @JvmField
    protected var rb: Rigidbody2D? = null

    /**
     * Returns the parent object's [Rigidbody2D]. A collider should have rigidbody to react to collisions.
     *
     * @return the attached rigidbody
     */
    fun getRigidbody(): Rigidbody2D? = rb

    fun <T : Collider2D?> setRigidbody(rb: Rigidbody2D?): T {
        this.rb = rb
        return this as T
    }

    // Physics Properties

    var material: PhysicsMaterial = PhysicsMaterial.DEFAULT_MATERIAL
        private set

    fun setMaterial(material: PhysicsMaterial): Collider2D {
        this.material = material
        return this
    }

    var isTrigger = false
        /**
         * Returns this collider is non-physical and should not react to collisions.
         */
        @JvmName("isTrigger") get
        private set

    fun setTrigger(trigger: Boolean): Collider2D {
        isTrigger = trigger
        return this
    }

    // Game Loop Methods

    override fun start() {
        rb = parent.getComponent(Rigidbody2D::class.java)
        if (rb == null) Logger.log("%s needs a rigidbody to function properly!", this)
    }

    // Shape Properties

    fun center(): Vec2 = transform?.position ?: Vec2()

    abstract fun getMinBounds(): AlignedBoxCollider2D

    open fun getAngMass(mass: Float): Float = mass

    // Shape vs Point Collisions
    /**
     * Calculates whether the given point is on or inside this shape.
     *
     * @param point a vector
     * @return if the shape contains the point
     */
    abstract operator fun contains(point: Vec2): Boolean

    /**
     * Returns the point on or inside the collider nearest to the given position.
     *
     * @param position a 2D vector
     * @return the point
     */
    abstract fun nearestPoint(position: Vec2): Vec2?

    // Shape vs Line Collisions

    /**
     * Calculates whether the given [Edge2D] touches or passes through this collider.
     *
     * @param edge a line segment
     * @return if the edge intersects this shape
     */
    open fun intersects(edge: Edge2D): Boolean {
        return if (edge.start in this || edge.end in this) true
        else raycast(Ray2D(edge), edge.length) != null
    }

    /**
     * Casts a [Ray2D] onto this collider and calculates where the ray intersects the collider.
     *
     * @param ray    a 2D ray
     * @param limit  The maximum distance the ray is allowed to travel before hitting an object. Set to 0 to allow the
     * ray to travel infinitely. Should be positive otherwise.
     * @return if the ray intersects this shape
     */
    abstract fun raycast(ray: Ray2D, limit: Float): RaycastResult?

    // Shape vs Shape Collisions

    /**
     * Detects whether this collider is touching or overlapping another.
     *
     * @param collider another collider
     * @return if there is a collision
     */
    open fun detectCollision(collider: Collider2D?): Boolean = getCollisionInfo(collider) != null

    /**
     * Calculates the contact points, normal, and overlap between this collider and another if they are intersecting.
     *
     * @param collider another collider
     * @return the collision info, or no if there is no intersection
     */
    abstract fun getCollisionInfo(collider: Collider2D?): CollisionManifold?

    // Transform Methods

    open fun toLocal(world: Vec2): Vec2 = transform?.toLocal(world) ?: world

    open fun toWorld(local: Vec2): Vec2 = transform?.toWorld(local) ?: local

    // Field Getters and Setters

    /**
     * Returns whether this collider has a null or static rigidbody, and thus does not respond to collisions.
     *
     * @return if this collider is not affected by collisions.
     */
    fun isStatic(): Boolean = rb?.hasInfiniteMass() ?: true

    // Callback Methods

    fun onCollision(collision: CollisionManifold) = parent.onCollision(collision)

    fun onTrigger(trigger: Collider2D) = parent.onTrigger(trigger)

    override fun destroy() {
        ignoreCurrentCollision = true
    }

}