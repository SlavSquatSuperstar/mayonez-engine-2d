package mayonez.physics.resolution

import mayonez.physics.dynamics.*
import kotlin.math.*

/**
 * A data structure that stores the physics material properties of two
 * objects involved in a collision.
 *
 * @author SlavSquatSuperstar
 */
internal data class MaterialData(
    /** The coefficient of restitution, or combined bounce, e. */
    internal val coeffRestitution: Float,
    /** The combined coefficient of static friction, mu_s. */
    internal val staticFriction: Float,
    /** The combined coefficient of kinetic friction, mu_k. */
    internal val kineticFriction: Float
) {
    companion object {
        internal fun combine(mat1: PhysicsMaterial, mat2: PhysicsMaterial): MaterialData {
            return MaterialData(
                geometricMean(mat1.kineticFriction, mat2.kineticFriction),
                geometricMean(mat1.staticFriction, mat2.staticFriction),
                average(mat1.bounce, mat2.bounce)
            )
        }

        // Friction combine: Geometric average, but could also multiply instead
        private fun geometricMean(num1: Float, num2: Float): Float {
            return sqrt(num1 * num2)
        }

        // Bounce combine: Arithmetic average, but could also take min
        private fun average(num1: Float, num2: Float): Float {
            return 0.5f * (num1 + num2)
        }
    }
}