package mayonez.physics

import mayonez.math.Vec2
import mayonez.physics.colliders.CollisionBody
import mayonez.physics.dynamics.PhysicsBody

/**
 * A simulation containing bodies that approximate real-world physics.
 *
 * @author SlavSquatSuperstar
 */
interface PhysicsWorld {

    companion object {
        /**
         * The gravitational acceleration on Earth's surface, g, equal to about
         * 9.81 m/s^2.
         */
        const val GRAVITY_CONSTANT = 9.80665f
    }

    // Physics Properties

    /**
     * The acceleration due to gravity, in m/s^2.
     */
    var gravity: Vec2

    /**
     * The default physics layers available to collidable objects.
     */
    val layers: Array<PhysicsLayer>

    // Game Object Methods

    /**
     * Add a collision body to this simulation.
     *
     * @param body the collision body to add
     */
    fun addCollisionBody(body: CollisionBody?)

    /**
     * Add a physics body to this simulation.
     *
     * @param body the physics body to add
     */
    fun addPhysicsBody(body: PhysicsBody?)

    /**
     * Removes a collision body to this simulation.
     *
     * @param body the collision body to remove
     */
    fun removeCollisionBody(body: CollisionBody?)

    /**
     * Removes a physics body to this simulation.
     *
     * @param body the physics body to remove
     */
    fun removePhysicsBody(body: PhysicsBody?)

    /** Removes all objects and frees any resources from the physics world. */
    fun clear()

    // Physics Methods

    /**
     * Updates all objects in the physics simulation by the given time step.
     *
     * @param dt seconds since the last frame
     */
    fun step(dt: Float)

}