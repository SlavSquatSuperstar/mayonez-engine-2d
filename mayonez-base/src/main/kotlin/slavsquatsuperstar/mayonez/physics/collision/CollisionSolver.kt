package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.mayonez.math.MathUtils
import slavsquatsuperstar.mayonez.math.Vec2
import slavsquatsuperstar.mayonez.physics.PhysicsMaterial
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

    companion object {
        // A Rigidbody that will give default values but is not modified
        private val NULL_RB = Rigidbody(0f)

        fun Rigidbody.safeAddImpulse(impulse: Vec2) {
            if (this !== NULL_RB) this.addImpulse(impulse)
        }

        fun Rigidbody.safeAddAngImpulse(impulse: Float) {
            if (this !== NULL_RB) this.addAngularImpulse(impulse)
        }
    }

    private val r1: Rigidbody = c1.rigidbody ?: NULL_RB
    private val r2: Rigidbody = c2.rigidbody ?: NULL_RB

    // Collision Properties
    private val normal = manifold.normal // Collision direction
    private val tangent = normal.normal() // Collision plane
    private val numContacts = manifold.numContacts()

    // Body Properties
    private val sumInvMass = r1.invMass + r2.invMass
    private val invAngMass1 = r1.invAngMass
    private val invAngMass2 = r2.invAngMass

    // Physics Material Properties
    private val mat1 = r1.material
    private val mat2 = r2.material
    private val cRest = PhysicsMaterial.combineBounce(mat1, mat2) // Coefficient of restitution, e
    private val sFric = PhysicsMaterial.combineStaticFriction(mat1, mat2) // combined static friction, mu_s
    private val kFric = PhysicsMaterial.combineKineticFriction(mat1, mat2) // combined kinetic friction, mu_k

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
        solveStatic()
        solveDynamicAngular()
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
     * Only transfer linear momentum between objects.
     */
    private fun solveDynamicLinear() {
        for (contact in manifold.getContacts()) {
            // Relative velocity at contact point (linear + angular velocity)
            val vel1 = r1.getPointVelocity(contact)
            val vel2 = r2.getPointVelocity(contact)
            val relVel = vel2 - vel1 // relative velocity, v_r

            // Normal (separation) impulse
            val normVel = relVel.dot(normal) // Velocity along collision normal
            if (normVel > 0f) return // Stop if moving away or stationary
            // Apply impulse at contact points evenly
            val normImp = -(1f + cRest) * normVel / (sumInvMass)

            val collisionImp = normal * normImp // impulse at this point
            r1.safeAddImpulse(-collisionImp)
            r2.safeAddImpulse(collisionImp)
        }
    }

    /**
     * Only transfer linear and angular momentum between objects.
     */
    private fun solveDynamicAngular() {
        val impulses = ArrayList<Array<Vec2>>(2) // Store impulses/radii for each contact

        // Calculate impulses
        for (contact in manifold.getContacts()) {
            // Relative velocity at contact point (linear + angular velocity)
            val vel1 = r1.getPointVelocity(contact)
            val vel2 = r2.getPointVelocity(contact)
            val relVel = vel2 - vel1 // relative velocity, v_rel
            val normVel = relVel.dot(normal) // Velocity along collision normal, v_n
            if (normVel > 0f) continue // Skip this point if moving away or stationary

            // Normal (separation) impulse
            val rad1 = contact - r1.position
            val rad2 = contact - r2.position
            val dot1Sq = MathUtils.squared(normal.dot(rad1.normal()))
            val dot2Sq = MathUtils.squared(normal.dot(rad2.normal()))
            val denom = sumInvMass + invAngMass1 * dot1Sq + invAngMass2 * dot2Sq
            val normImp = -(1f + cRest) * normVel / (denom * manifold.numContacts()) // Normal impulse, J_n

            val contactImp = normal * normImp // Impulse at this point
            impulses.add(arrayOf(contactImp, rad1, rad2))
        }

        // Apply impulses
        for (i in impulses.indices) {
            val arr = impulses[i]
            val imp = arr[0]
            val rad1 = arr[1]
            val rad2 = arr[2]
            r1.safeAddImpulse(-imp)
            r1.safeAddAngImpulse(rad1.cross(-imp))
            r2.safeAddImpulse(imp)
            r2.safeAddAngImpulse(rad2.cross(imp))
        }
    }

    /**
     * Transfer linear and angular momentum between objects and apply friction.
     */
    private fun solveDynamic() {
        val impulses = ArrayList<Array<Vec2>>(2) // Store impulses/radii for each contact

        // Calculate impulses
        for (contact in manifold.getContacts()) {
            // Relative velocity at contact point (linear + angular velocity)
            val vel1 = r1.getPointVelocity(contact)
            val vel2 = r2.getPointVelocity(contact)
            val relVel = vel2 - vel1 // relative velocity, v_rel

            val rad1 = contact - r1.position
            val rad2 = contact - r2.position
            val dot1Sq = MathUtils.squared(normal.dot(rad1.normal()))
            val dot2Sq = MathUtils.squared(normal.dot(rad2.normal()))

            // Normal (separation) impulse
            val normVel = relVel.dot(normal) // Velocity along collision normal, v_n
            if (normVel > 0f) continue // Skip this point if moving away or stationary
            val denom = sumInvMass + invAngMass1 * dot1Sq + invAngMass2 * dot2Sq
            val normImp = -(1f + cRest) * normVel / (denom * manifold.numContacts()) // Normal impulse, J_n

            // Tangential (friction) impulse
            /*
             * Coulomb's law (static/dynamic friction)
             * F_f ≤ mu*F_n
             * Clamp the friction magnitude to the normal magnitude
             * Use dynamic friction if J_f ≥ mu*J_n
             */
            val tanVel = relVel.dot(tangent)
            var tanImp = tanVel / (sumInvMass * numContacts)
            if (MathUtils.equals(tanImp, 0f)) tanImp = 0f // Ignore tiny friction impulses

            // Coulomb's Law: use kinetic friction if overcome static friction
            if (abs(tanImp) > abs(normImp * sFric)) tanImp =
                -sign(tanImp) * normImp * kFric
            tanImp = MathUtils.clamp(tanImp, -normImp * kFric, normImp * kFric)

            val contactImp = normal * normImp + tangent * tanImp // Impulse at this point
            impulses.add(arrayOf(contactImp, rad1, rad2))
        }

        // Apply impulses
        for (i in impulses.indices) {
            val arr = impulses[i]
            val imp = arr[0]
            val rad1 = arr[1]
            val rad2 = arr[2]
            r1.safeAddImpulse(-imp)
            r1.safeAddAngImpulse(rad1.cross(-imp))
            r2.safeAddImpulse(imp)
            r2.safeAddAngImpulse(rad2.cross(imp))
        }
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
        c1.transform.move(manifold.normal * -depth1)
        c2.transform.move(manifold.normal * depth2)
    }
}