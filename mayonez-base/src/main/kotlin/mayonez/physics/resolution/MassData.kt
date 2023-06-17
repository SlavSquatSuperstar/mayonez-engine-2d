package mayonez.physics.resolution

import mayonez.physics.*

/**
 * A data structure that stores the masses of two objects involved in a
 * collision.
 *
 * @author SlavSquatSuperstar
 */
internal data class MassData(
    /** The sum of both bodies' inverse masses, 1/m1 + 1/m2. */
    internal val sumInverseMasses: Float,
    /** The first body's inverse angular mass, 1/I1. */
    internal val inverseAngMass1: Float,
    /** The second body's inverse angular mass, 1/I2. */
    internal val inverseAngMass2: Float
) {
    companion object {
        internal fun getMassData(r1: Rigidbody, r2: Rigidbody): MassData {
            return MassData(
                r1.invMass + r2.invMass,
                r1.invAngMass,
                r2.invAngMass
            )
        }
    }
}