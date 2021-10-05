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

    // Constructors
    constructor(mass: Float) : this(mass, 0f, 0f)

    // Component Properties

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
    private var fixedRotation: Boolean = false

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

    // Game Loop Methods

    override fun start() {
        collider = parent.getComponent(Collider2D::class.java)
        if (collider is AlignedBoxCollider2D) fixedRotation = true
    }

    /**
     * Integrate force and torque to solve for velocity and angular velocity
     */
    fun integrateForce(dt: Float) {
        if (hasInfiniteMass()) return

        if (!MathUtils.equals(velocity.lenSquared(), 0f)) // Apply drag first
            addForce(velocity * -drag)
        velocity += netForce * (invMass * dt)


        if (!fixedRotation) {
            if (!MathUtils.equals(angVelocity, 0f))
                addTorque(angVelocity * -angDrag)
            angVelocity += netTorque * invAngMass * dt
        }

        // Reset accumulated forces/torques
        netForce.set(0f, 0f)
        netTorque = 0f
    }

    /**
     * Integrate velocity and angular velocity to solve for position and rotation
     */
    fun integrateVelocity(dt: Float) {
        transform?.move(velocity * dt)
        if (!fixedRotation) transform?.rotate(toDegrees(angVelocity) * dt)
    }

    // Motion Properties

    var position: Vec2
        get() = transform?.position ?: Vec2()
        set(position) {
            transform?.position = position
        }

    var rotation: Float
        get() = transform?.rotation ?: 0f
        set(rotation) {
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
        get() = if (hasInfiniteMass()) 0f else 1f / mass

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
            return 1f / angMass
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
        velocity += impulse * invMass // dv = J/m = m*dv/m
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
    fun getRelativePointVelocity(point: Vec2): Vec2 = (point - position).rotate(90f) * toRadians(angVelocity)

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