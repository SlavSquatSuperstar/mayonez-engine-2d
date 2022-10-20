package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.physics.Rigidbody;

/**
 * How a {@link Script} should affect the object's position or movement.
 *
 * @author SlavSquatSuperstar
 */
public enum MoveMode {
    /**
     * Lock the object's position to the mouse pointer.
     */
    FOLLOW_MOUSE(false),
    /**
     * Change the object's transform position.
     */
    POSITION(false),
    /**
     * Change the object's velocity (requires a {@link Rigidbody}).
     */
    VELOCITY(true),
    /**
     * Add an impulse to the object (requires a {@link Rigidbody}).
     */
    IMPULSE(true),
    /**
     * Add an acceleration to the object (requires a {@link Rigidbody}).
     */
    ACCELERATION(true),
    /**
     * Add a force to the object (requires a {@link Rigidbody}).
     */
    FORCE(true);

    final boolean requireRigidbody;

    MoveMode(boolean requireRigidbody) {
        this.requireRigidbody = requireRigidbody;
    }
}
