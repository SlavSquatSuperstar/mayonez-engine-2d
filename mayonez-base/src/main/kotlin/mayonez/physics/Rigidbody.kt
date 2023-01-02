package mayonez.physics

import mayonez.Component
import mayonez.math.FloatMath
import mayonez.math.FloatMath.clamp
import mayonez.math.Vec2
import mayonez.physics.colliders.Collider
import kotlin.math.abs

/**
 * A solid, massive object that responds to forces and collisions. Rigid bodies do not deform when forces are
 * applied to them.
 *
 * @author SlavSquatSuperstar
 */
class Rigidbody(mass: Float, drag: Float, angDrag: Float) : Component() {

    constructor(mass: Float) : this(mass, 0f, 0f)

    // Component References

    /**
     * Returns the parent object's [Collider]. May be null.
     *
     * @return the attached collider
     */
    private var collider: Collider? = null

    // Mass Properties

    var mass: Float = 0f.coerceAtLeast(mass) // positive mass
        set(mass) {
            field = 0f.coerceAtLeast(mass)
        }
    val infiniteMass: Boolean
        get() = FloatMath.equals(mass, 0f)
    val invMass: Float
        get() = if (infiniteMass) 0f else 1f / mass

    val angMass: Float
        get() = collider?.getAngMass(mass) ?: mass
    val invAngMass: Float
        get() = if (infiniteMass) 0f else 1f / angMass

    // Kinematics Properties (Position, Velocity)

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

    /**
     * The linear (translational) velocity of the body, v, in world units.
     */
    var velocity: Vec2 = Vec2()
    val speed: Float
        get() = velocity.len()

    /**
     * The angular (rotational) velocity of the body, ω, in degrees counterclockwise.
     */
    var angVelocity: Float = 0f
    val angSpeed: Float
        get() = abs(angVelocity)

    // Dynamics Properties (Force, Drag)

    private var netForce: Vec2 = Vec2()
    private var netTorque: Float = 0f

    /**
     * The body's drag, which represents a damping force proportional to its negative linear velocity. A drag of 0 means
     * all velocity is conserved during motion and 1 means all velocity is quickly lost without acceleration.
     */
    var drag: Float = clamp(drag, 0f, 1f)
//        set(drag) {
//            field = clamp(drag, 0f, 1f)
//        }

    /**
     * The body's angular drag, which represents a damping torque proportional to its negative angular velocity. A drag
     * of 0 means all velocity is conserved during motion and 1 means all velocity is quickly lost without acceleration.
     */
    var angDrag: Float = clamp(angDrag, 0f, 1f)
//        set(angDrag) {
//            field = clamp(angDrag, 0f, 1f)
//        }

    // Physics Engine Properties

    var material: PhysicsMaterial = PhysicsMaterial.DEFAULT_MATERIAL
        private set

    fun setMaterial(material: PhysicsMaterial): Rigidbody {
        this.material = material
        return this
    }

    var followsGravity: Boolean = true
        @JvmName("isFollowsGravity") get
        private set

    fun setFollowsGravity(followsGravity: Boolean): Rigidbody {
        this.followsGravity = followsGravity
        return this
    }

    var fixedRotation: Boolean = false
        @JvmName("isFixedRotation") get
        private set

    fun setFixedRotation(fixedRotation: Boolean): Rigidbody {
        this.fixedRotation = fixedRotation
        return this
    }

    // Game Loop Methods

    override fun start() {
        collider = gameObject.getComponent(Collider::class.java)
    }

    /**
     * Integrate net force and torque to solve for velocity and angular velocity.
     */
    fun integrateForce(dt: Float) {
        if (infiniteMass) return

        // Apply drag first, setting velocity to 0 for small speeds
        if (FloatMath.equals(velocity.lenSq(), 0f, 0.0005f)) velocity.set(Vec2(0f))
        else netForce += -velocity * drag
        velocity += netForce * (invMass * dt)

        if (!fixedRotation) {
            if (FloatMath.equals(angVelocity, 0f, 0.0005f)) angVelocity = 0f
            else netTorque += -FloatMath.toRadians(angVelocity) * drag
            angVelocity += FloatMath.toDegrees(netTorque) * invAngMass * dt
        }

        // Reset accumulated forces/torques
        netForce.set(0f, 0f)
        netTorque = 0f
    }

    /**
     * Integrate linear velocity and angular velocity to solve for position and rotation.
     */
    fun integrateVelocity(dt: Float) {
        transform.move(velocity * dt)
        if (!fixedRotation) transform.rotate(angVelocity * dt)
    }

    // Apply Force/Torque Methods

    /**
     * Applies a force to this body's center of mass.
     *
     * @param force a vector with the units `kg•m/s/s (F)`
     */
    fun applyForce(force: Vec2?) {
        netForce += force ?: return
    }

    /**
     * Applies a torque to this body's center of mass in the clockwise direction.
     *
     * @param torque a scalar with units `kg•m•m/s/s (τ = F•d)`
     */
    fun applyTorque(torque: Float?) {
        netTorque += torque ?: return
    }

    /**
     * Applies an impulse to the body's center of mass.
     *
     * @param impulse a vector with the units `kg•m/s (F•t)`
     */
    fun applyImpulse(impulse: Vec2?) {
        velocity += (impulse ?: return) * invMass // dv = J/m = m*dv/m
    }

    /**
     * Applies an angular impulse to this body in the clockwise direction.
     *
     * @param angImpulse a scalar with units `kg•m•m•rad/s (τ•t = I•ω)`
     */
    fun applyAngularImpulse(angImpulse: Float) {
        angVelocity += FloatMath.toDegrees(angImpulse) * invAngMass // dw = L/I = I*dw/I
    }

    /**
     * Accelerates this body in the given direction.
     *
     * @param acceleration a vector with the units `m/s/s (a)`
     */
    fun applyAcceleration(acceleration: Vec2?) {
        netForce += (acceleration ?: return) * invMass // dF = a/m
    }

    /**
     * Accelerates this body in the given direction.
     *
     * @param angAcceleration a vector with the units `deg/s/s (α)`
     */
    fun applyAngularAcceleration(angAcceleration: Float) {
        netTorque += angAcceleration * invAngMass // dT = A/I
    }

    /**
     * Adds a velocity to this body in the given direction.
     *
     * @param velocity a vector with the units `m/s (v)`
     */
    fun addVelocity(velocity: Vec2?) {
        this.velocity += velocity ?: return
    }

    /**
     * Adds an angular velocity to this body in the clockwise direction.
     *
     * @param angVelocity a scalar with units `deg/s (ω)`
     */
    fun addAngularVelocity(angVelocity: Float?) {
        this.angVelocity += angVelocity ?: return
    }

    // Velocity at Point Methods

    /**
     * Calculates the linear velocity of this body's center plus the tangential velocity the given point using the
     * relationship v_t = r_perp * w or v_t = w x r.
     *
     * @param point a point on this body
     * @return the point's total velocity
     */
//    fun getPointVelocity(point: Vec2): Vec2 = velocity + (point - position).normal() * toRadians(angVelocity)
    fun getPointVelocity(point: Vec2): Vec2 = velocity + (point - position).cross(FloatMath.toRadians(-angVelocity))

}