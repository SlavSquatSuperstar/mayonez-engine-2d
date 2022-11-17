package slavsquatsuperstar.mayonez.physics.resolution

import slavsquatsuperstar.mayonez.math.MathUtils
import slavsquatsuperstar.mayonez.math.Vec2
import slavsquatsuperstar.mayonez.physics.Rigidbody

/**
 * Describes contact point between two colliding shapes and stores additional information such as radius and impulse.
 *
 * Sources
 * - https://www.chrishecker.com/Rigid_Body_Dynamics
 * - https://www.youtube.com/playlist?list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs
 *
 * @author SlavSquatSuperstar
 */
class Contact(private val pos: Vec2, r1Pos: Vec2, r2Pos: Vec2) {
    /**
     * Distance to first body center, r1.
     */
    private val rad1: Vec2 = pos - r1Pos

    /**
     * Distance to second body center r2.
     */
    private val rad2: Vec2 = pos - r2Pos

    /**
     * Normal impulse magnitude, J_n.
     */
    var normImp: Float = 0f

    /**
     * Tangent impulse magnitude, J_t.
     */
    var tanImp: Float = 0f

    /**
     * Calculate the relative velocity of two bodies at this contact point.
     */
    fun getRelativeVelocity(r1: Rigidbody?, r2: Rigidbody?): Vec2 {
        val vel1 = r1?.getPointVelocity(pos) ?: Vec2()
        val vel2 = r2?.getPointVelocity(pos) ?: Vec2()
        return vel2 - vel1
    }

    /**
     * Calculate the denominator of the impulse equation, following the derivation from
     * https://www.chrishecker.com/images/e/e7/Gdmphys3.pdf.
     */
    fun getDenominator(direction: Vec2, sumInvM: Float, invI1: Float, invI2: Float): Float {
        val dot1Sq = MathUtils.squared(direction.dot(rad1.normal()))
        val dot2Sq = MathUtils.squared(direction.dot(rad2.normal()))
        return sumInvM + invI1 * dot1Sq + invI2 * dot2Sq
    }

    /**
     * Apply an impulse to two bodies at this contact point to resolve a collision.
     */
    fun applyImpulse(r1: Rigidbody?, r2: Rigidbody?, impulse: Vec2) {
        r1?.applyImpulse(-impulse)
        r1?.applyAngularImpulse(rad1.cross(-impulse))
        r2?.applyImpulse(impulse)
        r2?.applyAngularImpulse(rad2.cross(impulse))
    }
}