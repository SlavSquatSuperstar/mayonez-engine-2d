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
internal class CollisionSolver(
    private val c1: CollisionBody,
    private val c2: CollisionBody,
    private var manifold: Manifold
) {

    private val b1: PhysicsBody? = c1.physicsBody
    private val b2: PhysicsBody? = c2.physicsBody

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
        if (b1.static && b2.static) return // Check masses once, cannot both be static
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
        if (c1.collisionResolved || c2.collisionResolved) {
            manifold = c1.getContacts(c2) ?: return false
        }
        return true
    }

    /**
     * Correct positions of objects if one or zero colliders has an
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
        b1?.move(normal * -depth1)
        b2?.move(normal * depth2)
    }

    private fun PhysicsBody.move(direction: Vec2) {
        position.set(position + direction)
    }

    /** Transfer linear and angular momentum between objects and apply friction. */
    private fun solveDynamic() {
        // Physics Properties
        val massData = MassData.getFrom(b1, b2)
        val matData = MaterialData.combine(b1.material, b2.material)

        val contacts = manifold.getContacts().map {
            // Store contact pos, radii, impulses
            // Apply impulse to bodies using contact data
            ContactPoint(it, it - b1.position, it - b2.position)
        }

        // Calculate Impulses
        for (contact in contacts) {
            contact.normImp = contact.calculateNormalImpulse(massData, matData)
            contact.tanImp = contact.calculateTangentImpulse(massData, matData)
        }

        // Apply Impulses
        for (contact in contacts) {
            contact.applyImpulse(b1, b2, normal, tangent)
        }
    }

    private fun ContactPoint.calculateNormalImpulse(
        massData: MassData, matData: MaterialData
    ): Float {
        val cRest = matData.coeffRestitution
        val relVel = this.getRelativeVelocity(b1, b2) // Relative velocity, v_rel
        val normVel = relVel.dot(normal) // Velocity along collision normal, v_n
        if (normVel > 0f) return 0f // Skip this point if moving away or stationary

        // Normal (separation) impulse, J_n
        val denom = this.getDenominator(normal, massData)
        return -(1f + cRest) * normVel / (denom * manifold.numContacts())
    }

    private fun ContactPoint.calculateTangentImpulse(
        massData: MassData, matData: MaterialData
    ): Float {
        val (_, sFric, kFric) = matData

        val relVel = this.getRelativeVelocity(b1, b2) // Relative velocity, v_rel (will have changed)
        val tanVel = relVel.dot(tangent) // Velocity along collision tangent, v_t
        if (abs(tanVel) < 0.00005f) return 0f // Ignore tiny friction impulses

        // Tangent (friction) impulse, J_t (F_f)
        val denom = this.getDenominator(tangent, massData)
        val tanImp = -tanVel / (denom * manifold.numContacts())

        // Coulomb's Law: If static friction exceeded, use kinetic friction
        return if (abs(tanImp) > abs(this.normImp * sFric)) {
            sign(tanImp) * this.normImp * kFric // F_f = µ_s * F_n
        } else {
            tanImp // F_f = µ_k * F_n
        }
    }

}
