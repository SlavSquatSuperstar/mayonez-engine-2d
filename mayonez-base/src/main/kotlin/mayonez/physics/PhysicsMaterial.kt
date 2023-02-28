package mayonez.physics

import mayonez.math.*
import kotlin.math.*

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

        // Friction combine: Geometric average, could also multiply instead
        @JvmStatic
        fun combineKineticFriction(mat1: PhysicsMaterial, mat2: PhysicsMaterial) =
            sqrt(mat1.kineticFriction * mat2.kineticFriction)

        @JvmStatic
        fun combineStaticFriction(mat1: PhysicsMaterial, mat2: PhysicsMaterial) =
            sqrt(mat1.staticFriction * mat2.staticFriction)

        // Bounce combine: Arithmetic average, but could also take min
        @JvmStatic
        fun combineBounce(mat1: PhysicsMaterial, mat2: PhysicsMaterial) = (mat1.bounce + mat2.bounce) * 0.5f
    }
}