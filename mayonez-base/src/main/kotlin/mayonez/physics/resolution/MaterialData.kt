package mayonez.physics.resolution

import mayonez.physics.*

/**
 * A data structure that stores the physics material properties of two
 * objects involved in a collision.
 *
 * @author SlavSquatSuperstar
 */
internal class MaterialData(mat1: PhysicsMaterial, mat2: PhysicsMaterial) {
    val cRest: Float = PhysicsMaterial.combineBounce(mat1, mat2) // Coefficient of restitution, e
    val sFric: Float = PhysicsMaterial.combineStaticFriction(mat1, mat2) // Combined static friction, mu_s
    val kFric: Float = PhysicsMaterial.combineKineticFriction(mat1, mat2) // Combined kinetic friction, mu_k
}