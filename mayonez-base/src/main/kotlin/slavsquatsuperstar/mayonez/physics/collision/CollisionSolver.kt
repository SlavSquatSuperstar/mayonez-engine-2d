package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.mayonez.math.MathUtils
import slavsquatsuperstar.mayonez.math.Vec2
import slavsquatsuperstar.mayonez.physics.PhysicsMaterial
import slavsquatsuperstar.mayonez.physics.PhysicsWorld
import slavsquatsuperstar.mayonez.physics.Rigidbody
import slavsquatsuperstar.mayonez.physics.colliders.Collider
import kotlin.math.abs
import kotlin.math.sign

/**
 * Applies linear and angular impulses to two intersecting bodies to resolve a collision.
 *
 * @author SlavSquatSuperstar
 */
class CollisionSolver(private val c1: Collider, private val c2: Collider, private var manifold: Manifold) {
    private val r1: Rigidbody? = c1.rigidbody
    private val r2: Rigidbody? = c2.rigidbody

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
        if (!recheck()) return
        if (c1.isStatic() && c2.isStatic()) return // Check masses once, cannot both be static
        solveDynamic()
        solveStatic()
        c1.collisionResolved = true
        c2.collisionResolved = true
    }

    /**
     * Check for collision again if either object has been modified in a previous collision.
     *
     * @return whether the colliders are still colliding
     */
    private fun recheck(): Boolean {
        if (c1.collisionResolved || c2.collisionResolved)
            manifold = c1.getCollisionInfo(c2) ?: return false
        return true
    }

    /**
     * Transfer linear and angular momentum between objects and apply friction.
     */
    private fun solveDynamic() {
        // Collision Information
        val normal = manifold.normal // Collision direction
        val tangent = normal.normal() // Collision plane
        val numContacts = manifold.numContacts()

        // Physics Properties
        val sumInvMass = (r1?.invMass ?: 0f) + (r2?.invMass ?: 0f)
        val mat1 = c1.material
        val mat2 = c2.material
        val cRest = PhysicsMaterial.combineBounce(mat1, mat2) // coefficient of restitution, e
        val sFric = PhysicsMaterial.combineStaticFriction(mat1, mat2) // combined static friction, mu_s
        val kFric = PhysicsMaterial.combineKineticFriction(mat1, mat2) // combined kinetic friction, mu_k

        for (i in 0 until PhysicsWorld.IMPULSE_ITERATIONS) {
            // Net impulse of collision
            var sumImp = Vec2()
            var sumAngImp1 = 0f
            var sumAngImp2 = 0f

            for (contact in manifold.getContacts()) {
                // Radius from center of mass to contact
                val rad1 = contact - r1!!.position
                val rad2 = contact - r2!!.position

                // Relative velocity at contact point (linear + angular velocity)
                val vel1 = r1.getPointVelocity(contact)
                val vel2 = r2.getPointVelocity(contact)
                val relVel = vel1 - vel2 // relative velocity, v_r

                // Normal (separation) impulse
                val normVel = relVel.dot(normal) // Velocity along collision normal
                if (normVel < 0f) return  // Stop if moving away or stationary
                // Apply impulse at contact points evenly
                val normImp = -(1f + cRest) * normVel / (sumInvMass * numContacts)

                // Transfer angular momentum
                sumAngImp1 -= rad1.cross(normal * relVel)
                sumAngImp2 += rad2.cross(normal * relVel)

                // Tangential (friction) impulse
                /*
                 * Coulomb's law (static/dynamic friction)
                 * F_f ≤ mu*F_n
                 * Clamp the friction magnitude to the normal magnitude
                 * Use dynamic friction if J_f ≥ mu*J_n
                 */
                val tanVel = relVel.dot(tangent)
                var tanImp = -tanVel / (sumInvMass * numContacts)
                if (MathUtils.equals(tanImp, 0f)) tanImp = 0f // Ignore tiny friction impulses

                // Coulomb's Law: use kinetic friction if overcome static friction
                if (abs(tanImp) > abs(normImp * sFric)) tanImp =
                    -sign(tanImp) * normImp * kFric
                tanImp = MathUtils.clamp(tanImp, -normImp * kFric, normImp * kFric)

                // Calculate angular impulse from collision
                val collisionImp = normal * normImp + tangent * tanImp // impulse at this point
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
        val depth1 = manifold.depth * massRatio
        val depth2 = manifold.depth * (1 - massRatio)

        // Displace bodies to correct position
        c1.transform.move(manifold.normal * -depth1)
        c2.transform.move(manifold.normal * depth2)
    }
}