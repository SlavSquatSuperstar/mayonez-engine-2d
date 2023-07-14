package mayonez.scripts.movement;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.*;

/**
 * A base class contains utility methods for scripts using the input devices.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MovementScript extends Script {

    public float speed;
    public float topSpeed = -1; // If should clamp rb velocity
    protected MoveMode mode; // How to move the parent object
    protected Rigidbody rb = null; // Reference to object rigidbody

    public MovementScript(float speed, MoveMode mode) {
        this.mode = mode;
        this.speed = speed;
    }

    @Override
    public void start() {
        rb = getRigidbody();
        if (mode.requireRigidbody && rb == null) {
            Logger.warn("%s needs a rigidbody to function!", this);
            mode = MoveMode.POSITION;
        }
    }

    @SuppressWarnings({"unchecked"})
    public final <T extends MovementScript> T setTopSpeed(float topSpeed) {
        this.topSpeed = topSpeed;
        return (T) this;
    }

    /**
     * Detect the user's input.
     *
     * @return the input vector
     */
    public abstract Vec2 getUserInput();

}
