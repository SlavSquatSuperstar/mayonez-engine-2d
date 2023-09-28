package mayonez.scripts.movement;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;

/**
 * A base class contains utility methods for scripts using the input devices.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MovementScript extends Script {

    protected final float speed;
    protected float topSpeed = -1; // If rb velocity should be clamped
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

    @SuppressWarnings("unchecked")
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
