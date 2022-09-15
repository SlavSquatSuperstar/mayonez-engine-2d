package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Component
import slavsquatsuperstar.mayonez.GameObject
import slavsquatsuperstar.mayonez.graphics.DebugDraw
import slavsquatsuperstar.mayonez.physics.PhysicsMaterial
import slavsquatsuperstar.mayonez.physics.Rigidbody
import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo
import slavsquatsuperstar.mayonez.physics.collision.Collisions
import slavsquatsuperstar.mayonez.physics.shapes.BoundingBox
import slavsquatsuperstar.mayonez.physics.shapes.Shape
import java.awt.Color
import java.awt.Graphics2D

/**
 * A shape centered around the object's position that can detect collisions with other shapes. Requires a [Rigidbody]
 * to respond to collisions properly.
 *
 * @constructor Constructs a collider from a [Shape] object
 * @param shapeData the shape object that stores the vertices and the shape's properties
 *
 * @author SlavSquatSuperstar
 */
@Suppress("UNCHECKED_CAST")
abstract class Collider(private val shapeData: Shape) : Component() {

    // Object References

    /**
     * A reference to the parent object's [Rigidbody]. A collider should have a rigidbody to react to collisions.
     */
    var rigidbody: Rigidbody? = null

    // Physics Engine Properties

    var material: PhysicsMaterial = PhysicsMaterial.DEFAULT_MATERIAL
        private set

    fun setMaterial(material: PhysicsMaterial): Collider {
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

    fun setTrigger(trigger: Boolean): Collider {
        isTrigger = trigger
        return this
    }

    /**
     * If this frame's collision in [onCollision] should not be resolved by the physics engine.
     */
    var ignoreCurrentCollision = false

    /**
     * Whether the position or velocity of this collider has been modified in this frame.
     */
    var collisionResolved: Boolean = false


    // Debug Draw Properties

    /**
     * What color this collider should appear on the screen as. Set to null to disable drawing.
     */
    var drawColor: Color? = null
        private set

    var fillOption: Boolean = false
        private set

    /**
     * Set this shape's color and fill option for [DebugDraw], or disable debug drawing for this shape.
     *
     * @param color the draw color (disables drawing if null)
     */
    fun <T : Collider?> setDebugDraw(color: Color?, fill: Boolean): T {
        this.drawColor = color
        this.fillOption = fill
        return this as T
    }

    // Game Loop Methods

    override fun start() {
        rigidbody = parent.getComponent(Rigidbody::class.java)
    }

    override fun render(g2: Graphics2D?) {
        if (drawColor == null) return
        if (fillOption) DebugDraw.fillShape(transformToWorld(), drawColor)
        else DebugDraw.drawShape(transformToWorld(), drawColor)
    }

    // Shape Properties

    // TODO convert to property
    fun center(): Vec2 = transform!!.position

    open fun getRotation(): Float = transform!!.rotation

    open fun getMinBounds(): BoundingBox = transformToWorld().boundingRectangle()

    open fun getMass(density: Float): Float = shapeData.scale(transform!!.scale).mass(density)

    open fun getAngMass(mass: Float): Float = shapeData.scale(transform!!.scale).angularMass(mass)

    // Transform Methods

    /**
     * Transforms this shape into world space.
     */
    // TODO save as mutatable field
    protected open fun transformToWorld(): Shape {
        return shapeData.rotate(getRotation()).scale(transform!!.scale).translate(center())
    }

    // inverse scale, rotate about our center
    /**
     * Transforms another shape to this shape's local space.
     */
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
     * Calculates the contact points, normal, and overlap between this collider and another if they are intersecting.
     *
     * @param collider another collider
     * @return the collision info, or no if there is no intersection
     */
    fun getCollisionInfo(collider: Collider?): CollisionInfo? {
        return Collisions.getContacts(this.transformToWorld(), collider?.transformToWorld())
    }

    // Field Getters and Setters

    /**
     * Whether this collider has a null or infinite-mass rigidbody, and does not respond to collisions.
     *
     * @return if this collider is not affected by collisions.
     */
    fun isStatic(): Boolean = rigidbody?.hasInfiniteMass() ?: true

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
    fun onTrigger(trigger: Collider) = parent.onTrigger(trigger)

    override fun destroy() {
        ignoreCurrentCollision = true
    }

}