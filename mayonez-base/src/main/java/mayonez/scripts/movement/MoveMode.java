package mayonez.scripts.movement;

import mayonez.Script;
import mayonez.physics.Rigidbody;

/**
 * How a {@link Script} should affect the object's position or movement. Not all scripts will make use of all of these.
 *
 * @author SlavSquatSuperstar
 */
public enum MoveMode {
    /**
     * Lock the object's position to the mouse pointer.
     */
    FOLLOW_MOUSE(false),
    /**
     * Change the object's transform position or rotation.
     */
    POSITION(false),
    /**
     * Change the object's velocity (requires a {@link Rigidbody}).
     */
    VELOCITY(true),
    /**
     * Add an acceleration to the object (requires a {@link Rigidbody}).
     */
    ACCELERATION(true),
    /**
     * Apply an impulse to the object (requires a {@link Rigidbody}).
     */
    IMPULSE(true),
    /**
     * Apply a force or torque to the object (requires a {@link Rigidbody}).
     */
    FORCE(true);

    final boolean requireRigidbody;

    MoveMode(boolean requireRigidbody) {
        this.requireRigidbody = requireRigidbody;
    }
}
