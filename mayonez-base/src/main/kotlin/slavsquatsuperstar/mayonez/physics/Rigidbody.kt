package slavsquatsuperstar.mayonez.physics

import slavsquatsuperstar.mayonez.Component
import slavsquatsuperstar.mayonez.math.MathUtils
import slavsquatsuperstar.mayonez.math.MathUtils.clamp
import slavsquatsuperstar.mayonez.math.MathUtils.toDegrees
import slavsquatsuperstar.mayonez.math.MathUtils.toRadians
import slavsquatsuperstar.mayonez.math.Vec2
import slavsquatsuperstar.mayonez.physics.colliders.Collider

/**
 * A physical object with mass that responds to forces and collisions. Rigid bodies do not deform when forces are
 * applied to them.
 *
 * @author SlavSquatSuperstar
 */
class Rigidbody(mass: Float, drag: Float, angDrag: Float) : Component() {

    constructor(mass: Float) : this(mass, 0.05f, 0.05f)

    // Component References

    /**
     * Returns the parent object's [Collider]. May be null.
     *
     * @return the attached collider
     */
    var collider: Collider? = null
        private set

    // Physics Properties
    var mass: Float = mass
        set(mass) {
            field = 0f.coerceAtLeast(mass)
        }

    var material: PhysicsMaterial = PhysicsMaterial.DEFAULT_MATERIAL
        private set

    fun setMaterial(material: PhysicsMaterial): Rigidbody {
        this.material = material
        return this
    }

    var followsGravity: Boolean = true
        @JvmName("isFollowsGravity") get
        private set

    var fixedRotation: Boolean = false
        @JvmName("isFixedRotation") get
        private set

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
        collider = gameObject.getComponent(Collider::class.java)
    }

    /**
     * Integrate force and torque to solve for velocity and angular velocity
     */
    fun integrateForce(dt: Float) {
        if (hasInfiniteMass()) return

        if (!MathUtils.equals(velocity.lenSq(), 0f)) // Apply drag first
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
        transform.move(velocity * dt)
        if (!fixedRotation) transform.rotate(toDegrees(angVelocity) * dt)
    }

    // Motion Properties

    var position: Vec2
        get() = transform.position
        set(position) {
            transform.position = position
        }

    var rotation: Float
        get() = transform.rotation
        set(rotation) {
            transform.rotation = rotation
        }

    val speed: Float
        get() = velocity.len()

    fun setFollowsGravity(followsGravity: Boolean): Rigidbody {
        this.followsGravity = followsGravity
        return this
    }

    fun setFixedRotation(fixedRotation: Boolean): Rigidbody {
        this.fixedRotation = fixedRotation
        return this
    }

    // Mass Properties

    fun hasInfiniteMass(): Boolean = MathUtils.equals(mass, 0f)

    val invMass: Float
        get() = if (hasInfiniteMass()) 0f else 1f / mass

    val angMass: Float
        get() = collider?.getAngMass(mass) ?: mass

    val invAngMass: Float
        get() = if (hasInfiniteMass()) 0f else 1f / angMass

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

    /**
     * Calculates the tangential velocity of a rotating point on this body using the relationship v_T = r_perp * w.
     */
    fun getRelativePointVelocity(point: Vec2): Vec2 = (point - position).normal() * toRadians(angVelocity)

    /**
     * Calculates the velocity of this body's center plus the relative velocity the given point.
     */
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

}