package mayonez.physics.dynamics

import mayonez.*
import mayonez.math.*
import mayonez.math.FloatMath.clamp
import mayonez.physics.colliders.*
import kotlin.math.*

/**
 * A solid object that moves and responds to forces and collisions. Rigid
 * bodies do not deform when forces are applied to them.
 *
 * @author SlavSquatSuperstar
 */
class Rigidbody(mass: Float, drag: Float, angDrag: Float) : Component(), PhysicsBody {

    constructor(mass: Float) : this(mass, 0f, 0f)

    // Component References

    /**
     * Returns the parent object's [CollisionBody]. May be null.
     *
     * @return the attached collider
     */
    private var collider: CollisionBody? = null

    // Mass Properties

    override var mass: Float = 0f.coerceAtLeast(mass) // positive mass
        private set(mass) {
            field = 0f.coerceAtLeast(mass)
        }
    override val invMass: Float
        get() = if (static) 0f else 1f / mass

    override val angMass: Float
        get() = collider?.getAngMass(mass) ?: mass
    override val invAngMass: Float
        get() = if (static) 0f else 1f / angMass

    override val static: Boolean
        get() = FloatMath.equals(mass, 0f)

    // Kinematics Properties (Position, Velocity)

    override var position: Vec2
        get() = transform.position
        set(position) {
            transform.position.set(position)
        }
    override var rotation: Float
        get() = transform.rotation
        set(rotation) {
            transform.rotation = rotation
        }

    override var velocity: Vec2 = Vec2()
        set(velocity) {
            field.set(velocity)
        }
    override val speed: Float
        get() = velocity.len()


    override var angVelocity: Float = 0f
    override val angSpeed: Float
        get() = abs(angVelocity)

    // Dynamics Properties (Force, Drag)

    private var netForce: Vec2 = Vec2()
    private var netTorque: Float = 0f

    /**
     * The body's drag, which represents a damping force proportional to its
     * negative linear velocity. A drag of 0 means all velocity is conserved
     * during motion and 1 means all velocity is quickly lost without
     * acceleration.
     */
    private var drag: Float = clamp(drag, 0f, 1f)

    fun setDrag(drag: Float): Rigidbody {
        this.drag = clamp(drag, 0f, 1f)
        return this
    }

    /**
     * The body's angular drag, which represents a damping torque proportional
     * to its negative angular velocity. A drag of 0 means all velocity is
     * conserved during motion and 1 means all velocity is quickly lost without
     * acceleration.
     */
    private var angDrag: Float = clamp(angDrag, 0f, 1f)

    fun setAngDrag(angDrag: Float): Rigidbody {
        this.angDrag = clamp(angDrag, 0f, 1f)
        return this
    }

    // Physics Engine Properties

    override var material: PhysicsMaterial = PhysicsMaterial.DEFAULT_MATERIAL
        private set

    fun setMaterial(material: PhysicsMaterial): Rigidbody {
        this.material = material
        return this
    }

    private var followsGravity: Boolean = true

    /**
     * Set whether this object is affected by gravity.
     *
     * @param followsGravity if the body should follow gravity
     * @return this rigidbody
     */
    fun setFollowsGravity(followsGravity: Boolean): Rigidbody {
        this.followsGravity = followsGravity
        return this
    }

    private var fixedRotation: Boolean = false

    /**
     * Set whether this object should rotate.
     *
     * @param fixedRotation if the body should rotate
     * @return this rigidbody
     */
    fun setFixedRotation(fixedRotation: Boolean): Rigidbody {
        this.fixedRotation = fixedRotation
        return this
    }

    // Game Loop Methods

    override fun start() {
        collider = gameObject.getComponent(Collider::class.java)
    }

    override fun integrateForce(dt: Float, gravity: Vec2) {
        if (static) return

        if (followsGravity) applyForce(gravity * mass)

        if (FloatMath.equals(velocity.lenSq(), 0f, 0.0005f)) {
            // Zero out velocity for small speeds
            velocity.set(Vec2(0f))
        } else {
            // Apply drag first
            netForce -= velocity * drag
        }
        // Solve for velocity
        velocity += netForce * (invMass * dt)

        if (!fixedRotation) {
            if (FloatMath.equals(angVelocity, 0f, 0.0005f)) {
                angVelocity = 0f
            } else {
                netTorque -= FloatMath.toRadians(angVelocity) * drag
            }
            angVelocity += FloatMath.toDegrees(netTorque) * invAngMass * dt
        }

        // Reset accumulated forces/torques
        netForce.set(0f, 0f)
        netTorque = 0f
    }


    override fun integrateVelocity(dt: Float) {
        if (static) return
        transform.move(velocity * dt)
        if (!fixedRotation) transform.rotate(angVelocity * dt)
    }

    // Apply Force Methods

    override fun applyForce(force: Vec2?) {
        netForce += force ?: return
    }

    override fun applyImpulse(impulse: Vec2?) {
        velocity += (impulse ?: return) * invMass // dv = J/m = m*dv/m
    }

    override fun addVelocity(velocity: Vec2?) {
        this.velocity += velocity ?: return
    }

    // Apply Torque Methods

    override fun applyTorque(torque: Float) {
        netTorque += torque
    }

    override fun applyAngularImpulse(angImpulse: Float) {
        angVelocity += FloatMath.toDegrees(angImpulse) * invAngMass // dw = L/I = I*dw/I
    }

    override fun addAngularVelocity(angVelocity: Float) {
        this.angVelocity += angVelocity
    }

}