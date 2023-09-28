package mayonez.physics

import mayonez.math.*

/**
 * An object in the world that has position, mass, and motion.
 *
 * @author SlavSquatSuperstar
 */
interface PhysicsBody {

    // Mass

    val mass: Float

    val invMass: Float

    val angMass: Float

    val invAngMass: Float

    val static: Boolean

    // Position

    val position: Vec2

    val rotation: Float

    // Velocity

    var velocity: Vec2

    val speed: Float

    var angVelocity: Float

    val angSpeed: Float

    // Physics Engine

    val material: PhysicsMaterial

    val followsGravity: Boolean

    // Physics Loop

    fun integrateForce(dt: Float)

    fun integrateVelocity(dt: Float)

    // Apply Force

    fun applyForce(force: Vec2?)

    fun applyImpulse(impulse: Vec2?)

    fun addVelocity(velocity: Vec2?)

    // Apply Torque

    fun applyTorque(torque: Float)

    fun applyAngularImpulse(angImpulse: Float)

    fun addAngularVelocity(angVelocity: Float)

    // Velocity at Point

    fun getPointVelocity(point: Vec2): Vec2

}
