package mayonez.physics

import mayonez.*
import mayonez.math.*
import mayonez.physics.colliders.*
import mayonez.physics.resolution.*

/**
 * A simulation containing bodies that approximate real-world physics.
 *
 * Sources:
 * - [GamesWithGabe Coding a 2D Physics Engine](https://youtube.com/playlist?list=PLtrSb4XxIVbpZpV65kk73OoUcIrBzoSiO)
 *
 * @author SlavSquatSuperstar
 */
// todo add 2-bit coding
class PhysicsWorld {

    companion object {
        const val GRAVITY_CONSTANT = 9.8f
    }

    // World Properties
    private var gravity: Vec2 = Vec2() // acceleration due to gravity

    // Bodies and Collisions
    private val bodies: MutableList<Rigidbody> // physical objects in the world
    private val colliders: MutableList<Collider> // shapes in the world
    private val listeners: MutableSet<CollisionListener> // all collision listeners
    private val collisions: MutableList<CollisionSolver> // confirmed narrowphase collisions

    init {
        bodies = ArrayList()
        colliders = ArrayList()
        listeners = HashSet()
        collisions = ArrayList()
        gravity = Vec2(0f, -GRAVITY_CONSTANT)
    }

    fun start() {
        bodies.clear()
        colliders.clear()
        listeners.clear()
        collisions.clear()
    }

    /**
     * Updates all objects in the physics simulation by the given time step.
     *
     * @param dt seconds since the last frame
     */
    /*
     * Pre-collision optimizations
     * Detect collisions x1
     * Resolve static collisions x1
     * Send collision events x2
     * Resolve dynamic collisions x2
     *
     * Integrate forces and velocities
     */
    fun step(dt: Float) {
        collisions.clear()

        updateBodies(dt)

        // TODO Pre-collision optimizations and spatial partitioning
        detectBroadPhase()
        detectNarrowPhase()

        collisions.forEach(CollisionSolver::solveCollision)
    }

    private fun updateBodies(dt: Float) {
        bodies.forEach {
            // Apply gravity
            if (it.followsGravity) it.applyForce(gravity * it.mass)
            it.integrateForce(dt)
            it.integrateVelocity(dt)
        }
    }

    // Collision Detection Methods

    /**
     * Detect potential collisions between bounding boxes while avoiding
     * expensive contact calculations.
     */
    private fun detectBroadPhase() {
        for (i in colliders.indices) {
            // Avoid duplicate collisions between two objects and checking against self
            for (j in i + 1 until colliders.size) {
                val c1 = colliders[i]
                val c2 = colliders[j]

                // Reset collision flags
                c1.collisionResolved = false
                c2.collisionResolved = false

                if (collidersCannotCollide(c1, c2)) continue

                val lis = getListener(c1, c2)
                if (lis.checkBroadphase()) listeners.add(lis)
            }
        }
    }

    /**
     * Get the collision listener that matches the with two colliders
     *
     * @param c1 the first collider
     * @param c2 the second collider
     * @return an existing listener or a new listener
     */
    private fun getListener(c1: Collider, c2: Collider): CollisionListener {
        for (lis in listeners) {
            if (lis.match(c1, c2)) return lis
        }
        return CollisionListener(c1, c2)
    }

    private fun collidersCannotCollide(c1: Collider, c2: Collider): Boolean {
        val disabled = !(c1.isEnabled && c2.isEnabled)
        val ignore = c1.gameObject.hasTag("Ignore Collisions") || c2.gameObject.hasTag("Ignore Collisions")
        val static = c1.isStatic() && c2.isStatic() // Don't check for collision if both are static
        if (disabled || ignore || static) return true
        return false
    }

    /** Check broadphase pairs for collisions and calculate contact points. */
    private fun detectNarrowPhase() {
        for (lis in listeners) {
            val collision = lis.checkNarrowphase() ?: continue // Get contacts
            val c1 = lis.c1
            val c2 = lis.c2

            // Don't resolve if either object called ignore collision
            if (c1.ignoreCurrentCollision || c2.ignoreCurrentCollision) {
                c1.ignoreCurrentCollision = false
                c2.ignoreCurrentCollision = false
                continue
            }
            collisions.add(CollisionSolver(c1, c2, collision)) // Resolve collisions
        }
    }

    // Game Object Methods

    fun addObject(obj: GameObject) {
        val rb = obj.getComponent(Rigidbody::class.java)
        if (rb != null) bodies.add(rb)
        val col = obj.getComponent(Collider::class.java)
        if (col != null) colliders.add(col)
    }

    fun removeObject(obj: GameObject) {
        bodies.remove(obj.getComponent(Rigidbody::class.java))
        val col = obj.getComponent(Collider::class.java)
        colliders.remove(col)
        listeners.stream()
            .filter { lis -> lis.match(col) }
            .toList() // needed to avoid ConcurrentModificationException
            .forEach(listeners::remove)
    }

    fun setScene(newScene: Scene) {
        bodies.clear()
        colliders.clear()
        newScene.objects.forEach(this::addObject)
    }

    fun stop() {
        bodies.clear()
        colliders.clear()
        listeners.clear()
        collisions.clear()
    }

    // Getters and Setters

    fun setGravity(gravity: Vec2?) {
        this.gravity = gravity ?: Vec2()
    }

}