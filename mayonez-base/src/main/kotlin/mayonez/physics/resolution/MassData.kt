package mayonez.physics.resolution

import mayonez.physics.*

/**
 * Stores the masses of two bodies involved in a collision.
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
        internal fun getFrom(b1: PhysicsBody, b2: PhysicsBody): MassData {
            return MassData(
                b1.invMass + b2.invMass,
                b1.invAngMass, b2.invAngMass
            )
        }
    }
}