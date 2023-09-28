package mayonez.physics.dynamics

import mayonez.math.*

/**
 * A reusable preset of bounce and friction values.
 *
 * @author SlavSquatSuperstar
 */
class PhysicsMaterial(kineticFriction: Float, staticFriction: Float, bounce: Float) {

    /**
     * The kinetic (dynamic) coefficient of friction, µ_k, represents how much
     * this object resists applied forces and decelerates when sliding against
     * other object. 0 indicates no opposition, and large positive values
     * indicates a very strong opposition.
     */
    val kineticFriction: Float = 0f.coerceAtLeast(kineticFriction)

    /**
     * The static coefficient of friction, µ_s, represents how much this object
     * resists acceleration while resting on another object. 0 indicates
     * no opposition, and large positive values indicates a very strong
     * opposition.
     */
    val staticFriction: Float = 0f.coerceAtLeast(staticFriction)

    /**
     * The coefficient of restitution, e, represents how much kinetic energy
     * is conserved after a collision. 0 means all energy is lost, and 1 means
     * energy is perfectly conserved.
     */
    val bounce: Float = FloatMath.clamp(bounce, 0f, 1f)

    companion object {
        @JvmField
        val DEFAULT_MATERIAL = PhysicsMaterial(0.5f, 0.5f, 0f)
    }
}