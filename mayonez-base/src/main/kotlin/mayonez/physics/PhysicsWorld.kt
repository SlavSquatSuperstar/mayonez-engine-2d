package mayonez.physics

import mayonez.*
import mayonez.math.*

/**
 * A simulation containing bodies that approximate real-world physics.
 *
 * @author SlavSquatSuperstar
 */
interface PhysicsWorld {

    companion object {
        /**
         * The gravitational acceleration on Earth's surface, g, equal to 9.81
         * m/s/s.
         */
        const val GRAVITY_CONSTANT = 9.81f
    }

    // Physics Properties

    /** The acceleration due to gravity, in m/s/s. */
    var gravity: Vec2

    // Game Object Methods

    /**
     * Add an object and its attached body and colliders to the simulation.
     *
     * @param obj the game object to add
     */
    fun addObject(obj: GameObject)

    /**
     * Remove an object and its attached body and colliders from the
     * simulation.
     *
     * @param obj the game object to remove
     */
    fun removeObject(obj: GameObject)

    /** Removes all objects and frees any resources from the physics world. */
    fun clearObjects()

    // Physics Methods

    /**
     * Updates all objects in the physics simulation by the given time step.
     *
     * @param dt seconds since the last frame
     */
    fun step(dt: Float)

}