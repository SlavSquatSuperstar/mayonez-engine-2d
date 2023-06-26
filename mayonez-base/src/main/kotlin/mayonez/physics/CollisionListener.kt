package mayonez.physics

import mayonez.physics.colliders.*
import mayonez.physics.manifold.*

/**
 * Detects when collisions start and stop between two
 * [mayonez.physics.colliders.Collider] components.
 *
 * @author SlavSquatSuperstar
 */
internal class CollisionListener(
    @JvmField val c1: Collider,
    @JvmField val c2: Collider
) {
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
            !colliding -> startCollision() // has not collided before
            else -> continueCollision() // has collided before
        }

        trigger = c1.trigger || c2.trigger // if either is trigger then call but don't resolve
        return if (trigger) null else manifold
    }

    private fun startCollision() {
        if (!colliding) {
            colliding = true
            sendCollisionEvents(CollisionEventType.ENTER)
        }
    }

    private fun continueCollision() {
        sendCollisionEvents(CollisionEventType.STAY)
    }

    private fun stopCollision() {
        if (colliding) {
            colliding = false
            sendCollisionEvents(CollisionEventType.EXIT)
        }
    }

    private fun sendCollisionEvents(type: CollisionEventType) {
        // No collision if either object is missing
        if (c1.gameObject == null || c2.gameObject == null) return

        c1.sendCollisionEvent(CollisionEvent(c2.gameObject, trigger, type))
        c2.sendCollisionEvent(CollisionEvent(c1.gameObject, trigger, type))
    }

    fun match(col: Collider?): Boolean = (c1 == col) || (c2 == col)

    fun match(c1: Collider, c2: Collider): Boolean {
        return (this.c1 == c1 && this.c2 == c2) || (this.c1 == c2 && this.c2 == c1)
    }
}