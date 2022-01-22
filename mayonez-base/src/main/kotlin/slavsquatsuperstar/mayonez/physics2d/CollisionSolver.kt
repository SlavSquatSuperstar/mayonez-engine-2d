package slavsquatsuperstar.mayonez.physics2d

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D
import kotlin.math.abs
import kotlin.math.sign

/**
 * Resolves position and impulse of two bodies in a collision.
 *
 * @author SlavSquatSuperstar
 */
class CollisionSolver(private val cm: CollisionManifold) {
    private val c1: Collider2D = cm.self
    private val c2: Collider2D = cm.other
    private val r1: Rigidbody2D? = c1.getRigidbody()
    private val r2: Rigidbody2D? = c2.getRigidbody()

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
//        if (r1 == null || r2 == null) return; // Need both rigidbodies for dynamic

        // Collision Information
        val normal = cm.normal // Collision direction
        val tangent = normal.getNormal() // Collision plane
        val numContacts = cm.countContacts()

        // Physics Properties
        val sumInvMass = (r1?.invMass ?: 0f) + (r2?.invMass ?: 0f)
        val mat1 = c1.material
        val mat2 = c2.material
        val restitution = PhysicsMaterial.combineBounce(mat1, mat2)
        val sFric = PhysicsMaterial.combineStaticFriction(mat1, mat2)
        val kFric = PhysicsMaterial.combineKineticFriction(mat1, mat2)

        for (i in 0 until Physics2D.IMPULSE_ITERATIONS) {
            // Net impulse of collision
            var sumImp = Vec2()
            var sumAngImp1 = 0f
            var sumAngImp2 = 0f

            for (j in 0 until numContacts) {
                // Radius from center of mass to contact
                val contact = cm.getContact(j)
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
                val normalImp = -(1f + restitution) * normalVel / (sumInvMass * numContacts)

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
        val depth1 = cm.depth * massRatio
        val depth2 = cm.depth * (1 - massRatio)

        // Displace bodies to correct position
        c1.transform.move(cm.normal * -depth1)
        c2.transform.move(cm.normal * depth2)
    }
}