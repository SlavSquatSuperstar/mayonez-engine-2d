package mayonez.physics.resolution

import mayonez.math.*
import mayonez.physics.colliders.*
import mayonez.physics.dynamics.*
import mayonez.physics.manifold.*
import kotlin.math.*

//private const val DEFAULT_IMPULSE_ITERATIONS = 4

/**
 * Applies linear and angular impulses to two intersecting bodies to
 * resolve a collision.
 *
 * @author SlavSquatSuperstar
 */
internal class CollisionSolver(private val c1: Collider, private val c2: Collider, private var manifold: Manifold) {

    private val b1: PhysicsBody = c1.rigidbody ?: Rigidbody(0f)
    private val b2: PhysicsBody = c2.rigidbody ?: Rigidbody(0f)

    // Collision Properties
    private lateinit var normal: Vec2 // Collision direction
    private lateinit var tangent: Vec2 // Collision plane

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
    fun solveCollision() {
        if (c1.isStatic() && c2.isStatic()) return // Check masses once, cannot both be static
        if (!recheckCollision()) return

        normal = manifold.normal
        tangent = normal.normal()

        solveStatic()
        solveDynamic()

        c1.collisionResolved = true
        c2.collisionResolved = true
    }

    /**
     * Check for collision again and update the manifold if either object has
     * been modified in a previous collision.
     *
     * @return whether the colliders are still colliding
     */
    private fun recheckCollision(): Boolean {
        if (c1.collisionResolved || c2.collisionResolved)
            manifold = c1.getContacts(c2) ?: return false
        return true
    }

    /**
     * Correct positions of objects if one or neither collider has an
     * infinite-mass rigidbody.
     */
    private fun solveStatic() {
        val mass1: Float = b1.mass
        val mass2: Float = b2.mass

        // Separate objects factoring in mass
        val massRatio = mass1 / (mass1 + mass2)
        val depth1 = manifold.depth * massRatio
        val depth2 = manifold.depth * (1f - massRatio)

        // Displace bodies to correct position
        c1.transform.move(normal * -depth1)
        c2.transform.move(normal * depth2)
    }

    /** Transfer linear and angular momentum between objects and apply friction. */
    private fun solveDynamic() {
        // Physics Properties
        val massData = MassData.getFrom(b1, b2)
        val matData = MaterialData.combine(b1.material, b2.material)

        val contacts = Array(manifold.numContacts()) { ContactPoint(manifold.getContact(it), b1.position, b2.position) }
        calculateNormalImpulse(contacts, massData, matData)
        calculateTangentImpulse(contacts, massData, matData)

        for (contact in contacts) {
            val totalImpulse = (normal * contact.normImp) + (tangent * contact.tanImp)
            contact.applyImpulse(b1, b2, totalImpulse)
        }
    }

    private fun calculateNormalImpulse(contacts: Array<ContactPoint>, massData: MassData, matData: MaterialData) {
        val cRest = matData.coeffRestitution

        for (contact in contacts) {
            val relVel = contact.getRelativeVelocity(b1, b2) // Relative velocity, v_rel
            val normVel = relVel.dot(normal) // Velocity along collision normal, v_n
            if (normVel > 0f) continue // Skip this point if moving away or stationary

            // Normal (separation) impulse, J_n
            val denom = contact.getDenominator(normal, massData)
            contact.normImp = -(1f + cRest) * normVel / (denom * manifold.numContacts())
        }
    }

    private fun calculateTangentImpulse(contacts: Array<ContactPoint>, massData: MassData, matData: MaterialData) {
        val (_, sFric, kFric) = matData

        for (contact in contacts) {
            val relVel = contact.getRelativeVelocity(b1, b2) // Relative velocity, v_rel (will have changed)
            val tanVel = relVel.dot(tangent) // Velocity along collision tangent, v_t
            if (abs(tanVel) < 0.00005f) continue // Ignore tiny friction impulses

            // Tangent (friction) impulse, J_t
            val denom = contact.getDenominator(tangent, massData)
            var tanImp = -tanVel / (denom * manifold.numContacts())

            // Coulomb's Law: if exceed static friction, use kinetic friction
            val normImp = contact.normImp
            if (isStaticFrictionExceeded(tanImp, normImp, sFric)) {
                tanImp = sign(tanImp) * normImp * kFric
            }
            contact.tanImp = tanImp
        }
    }

    private fun isStaticFrictionExceeded(tanImp: Float, normImp: Float, sFric: Float): Boolean {
        return abs(tanImp) > abs(normImp * sFric)
    }

}