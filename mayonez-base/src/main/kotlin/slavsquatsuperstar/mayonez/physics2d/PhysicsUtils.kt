package slavsquatsuperstar.mayonez.physics2d

import slavsquatsuperstar.math.Vec2

/**
 * A utility class for checking the conservation of physics quantities.
 *
 * @author SlavSquatSuperstar
 */
object PhysicsUtils {

    /**
     * Calculate a rigidbody's momentum vector (p), equal to mass times velocity (mv)
     */
    @JvmStatic
    fun getMomentum(rb: Rigidbody2D): Vec2 = rb.velocity * rb.mass

    /**
     * Calculate the magnitude of a rigidbody's momentum (|p|), equal to mass times speed (m|v|)
     */
    @JvmStatic
    fun getMomentumMag(rb: Rigidbody2D): Float = getMomentum(rb).mag()

    /**
     * Calculate a rigidbody's angular momentum (L), equal to moment of inertia times angular velocity (Iω)
     */
    @JvmStatic
    fun getAngMomentum(rb: Rigidbody2D): Float = rb.mass * rb.angVelocity

    /**
     * Calculate a rigidbody's kinetic energy (KE) equal to half times mass times speed squared (1/2 * mv^2)
     */
    @JvmStatic
    fun getKineticEnergy(rb: Rigidbody2D): Float = 0.5f * rb.mass * rb.speed * rb.speed

    /**
     * Calculate a rigidbody's rotational energy (RE) equal to half times moment of inertia times angular speed squared (1/2 * Iω^2)
     */
    @JvmStatic
    fun getRotationalEnergy(rb: Rigidbody2D): Float = 0.5f * rb.angMass * rb.angVelocity * rb.angVelocity

}