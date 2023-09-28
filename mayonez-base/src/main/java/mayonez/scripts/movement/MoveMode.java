package mayonez.scripts.movement;

/**
 * How a {@link mayonez.Script} should affect the object's position or movement. Not all scripts will make use of all of these.
 *
 * @author SlavSquatSuperstar
 */
public enum MoveMode {

    /**
     * Change the object's transform position or rotation.
     */
    POSITION(false),
    /**
     * Change the object's velocity (requires a {@link mayonez.physics.dynamics.Rigidbody}).
     */
    VELOCITY(true),
    /**
     * Apply an impulse to the object (requires a {@link mayonez.physics.dynamics.Rigidbody}).
     */
    IMPULSE(true),
    /**
     * Apply a force or torque to the object (requires a {@link mayonez.physics.dynamics.Rigidbody}).
     */
    FORCE(true);

    final boolean requireRigidbody;

    MoveMode(boolean requireRigidbody) {
        this.requireRigidbody = requireRigidbody;
    }

}
