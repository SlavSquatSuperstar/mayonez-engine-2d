package mayonez.physics

import mayonez.math.*
import mayonez.physics.colliders.*
import mayonez.physics.dynamics.*
import mayonez.physics.resolution.*

/**
 * The default implementation of a [PhysicsWorld].
 *
 * Sources:
 * - [GamesWithGabe Coding a 2D Physics
 *   Engine](https://youtube.com/playlist?list=PLtrSb4XxIVbpZpV65kk73OoUcIrBzoSiO)
 * - [Two-Bit Coding Let's Make a Physics
 *   Engine](https://www.youtube.com/playlist?list=PLSlpr6o9vURwq3oxVZSimY8iC-cdd3kIs)
 *
 * @author SlavSquatSuperstar
 */
class DefaultPhysicsWorld : PhysicsWorld {

    // World Properties
    override var gravity: Vec2 = Vec2()

    // Bodies and Collisions
    private val bodies: MutableList<PhysicsBody> // physical objects in the world
    private val colliders: MutableList<CollisionBody> // shapes in the world
    private val listeners: MutableSet<CollisionListener> // all collision listeners

    // TODO use adjacency list?
    private val collisions: MutableList<CollisionSolver> // confirmed narrowphase collisions

    init {
        gravity = Vec2(0f, -PhysicsWorld.GRAVITY_CONSTANT)
        bodies = ArrayList()
        colliders = ArrayList()
        listeners = HashSet()
        collisions = ArrayList()
    }

    // Body Methods

    // TODO make sure not adding duplicates
    override fun addCollisionBody(body: CollisionBody?) {
        colliders.add(body ?: return)
    }

    override fun addPhysicsBody(body: PhysicsBody?) {
        bodies.add(body ?: return)
    }

    override fun removeCollisionBody(body: CollisionBody?) {
        colliders.remove(body ?: return)
        listeners.removeIf { it.match(body) }
    }

    override fun removePhysicsBody(body: PhysicsBody?) {
        bodies.remove(body ?: return)
    }

    override fun clear() {
        bodies.clear()
        colliders.clear()
        listeners.clear()
        collisions.clear()
    }

    // Game Object Methods

    override fun step(dt: Float) {
        // Update bodies
        bodies.forEach {
            it.integrateForce(dt, gravity)
            it.integrateVelocity(dt)
        }

        // Detect collisions
        // TODO Pre-collision optimizations and spatial partitioning
        collisions.clear()
        detectBroadPhase()
        detectNarrowPhase()

        // Resolve Collisions
        collisions.forEach(CollisionSolver::solveCollision)
    }

    // Collision Detection Methods

    /**
     * Detect potential collisions between bounding boxes while avoiding
     * expensive contact calculations.
     */
    private fun detectBroadPhase() {
        for (i in colliders.indices) {
            // Avoid duplicate collisions between two objects and checking against self
            for (j in i + 1..<colliders.size) {
                val c1 = colliders[i]
                val c2 = colliders[j]

                // Reset collision flags
                c1.collisionResolved = false
                c2.collisionResolved = false

                if (c1.canCollide(c2)) {
                    val lis = listeners.find { it.match(c1, c2) }
                        ?: CollisionListener(c1, c2)
                    if (lis.checkBroadphase()) listeners.add(lis)
                }
            }
        }
    }

    /** Check broadphase pairs for collisions and calculate contact points. */
    private fun detectNarrowPhase() {
        for (lis in listeners) {
            val collision = lis.checkNarrowphase() ?: continue // Get contacts
            val c1 = lis.c1
            val c2 = lis.c2
            collisions.add(CollisionSolver(c1, c2, collision)) // Resolve collisions
        }
    }

}