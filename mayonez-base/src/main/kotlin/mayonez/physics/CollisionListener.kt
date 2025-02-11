package mayonez.physics

import mayonez.math.*
import mayonez.physics.colliders.*
import mayonez.physics.manifold.*
import mayonez.physics.resolution.*

/**
 * Detects when collisions start and stop between two
 * [mayonez.physics.colliders.CollisionBody] objects.
 *
 * @author SlavSquatSuperstar
 */
internal class CollisionListener(val c1: CollisionBody, val c2: CollisionBody) {
    private var colliding: Boolean = false // was colliding last frame
    private var broadphase: Boolean = false // bounding boxes are colliding
    private var trigger: Boolean = false

    fun checkBroadphase(): Boolean {
        broadphase = Collisions.checkCollision(c1.getMinBounds(), c2.getMinBounds())
        if (!broadphase) stopCollision() // no longer colliding
        return broadphase
    }

    fun checkNarrowphase(): Manifold? {
        if (!broadphase) return null // don't check if not broadphase
        if (c1.trigger && c2.trigger) return null // don't call or resolve if both are triggers

        val manifold = c1.getContacts(c2)
        when {
            (manifold == null) -> stopCollision() // no longer colliding
            !colliding -> startCollision(manifold) // has not collided before
            else -> continueCollision(manifold) // has collided before
        }

        trigger = c1.trigger || c2.trigger // if either is trigger then call but don't resolve
        return if (trigger) null else manifold
    }

    private fun startCollision(manifold: Manifold) {
        if (!colliding) {
            colliding = true
            val velocity = c2.physicsBody.velocity - c1.physicsBody.velocity
            sendCollisionEvents(CollisionEventType.ENTER, manifold = manifold, velocity = velocity)
        }
    }

    private fun continueCollision(manifold: Manifold) {
        if (colliding) {
            sendCollisionEvents(CollisionEventType.STAY, manifold = manifold)
        }
    }

    private fun stopCollision() {
        if (colliding) {
            colliding = false
            sendCollisionEvents(CollisionEventType.EXIT)
        }
    }

    private fun sendCollisionEvents(
        type: CollisionEventType, manifold: Manifold? = null, velocity: Vec2? = null
    ) {
        val direction = manifold?.normal
        val contacts = manifold?.getContacts()

        c1.onCollisionEvent(
            CollisionEvent(
                (c2 as Collider).gameObject, trigger, type,
                direction, velocity, contacts
            )
        )
        c2.onCollisionEvent(
            CollisionEvent(
                (c1 as Collider).gameObject, trigger, type,
                direction?.unaryMinus(), velocity?.unaryMinus(), contacts
            )
        )
    }

    fun match(col: CollisionBody?): Boolean = (c1 == col) || (c2 == col)

    fun match(c1: CollisionBody, c2: CollisionBody): Boolean {
        return (this.c1 == c1 && this.c2 == c2) || (this.c1 == c2 && this.c2 == c1)
    }

}