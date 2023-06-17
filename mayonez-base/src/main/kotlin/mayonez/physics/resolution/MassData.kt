package mayonez.physics.resolution

import mayonez.physics.*

/**
 * A data structure that stores the masses of two objects involved in a
 * collision.
 *
 * @author SlavSquatSuperstar
 */
internal class MassData(r1: Rigidbody, r2: Rigidbody) {
    internal val sumInv: Float = r1.invMass + r2.invMass // Sum of inverse masses, 1/m1 + 1/m2
    internal val invAng1: Float = r1.invAngMass // Inverse angular mass 1, 1/I1
    internal val invAng2: Float = r2.invAngMass // Inverse angular mass 2, 1/I2
}