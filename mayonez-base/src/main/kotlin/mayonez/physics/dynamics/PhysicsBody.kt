package mayonez.physics.dynamics

import mayonez.math.*

/**
 * An object in the world that has position, mass, and velocity, and responds
 * to forces.
 *
 * @author SlavSquatSuperstar
 */
interface PhysicsBody {

    // Mass Properties

    /** The mass of the object, m, in kilograms. */
    val mass: Float

    /** The inverse mass of the object, equal to 1/m. */
    val invMass: Float

    /** The angular mass (moment of inertia) of the object, I, in kg • m^2. */
    val angMass: Float

    /** The inverse angular mass of the object, I, equal to 1/I */
    val invAngMass: Float

    /** Whether the object is static and does not respond to forces. */
    val static: Boolean

    // Position Properties

    /** The absolute position of the object in the world, in meters. */
    val position: Vec2

    /** The absolute rotation of the object in the world, in degrees. */
    val rotation: Float

    // Velocity Properties

    /**
     * The linear (translational) velocity of the body, v, in meters per
     * second.
     */
    var velocity: Vec2

    /**
     * The linear speed of the body, or the magnitude of the velocity, in
     * meters per second.
     */
    val speed: Float

    /**
     * The angular (rotational) velocity of the body, ω, in degrees per second
     * counterclockwise.
     */
    var angVelocity: Float

    /**
     * The angular speed of the body, or the magnitude of the velocity, in
     * degrees per second.
     */
    val angSpeed: Float

    // Physics Engine Properties

    /**
     * The [PhysicsMaterial] of this object, which defines its friction and
     * bounce.
     */
    val material: PhysicsMaterial

    // Physics Loop Methods

    /**
     * Integrate net force and torque to solve for velocity and angular
     * velocity.
     *
     * @param dt the time since the last frame
     * @param gravity the gravitational acceleration
     */
    fun integrateForce(dt: Float, gravity: Vec2)

    /**
     * Integrate linear velocity and angular velocity to solve for position and
     * rotation.
     *
     * @param dt the time since the last frame
     */
    fun integrateVelocity(dt: Float)

    // Apply Force

    /**
     * Applies a force to this body's center of mass.
     *
     * @param force a vector with the units `kg•m/s/s (F)`
     */
    fun applyForce(force: Vec2?)

    /**
     * Applies an impulse to the body's center of mass.
     *
     * @param impulse a vector with the units `kg•m/s (F•t)`
     */
    fun applyImpulse(impulse: Vec2?)

    /**
     * Adds a velocity to this body in the given direction.
     *
     * @param velocity a vector with the units `m/s (v)`
     */
    fun addVelocity(velocity: Vec2?)

    // Apply Torque

    /**
     * Applies a torque to this body's center of mass in the clockwise
     * direction.
     *
     * @param torque a scalar with units `kg•m•m/s/s (τ = F•d)`
     */
    fun applyTorque(torque: Float)

    /**
     * Applies an angular impulse to this body in the clockwise direction.
     *
     * @param angImpulse a scalar with units `kg•m•m•rad/s (τ•t = I•ω)`
     */
    fun applyAngularImpulse(angImpulse: Float)

    /**
     * Adds an angular velocity to this body in the clockwise direction.
     *
     * @param angVelocity a scalar with units `deg/s (ω)`
     */
    fun addAngularVelocity(angVelocity: Float)

    // Velocity at Point

    /**
     * Calculates the linear velocity of this body's center plus the tangential
     * velocity the given point using the relationship v_t = r_perp * w or v_t
     * = w x r.
     *
     * @param point a point on this body
     * @return the point's total velocity
     */
    fun getPointVelocity(point: Vec2): Vec2 {
        return velocity + (point - position).cross(FloatMath.toRadians(-angVelocity))
    }

}
