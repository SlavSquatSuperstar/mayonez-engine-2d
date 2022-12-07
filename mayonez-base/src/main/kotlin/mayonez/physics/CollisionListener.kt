package mayonez.physics

import mayonez.physics.colliders.Collider
import mayonez.physics.resolution.CollisionSolver
import mayonez.physics.resolution.Manifold

internal class CollisionListener(
    @JvmField val c1: Collider,
    @JvmField val c2: Collider
) {
    lateinit var solver: CollisionSolver

    private var colliding: Boolean = false // was colliding last frame
    private var broadphase: Boolean = false // bounding boxes are colliding
    private var trigger: Boolean = false;

    fun checkBroadphase(): Boolean {
        broadphase = Collisions.checkCollision(c1.getMinBounds(), c2.getMinBounds());
        if (!broadphase) stopCollision() // no longer colliding
        return broadphase
    }

    fun checkNarrowphase(): Manifold? {
        if (!broadphase) return null // don't check if not broadphase
        if (c1.trigger && c2.trigger) return null // don't call or resolve if both are triggers

        val manifold = c1.getContacts(c2)
        if (manifold == null) stopCollision() // no longer colliding
        else if (!colliding) startCollision() // has not collided before
        else continueCollision() // has collided before

        trigger = c1.trigger || c2.trigger // if either is trigger then call but don't resolve
        return if (trigger) null else manifold
    }

    private fun startCollision() {
        if (!colliding) {
            colliding = true
            c1.startCollision(c2, trigger)
            c2.startCollision(c1, trigger)
        }
    }

    private fun continueCollision() {
        c1.continueCollision(c2, trigger)
        c2.continueCollision(c1, trigger)
    }

    private fun stopCollision() {
        if (colliding) {
            colliding = false
            c1.stopCollision(c2, trigger)
            c2.stopCollision(c1, trigger)
        }
    }

    fun match(col: Collider): Boolean {
        return c1 == col || c2 == col
    }

    fun match(c1: Collider, c2: Collider): Boolean {
        return (this.c1 == c1 && this.c2 == c2) || (this.c1 == c2 && this.c2 == c1)
    }
}