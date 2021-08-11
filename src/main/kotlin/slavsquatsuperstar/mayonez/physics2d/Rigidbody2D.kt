package slavsquatsuperstar.mayonez.physics2d

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.MathUtils.clamp
import slavsquatsuperstar.math.MathUtils.pythagoreanSquared
import slavsquatsuperstar.math.MathUtils.toDegrees
import slavsquatsuperstar.math.MathUtils.toRadians
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Component
import slavsquatsuperstar.mayonez.GameObject
import slavsquatsuperstar.mayonez.Transform
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D
import slavsquatsuperstar.mayonez.physics2d.colliders.BoxCollider2D
import slavsquatsuperstar.mayonez.physics2d.colliders.CircleCollider
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D

/**
 * A physical object with mass that responds to forces and collisions.
 *
 * @author SlavSquatSuperstar
 */
class Rigidbody2D(mass: Float, drag: Float, angDrag: Float) : Component() {

    // Components
    /**
     * A reference to the parent object's [Transform].
     */
    var transform: Transform? = null

    /**
     * Returns the parent object's [Collider2D]. May be null.
     *
     * @return the attached collider
     */
    var collider: Collider2D? = null
        private set

    // Physics Properties
    var mass: Float = mass
        set(mass) {
            field = 0f.coerceAtLeast(mass)
        }
    private var followsGravity: Boolean = true
    private var fixedRotation: Boolean = true;

    // Linear Motion Fields
    private var netForce: Vec2 = Vec2()
    var velocity: Vec2 = Vec2()
    var drag: Float = drag
        set(drag) {
            field = clamp(drag, 0f, 1f)
        }

    // Angular Motion Fields
    private var netTorque: Float = 0f
    var angVelocity: Float = 0f
    var angDrag: Float = angDrag
        set(angDrag) {
            field = clamp(angDrag, 0f, 1f)
        }

    // Constructors

    constructor(mass: Float) : this(mass, 0f, 0f)

    // Game Loop Methods

    override fun start() {
        collider = parent.getComponent(Collider2D::class.java)
        if (collider is AlignedBoxCollider2D) fixedRotation = true
    }

    fun physicsUpdate(dt: Float) {
        if (hasInfiniteMass())
            return

        // Integrate forces and velocities, add drag if not stationary
        velocity += netForce * (invMass * dt) // Integrate velocity
        if (!MathUtils.equals(velocity.lenSquared(), 0f))
            addForce(velocity * -drag)
        transform?.move(velocity * dt)

        angVelocity += netTorque * invAngMass * dt
        if (!MathUtils.equals(angVelocity, 0f))
            addTorque(angVelocity * -angDrag)
        transform?.rotate(toDegrees(angVelocity) * dt)

        // Reset accumulated forces/torques
        netForce.set(0f, 0f)
        netTorque = 0f
    }

    // Motion Properties

    val position: Vec2
        get() = if (transform != null) transform!!.position else Vec2()

    fun setPosition(position: Vec2) {
        transform?.position = position
    }

    val rotation: Float
        get() = if (transform != null) transform!!.rotation else 0f

    fun setRotation(rotation: Float) {
        transform?.rotation = rotation
    }

    val speed: Float
        get() = velocity.len()

    fun isFollowsGravity(): Boolean = followsGravity

    fun setFollowsGravity(followsGravity: Boolean): Rigidbody2D {
        this.followsGravity = followsGravity
        return this
    }

    fun isFixedRotation(): Boolean = fixedRotation;

    fun setFixedRotation(fixedRotation: Boolean): Rigidbody2D {
        this.fixedRotation = fixedRotation
        return this
    }

    // Mass Properties

    fun hasInfiniteMass(): Boolean = MathUtils.equals(mass, 0f)

    val invMass: Float
        get() = if (hasInfiniteMass()) 0f else 1 / mass

    private val invAngMass: Float
        get() {
            if (collider == null || hasInfiniteMass())
                return invMass

            var angMass = mass
            when (collider) {
                is CircleCollider -> angMass *= MathUtils.PI * 0.5f * (collider as CircleCollider).radius() *
                        (collider as CircleCollider).radius()
                is BoxCollider2D -> angMass *= pythagoreanSquared(
                    (collider as BoxCollider2D).width(),
                    (collider as BoxCollider2D).height()
                ) / 12f
            }
            return 1 / angMass
        }

    // Force Methods

    /**
     * Applies a force to this body's center of mass.
     *
     * @param force a vector with the units `kg•m/s/s`
     */
    fun addForce(force: Vec2) {
        netForce += force
    }

    /**
     * Accelerates this body in the given direction.
     *
     * @param acceleration a vector with the units `m/s/s`
     */
    fun addAcceleration(acceleration: Vec2) {
        netForce += acceleration * invMass // dF = a/m
    }

    /**
     * Applies an impulse to the body's center of mass.
     *
     * @param impulse a vector with the units `kg•m/s`
     */
    fun addImpulse(impulse: Vec2) {
        velocity = velocity.add(impulse.mul(invMass)) // dv = J/m = m*dv/m
    }

    /**
     * Adds a velocity to this body in the given direction.
     *
     * @param velocityChange a vector with the units `m/s`
     */
    fun addVelocity(velocityChange: Vec2) {
        velocity += velocityChange
    }

    // Force/Velocity at Point Methods

    fun addForceAtPoint(force: Vec2, point: Vec2) {
        addForce(force)
        addTorque(point.sub(position).cross(force))
    }

    fun addImpulseAtPoint(impulse: Vec2, point: Vec2) {
        addImpulse(impulse)
        addAngularImpulse(point.sub(position).cross(impulse))
    }

    // convert angular velocity to linear velocity
    fun getRelativePointVelocity(point: Vec2): Vec2 = point.sub(position).rotate(90f) * toRadians(angVelocity)

    fun getPointVelocity(point: Vec2): Vec2 = velocity + getRelativePointVelocity(point)

    // Torque Methods

    /**
     * Applies a torque to this body's center of mass in the clockwise direction.
     *
     * @param torque a scalar with units `kg•m•m/s/s`
     */
    fun addTorque(torque: Float) {
        netTorque += torque
    }

    /**
     * Applies an angular impulse to this body in the clockwise direction.
     *
     * @param impulse a scalar with units `deg/s`
     */
    fun addAngularImpulse(impulse: Float) {
        angVelocity += impulse * invAngMass
    }

    /**
     * Adds an angular velocity to this body in the clockwise direction.
     *
     * @param velocityChange a scalar with units `deg/s`
     */
    fun addAngularVelocity(velocityChange: Float) {
        angVelocity += velocityChange
    }

    // Component Getters and Setters

    override fun setParent(parent: GameObject): Rigidbody2D {
        super.setParent(parent)
        transform = parent.transform
        return this
    }

}