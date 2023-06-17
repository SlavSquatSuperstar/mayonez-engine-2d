package mayonez.physics.resolution

import mayonez.physics.*
import kotlin.math.*

/**
 * A data structure that stores the physics material properties of two
 * objects involved in a collision.
 *
 * @author SlavSquatSuperstar
 */
internal class MaterialData(mat1: PhysicsMaterial, mat2: PhysicsMaterial) {
    val cRest: Float = combineBounce(mat1, mat2) // Coefficient of restitution, e
    val sFric: Float = combineStaticFriction(mat1, mat2) // Combined static friction, mu_s
    val kFric: Float = combineKineticFriction(mat1, mat2) // Combined kinetic friction, mu_k

    companion object {
        // Friction combine: Geometric average, but could also multiply instead
        private fun combineKineticFriction(mat1: PhysicsMaterial, mat2: PhysicsMaterial): Float {
            return sqrt(mat1.kineticFriction * mat2.kineticFriction)
        }

        private fun combineStaticFriction(mat1: PhysicsMaterial, mat2: PhysicsMaterial): Float {
            return sqrt(mat1.staticFriction * mat2.staticFriction)
        }

        // Bounce combine: Arithmetic average, but could also take min
        private fun combineBounce(mat1: PhysicsMaterial, mat2: PhysicsMaterial): Float {
            return 0.5f * (mat1.bounce + mat2.bounce)
        }
    }
}