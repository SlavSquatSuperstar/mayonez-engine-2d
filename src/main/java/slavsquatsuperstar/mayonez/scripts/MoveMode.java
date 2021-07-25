package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;

/**
 * How a {@link Script} should affect the object's position or movement.
 */
public enum MoveMode {
    /**
     * Follow the mouse pointer.
     */
    FOLLOW_MOUSE(false),
    /**
     * Change the object's transform position.
     */
    POSITION(false),
    /**
     * Change the object's velocity (requires a {@link Rigidbody2D}).
     */
    VELOCITY(true),
    /**
     * Add an impulse to the object (requires a {@link Rigidbody2D}).
     */
    IMPULSE(true),
    /**
     * Add an acceleration to the object (requires a {@link Rigidbody2D}).
     */
    ACCELERATION(true),
    /**
     * Add a force to the object (requires a {@link Rigidbody2D}).
     */
    FORCE(true);

    final boolean requireRigidbody;

    MoveMode(boolean requireRigidbody) {
        this.requireRigidbody = requireRigidbody;
    }
}
