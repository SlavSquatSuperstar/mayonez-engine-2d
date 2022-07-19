package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.Physics
import slavsquatsuperstar.mayonez.physics.PhysicsMaterial
import slavsquatsuperstar.mayonez.physics.Rigidbody
import slavsquatsuperstar.mayonez.physics.colliders.Collider
import kotlin.math.abs
import kotlin.math.sign

/**
 * Resolves position and impulse of two bodies in a collision.
 *
 * @author SlavSquatSuperstar
 */
class Solver(private val man: CollisionInfo) {
    private val c1: Collider = man.self
    private val c2: Collider = man.other
    private val r1: Rigidbody? = c1.getRigidbody()
    private val r2: Rigidbody? = c2.getRigidbody()

    /*
     * Collider & Rigidbody (dynamic): Can move and push
     *  - Ex: player, enemy
     * Collider & no Rigidbody (static): Can push but not move
     *  - Ex: fire area
     * Rigidbody & no Collider (intangible): Can move but not push
     *  - Ex: sun
     *
     * // Require RB
     * Static vs Static (ignore)
     * Dynamic vs Static
     * Dynamic vs Dynamic
     */
    fun solve() {
        // Check masses once, cannot both be static
        if (c1.isStatic() && c2.isStatic()) return
        solveDynamic()
        solveStatic()
    }

    /**
     * Transfer linear and angular momentum between objects and apply friction.
     */
    private fun solveDynamic() {
        // Collision Information
        val normal = man.normal // Collision direction
        val tangent = normal.normal() // Collision plane
        val numContacts = man.countContacts()

        // Physics Properties
        val sumInvMass = (r1?.invMass ?: 0f) + (r2?.invMass ?: 0f)
        val mat1 = c1.material
        val mat2 = c2.material
        val kRest = PhysicsMaterial.combineBounce(mat1, mat2) // coefficient of restitution, e
        val sFric = PhysicsMaterial.combineStaticFriction(mat1, mat2) // combined static friction, mu_s
        val kFric = PhysicsMaterial.combineKineticFriction(mat1, mat2) // combined kinetic friction, mu_k

        for (i in 0 until Physics.IMPULSE_ITERATIONS) {
            // Net impulse of collision
            var sumImp = Vec2()
            var sumAngImp1 = 0f
            var sumAngImp2 = 0f

            for (j in 0 until numContacts) {
                // Radius from center of mass to contact
                val contact = man.getContact(j)
                val rad1 = contact - r1!!.position
                val rad2 = contact - r2!!.position

                // Relative velocity at contact point (linear + angular velocity)
                val vel1 = r1.getPointVelocity(contact)
                val vel2 = r2.getPointVelocity(contact)
                val relativeVel = vel1 - vel2

                // Normal (separation) impulse
                val normalVel = relativeVel.dot(normal) // Velocity along collision normal
                if (normalVel < 0f) return  // Stop if moving away or stationary
                // Apply impulse at contact points evenly
                val normalImp = -(1f + kRest) * normalVel / (sumInvMass * numContacts)

                // Transfer angular momentum
                sumAngImp1 -= rad1.cross(normal * relativeVel)
                sumAngImp2 += rad2.cross(normal * relativeVel)

                // Tangential (friction) impulse
                /*
                 * Coulomb's law (static/dynamic friction)
                 * F_f ≤ mu*F_n
                 * Clamp the friction magnitude to the normal magnitude
                 * Use dynamic friction if J_f ≥ mu*J_n
                 */
                val tangentVel = relativeVel.dot(tangent)
                var tangentImp = -tangentVel / (sumInvMass * numContacts)
                if (MathUtils.equals(tangentImp, 0f)) tangentImp = 0f // Ignore tiny friction impulses

                // Coulomb's Law: use kinetic friction if overcome static friction
                if (abs(tangentImp) > abs(normalImp * sFric)) tangentImp =
                    -sign(tangentImp) * normalImp * kFric
                tangentImp = MathUtils.clamp(tangentImp, -normalImp * kFric, normalImp * kFric)

                // Calculate angular impulse from collision
                val collisionImp = normal * normalImp + tangent * tangentImp // impulse at this point
                sumImp += collisionImp
                sumAngImp1 += rad1.cross(collisionImp)
                sumAngImp2 -= rad2.cross(collisionImp)
            }

            // Transfer Momentum
            r1?.addImpulse(sumImp)
            r1?.addAngularImpulse(sumAngImp1)
            r2?.addImpulse(-sumImp)
            r2?.addAngularImpulse(sumAngImp2)
        }
    }

    /**
     * Correct positions of objects if one or neither collider has an infinite-mass rigidbody.
     */
    private fun solveStatic() {
        val mass1: Float = r1?.mass ?: 0f
        val mass2: Float = r2?.mass ?: 0f

        // Separate objects factoring in mass
        val massRatio = mass1 / (mass1 + mass2)
        val depth1 = man.depth * massRatio
        val depth2 = man.depth * (1 - massRatio)

        // Displace bodies to correct position
        c1.transform.move(man.normal * -depth1)
        c2.transform.move(man.normal * depth2)
    }
}