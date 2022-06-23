package slavsquatsuperstar.mayonez.physics2d.colliders

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.math.geom.Rectangle
import slavsquatsuperstar.math.geom.Shape
import slavsquatsuperstar.mayonez.Component
import slavsquatsuperstar.mayonez.GameObject
import slavsquatsuperstar.mayonez.graphics.DebugDraw
import slavsquatsuperstar.mayonez.physics2d.PhysicsMaterial
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D
import slavsquatsuperstar.mayonez.physics2d.collision.Manifold
import slavsquatsuperstar.mayonez.physics2d.collision.RaycastResult
import java.awt.Color
import java.awt.Graphics2D

/**
 * A shape that takes up space and can detect collisions. Requires a [Rigidbody2D] to respond to collisions
 * properly.
 *
 * @author SlavSquatSuperstar
 */
@Suppress("UNCHECKED_CAST")
abstract class Collider2D(private val shapeData: Shape) : Component() {

    // Object References

    /**
     * A reference to the parent object's [Rigidbody2D]. A collider should have a rigidbody to react to collisions.
     */
    @JvmField
    protected var rb: Rigidbody2D? = null

    /**
     * Returns the parent object's [Rigidbody2D].
     *
     * @return the attached rigidbody
     */
    fun getRigidbody(): Rigidbody2D? = rb

    fun <T : Collider2D?> setRigidbody(rb: Rigidbody2D?): T {
        this.rb = rb
        return this as T
    }

    // Physics Engine Properties

    var material: PhysicsMaterial = PhysicsMaterial.DEFAULT_MATERIAL
        private set

    fun setMaterial(material: PhysicsMaterial): Collider2D {
        this.material = material
        return this
    }

    var isTrigger = false
        /**
         * Returns whether this collider is non-physical and should not react to collisions.
         *
         * @return if this collider is a trigger
         */
        @JvmName("isTrigger") get
        private set

    fun setTrigger(trigger: Boolean): Collider2D {
        isTrigger = trigger
        return this
    }

    /**
     * If this frame's collision in [onCollision] should not be resolved by the physics engine.
     */
    var ignoreCurrentCollision = false

    // Debug Draw Properties

    /**
     * What color this collider should appear on the screen as. Set to null to disable drawing.
     */
    var drawColor: Color? = null
        private set

    /**
     * Set this shape' color for [DebugDraw], or disable debug drawing for this shape.
     *
     * @param color the draw color (disables drawing if null)
     */
    fun <T : Collider2D?> setDrawColor(color: Color?): T {
        this.drawColor = color
        return this as T
    }

    // Game Loop Methods

    override fun start() {
        rb = parent.getComponent(Rigidbody2D::class.java)
    }

    override fun render(g2: Graphics2D?) {
        DebugDraw.drawShape(this, drawColor ?: return)
    }

    // Shape Properties

    // TODO convert to property
    fun center(): Vec2 = transform!!.position

    open fun getRotation(): Float = transform!!.rotation

    abstract fun getMinBounds(): Rectangle

    open fun getMass(density: Float): Float = shapeData.scale(transform!!.scale).mass(density)

    open fun getAngMass(mass: Float): Float = shapeData.scale(transform!!.scale).angularMass(mass)

    // Transform Methods

    /**
     * Transforms this shape into world space.
     */
    protected open fun transformToWorld(): Shape {
        return shapeData.rotate(getRotation()).scale(transform!!.scale).translate(center())
    }

    // inverse scale, rotate about our center
    /**
     * Transforms another shape to this shape's local space.
     */
    protected open fun transformToLocal(world: Shape): Shape {
        return world.translate(-center()).scale(transform!!.scale.reciprocal(), centered = false)
            .rotate(-getRotation())
    }

//    open fun toLocal(world: Vec2): Vec2 = transform?.toLocal(world) ?: world

//    open fun toWorld(local: Vec2): Vec2 = transform?.toWorld(local) ?: local

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
    abstract fun getCollisionInfo(collider: Collider2D?): Manifold?

// Field Getters and Setters

    /**
     * Whether this collider has a null or infinite-mass rigidbody, and does not respond to collisions.
     *
     * @return if this collider is not affected by collisions.
     */
    fun isStatic(): Boolean = rb?.hasInfiniteMass() ?: true

// Callback Methods

    /**
     * A callback event broadcasted when two solid objects collide with each other.
     *
     * @param other the other game object
     */
    fun onCollision(other: GameObject) = parent.onCollision(other)

    /**
     * A callback event broadcasted when one solid object enters a trigger area.
     *
     * @param trigger the trigger
     */
    fun onTrigger(trigger: Collider2D) = parent.onTrigger(trigger)

    override fun destroy() {
        ignoreCurrentCollision = true
    }

}