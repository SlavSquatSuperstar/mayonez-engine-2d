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
    private var collider: Collider? = null

    // Mass Properties

    var mass: Float = 0f.coerceAtLeast(mass) // positive mass
        set(mass) {
            field = 0f.coerceAtLeast(mass)
        }
    val infiniteMass: Boolean
        get() = MathUtils.equals(mass, 0f)
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

    var velocity: Vec2 = Vec2()
    val speed: Float
        get() = velocity.len()
    var angVelocity: Float = 0f

    // Dynamics Properties (Force, Drag)

    private var netForce: Vec2 = Vec2()
    private var netTorque: Float = 0f

    var drag: Float = drag
        set(drag) {
            field = clamp(drag, 0f, 1f)
        }
    var angDrag: Float = angDrag
        set(angDrag) {
            field = clamp(angDrag, 0f, 1f)
        }

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

        if (!MathUtils.equals(velocity.lenSq(), 0f)) // Apply drag first
            applyForce(velocity * -drag)
        velocity += netForce * (invMass * dt)

        if (!fixedRotation) {
            if (!MathUtils.equals(angVelocity, 0f))
                applyTorque(angVelocity * -angDrag)
            angVelocity += netTorque * invAngMass * dt
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
        if (!fixedRotation) transform.rotate(toDegrees(angVelocity) * dt)
    }

    // Apply Force/Torque Methods

    /**
     * Applies a force to this body's center of mass.
     *
     * @param force a vector with the units `kg•m/s/s`
     */
    fun applyForce(force: Vec2?) {
        netForce += force ?: return
    }

    /**
     * Applies a torque to this body's center of mass in the clockwise direction.
     *
     * @param torque a scalar with units `kg•m•m/s/s`
     */
    fun applyTorque(torque: Float?) {
        netTorque += torque ?: return
    }

    /**
     * Applies an impulse to the body's center of mass.
     *
     * @param impulse a vector with the units `kg•m/s`
     */
    fun applyImpulse(impulse: Vec2?) {
        velocity += (impulse ?: return) * invMass // dv = J/m = m*dv/m
    }

    /**
     * Applies an angular impulse to this body in the clockwise direction.
     *
     * @param impulse a scalar with units `deg/s`
     */
    fun applyAngularImpulse(impulse: Float) {
        angVelocity += impulse * invAngMass // dw = T/I = I*dw/I
    }

    /**
     * Accelerates this body in the given direction.
     *
     * @param acceleration a vector with the units `m/s/s`
     */
    fun applyAcceleration(acceleration: Vec2?) {
        netForce += (acceleration ?: return) * invMass // dF = a/m
    }

    /**
     * Accelerates this body rotationally in the given direction.
     *
     * @param angAcceleration a vector with the units `deg/s/s`
     */
    fun applyAngularAcceleration(angAcceleration: Float) {
        netTorque += angAcceleration * invAngMass // dT = A/I
    }

    /**
     * Adds a velocity to this body in the given direction.
     *
     * @param velocityChange a vector with the units `m/s`
     */
    fun addVelocity(velocityChange: Vec2?) {
        velocity += velocityChange ?: return
    }

    /**
     * Adds an angular velocity to this body in the clockwise direction.
     *
     * @param angVelocityChange a scalar with units `deg/s`
     */
    fun addAngularVelocity(angVelocityChange: Float?) {
        angVelocity += angVelocityChange ?: return
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
    fun getPointVelocity(point: Vec2): Vec2 = velocity + (point - position).cross(toRadians(-angVelocity))

}