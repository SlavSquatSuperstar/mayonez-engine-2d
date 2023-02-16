package mayonez.physics.resolution

import mayonez.physics.Rigidbody

/**
 * A data structure that stores the masses of two objects involved in a
 * collision.
 *
 * @author SlavSquatSuperstar
 */
internal class MassData(r1: Rigidbody, r2: Rigidbody) {
    val sumInv: Float = r1.invMass + r2.invMass // Sum of inverse masses, 1/m1 + 1/m2
    val invAng1: Float = r1.invAngMass // Inverse angular mass 1, 1/I1
    val invAng2: Float = r2.invAngMass // Inverse angular mass 2, 1/I2
}