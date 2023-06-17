package mayonez.physics.resolution

import mayonez.math.*
import mayonez.physics.*

/**
 * Describes contact point between two colliding shapes and stores
 * additional information such as radius and impulse.
 *
 * Sources
 * - [Chris Hecker](https://www.chrishecker.com/Rigid_Body_Dynamics)
 * - [YouTube](https://www.youtube.com/playlist?list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs)
 *
 * @author SlavSquatSuperstar
 */
internal class Contact(private val contactPos: Vec2, r1Pos: Vec2, r2Pos: Vec2) {
    /** Distance to first body center, r1. */
    private val rad1: Vec2 = contactPos - r1Pos

    /** Distance to second body center, r2. */
    private val rad2: Vec2 = contactPos - r2Pos

    /** Normal impulse magnitude, J_n. */
    internal var normImp: Float = 0f

    /** Tangent impulse magnitude, J_t. */
    internal var tanImp: Float = 0f

    /** Calculate the relative velocity of two bodies at this contact point. */
    fun getRelativeVelocity(r1: Rigidbody?, r2: Rigidbody?): Vec2 {
        val vel1 = r1?.getPointVelocity(contactPos) ?: Vec2()
        val vel2 = r2?.getPointVelocity(contactPos) ?: Vec2()
        return vel2 - vel1
    }

    /**
     * Calculate the denominator of the impulse equation, following the
     * derivation from https://www.chrishecker.com/images/e/e7/Gdmphys3.pdf.
     */
    fun getDenominator(direction: Vec2, massData: MassData): Float {
        val (sumInv, invAng1, invAng2) = massData
        val dot1Sq = FloatMath.squared(direction.dot(rad1.normal()))
        val dot2Sq = FloatMath.squared(direction.dot(rad2.normal()))
        return sumInv + (invAng1 * dot1Sq) + (invAng2 * dot2Sq)
    }

    /**
     * Apply an impulse to two bodies at this contact point to resolve a
     * collision.
     */
    fun applyImpulse(r1: Rigidbody?, r2: Rigidbody?, impulse: Vec2) {
        r1?.applyImpulse(-impulse)
        r1?.applyAngularImpulse(rad1.cross(-impulse))
        r2?.applyImpulse(impulse)
        r2?.applyAngularImpulse(rad2.cross(impulse))
    }
}