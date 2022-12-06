package mayonez.scripts;

import mayonez.math.Vec2;
import mayonez.Logger;
import mayonez.Script;
import mayonez.physics.Rigidbody;

/**
 * A base class contains utility methods for scripts using the input devices.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MovementScript extends Script {

    public float speed, topSpeed = -1; // If parent has physics clamp rb velocity
    protected MoveMode mode; // How to move the parent object
    protected Rigidbody rb = null; // Reference to object rigidbody

    public MovementScript(MoveMode mode, float speed) {
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

    /**
     * Detect the player's input.
     *
     * @return the input vector
     */
    protected abstract Vec2 getRawInput();

}
