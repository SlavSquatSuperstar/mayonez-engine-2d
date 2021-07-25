package slavsquatsuperstar.mayonez.scripts;

import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;

/**
 * A base class contains utility methods for scripts using the input devices.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MovementScript extends Script {

    public float speed = 0, topSpeed = -1; // If parent has physics clamp rb velocity
    protected MoveMode mode = MoveMode.POSITION; // How to move the parent object
    protected Rigidbody2D rb = null; // Reference to object rigidbody

    protected MovementScript() {}

    public MovementScript(MoveMode mode, float speed) {
        this.mode = mode;
        this.speed = speed;
    }

    @Override
    public void start() {
        if (mode.requireRigidbody) {
            rb = parent.getComponent(Rigidbody2D.class);
            if (rb == null) {
                Logger.warn("%s needs a Rigidbody to function!", getClass().getSimpleName());
                mode = MoveMode.POSITION;
            }
        }
    }

    /**
     * Detect the player's input.
     *
     * @return the input vector
     */
    protected abstract Vector2 getRawInput();

}
