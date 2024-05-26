package mayonez.physics.resolution

import mayonez.math.*
import mayonez.physics.dynamics.*

/**
 * Describes a contact point between two colliding shapes and stores
 * additional information such as radius and impulse.
 *
 * Sources
 * - [Chris Hecker](https://www.chrishecker.com/Rigid_Body_Dynamics)
 * - [YouTube](https://www.youtube.com/playlist?list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs)
 *
 * @author SlavSquatSuperstar
 */
internal class ContactPoint(private val contactPos: Vec2, b1Pos: Vec2, b2Pos: Vec2) {
    /** Distance to first body center, r1. */
    private val rad1: Vec2 = contactPos - b1Pos

    /** Distance to second body center, r2. */
    private val rad2: Vec2 = contactPos - b2Pos

    /** Normal impulse magnitude, J_n. */
    internal var normImp: Float = 0f

    /** Tangent impulse magnitude, J_t. */
    internal var tanImp: Float = 0f

    /** Calculate the relative velocity of two bodies at this contact point. */
    fun getRelativeVelocity(b1: PhysicsBody?, b2: PhysicsBody?): Vec2 {
        val vel1 = b1.getPointVelocity(contactPos)
        val vel2 = b2.getPointVelocity(contactPos)
        return vel2 - vel1
    }

    /**
     * Calculate the denominator of the impulse equation, following the
     * derivation from https://www.chrishecker.com/images/e/e7/Gdmphys3.pdf.
     */
    fun getDenominator(direction: Vec2, massData: MassData): Float {
        val (sumInv, invAng1, invAng2) = massData
        val dot1Sq = MathUtils.squared(direction.dot(rad1.normal()))
        val dot2Sq = MathUtils.squared(direction.dot(rad2.normal()))
        return sumInv + (invAng1 * dot1Sq) + (invAng2 * dot2Sq)
    }

    /**
     * Apply an impulse to two bodies at this contact point to resolve a
     * collision.
     */
    fun applyImpulse(b1: PhysicsBody?, b2: PhysicsBody?, impulse: Vec2) {
        b1?.applyImpulse(-impulse)
        b1?.applyAngularImpulse(rad1.cross(-impulse))
        b2?.applyImpulse(impulse)
        b2?.applyAngularImpulse(rad2.cross(impulse))
    }
}