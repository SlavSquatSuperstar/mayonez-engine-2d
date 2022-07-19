package slavsquatsuperstar.mayonez.physics

import kotlin.math.sqrt

/**
 * A reusable preset of bounce and friction values.
 *
 * @author SlavSquatSuperstar
 */
class PhysicsMaterial(
    /**
     * How much this object resists applied forces and comes to rest while in motion.
     * 0 indicates no opposition, and 1 indicates a very strong opposition.
     */
    val kineticFriction: Float,

    /**
     * How much this object resists acceleration from a standstill.
     * 0 indicates no opposition, and 1 indicates a very strong opposition.
     */
    val staticFriction: Float,

    /**
     * How much kinetic energy is conserved after a collision.
     * 0 means all energy is lost, and 1 means energy is perfectly conserved.
     */
    val bounce: Float
) {
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