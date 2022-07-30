package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.physics.Rigidbody;

/**
 * A base class contains utility methods for scripts using the input devices.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MovementScript extends Script {

    public float speed = 0, topSpeed = -1; // If parent has physics clamp rb velocity
    protected MoveMode mode = MoveMode.POSITION; // How to move the parent object
    protected Rigidbody rb = null; // Reference to object rigidbody

    protected MovementScript() {}

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
