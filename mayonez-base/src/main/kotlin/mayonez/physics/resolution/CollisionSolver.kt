package mayonez.physics.resolution

import mayonez.physics.PhysicsMaterial
import mayonez.physics.Rigidbody
import mayonez.physics.colliders.Collider
import kotlin.math.abs
import kotlin.math.sign

/**
 * Applies linear and angular impulses to two intersecting bodies to resolve a collision.
 *
 * @author SlavSquatSuperstar
 */
class CollisionSolver(private val c1: Collider, private val c2: Collider, private var manifold: Manifold) {

    private val r1: Rigidbody = c1.rigidbody ?: Rigidbody(0f)
    private val r2: Rigidbody = c2.rigidbody ?: Rigidbody(0f)

    // Collision Properties
    private val normal = manifold.normal // Collision direction
    private val tangent = normal.normal() // Collision plane

    /*
     * Types of Collisions
     * Collider & Rigidbody (dynamic): Can move and push
     *  - Ex: player, enemy
     * Collider & no Rigidbody (static): Can push but not move
     *  - Ex: fire area
     * Rigidbody & no Collider (intangible): Can move but not push
     *  - Ex: sun
     *
     * Require RB
     * Static vs Static (ignore)
     * Dynamic vs Static
     * Dynamic vs Dynamic
     */
    fun solve() {
        if (c1.isStatic() && c2.isStatic()) return // Check masses once, cannot both be static
        if (!recheck()) return
        solveStatic()
        solveDynamic()
        c1.collisionResolved = true
        c2.collisionResolved = true
    }

    /**
     * Check for collision again and update the manifold if either object has been modified in a previous collision.
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
        // Body Properties
        val sumInvM = r1.invMass + r2.invMass // Sum of inverse masses, 1/m1 + 1/m2
        val invI1 = r1.invAngMass // Inverse angular mass 1, 1/I1
        val invI2 = r2.invAngMass // Inverse angular mass 2, 1/I2

        // Physics Material Properties
        val mat1 = r1.material
        val mat2 = r2.material
        val cRest = PhysicsMaterial.combineBounce(mat1, mat2) // Coefficient of restitution, e
        val sFric = PhysicsMaterial.combineStaticFriction(mat1, mat2) // Combined static friction, mu_s
        val kFric = PhysicsMaterial.combineKineticFriction(mat1, mat2) // Combined kinetic friction, mu_k

        // Impulses per contact
        val contacts = Array(manifold.numContacts()) { Contact(manifold.getContact(it), r1.position, r2.position) }

        // Calculate and apply normal impulses
        for (contact in contacts) {
            val relVel = contact.getRelativeVelocity(r1, r2) // Relative velocity, v_rel
            val normVel = relVel.dot(normal) // Velocity along collision normal, v_n
            if (normVel > 0f) continue // Skip this point if moving away or stationary

            // Normal (separation) impulse, J_n
            val denom = contact.getDenominator(normal, sumInvM, invI1, invI2)
            contact.normImp = -(1f + cRest) * normVel / (denom * manifold.numContacts())
        }
        for (contact in contacts) contact.applyImpulse(r1, r2, normal * contact.normImp)

        // Calculate and apply tangent impulses
        for (contact in contacts) {
            val relVel = contact.getRelativeVelocity(r1, r2) // Relative velocity, v_rel (will have changed)
            val tanVel = relVel.dot(tangent) // Velocity along collision tangent, v_t
            if (abs(tanVel) < 0.00005f) continue // Ignore tiny friction impulses

            // Tangent (friction) impulse, J_t
            val denom = contact.getDenominator(tangent, sumInvM, invI1, invI2)
            var tanImp = -tanVel / (denom * manifold.numContacts())
            // Coulomb's Law: if exceed static friction, use kinetic friction
            val normImp = contact.normImp
            if (abs(tanImp) > abs(normImp * sFric)) tanImp = sign(tanImp) * normImp * kFric
            contact.tanImp = tanImp
        }
        for (contact in contacts) contact.applyImpulse(r1, r2, tangent * contact.tanImp)
    }

    /**
     * Correct positions of objects if one or neither collider has an infinite-mass rigidbody.
     */
    private fun solveStatic() {
        val mass1: Float = r1.mass
        val mass2: Float = r2.mass

        // Separate objects factoring in mass
        val massRatio = mass1 / (mass1 + mass2)
        val depth1 = manifold.depth * massRatio
        val depth2 = manifold.depth * (1 - massRatio)

        // Displace bodies to correct position
        c1.transform.move(normal * -depth1)
        c2.transform.move(normal * depth2)
    }

}