package slavsquatsuperstar.mayonez.components.scripts;

import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;

public abstract class MovementScript extends Script {

    public float speed = 0, topSpeed = -1; // if parent has physics clamp rb velocity
    protected Rigidbody2D rb = null; // Reference to object rigidbody
    protected Mode mode = Mode.POSITION;

    protected MovementScript() {}

    public MovementScript(Mode mode, float speed) {
        this.mode = mode;
        this.speed = speed;
    }

    @Override
    public void start() {
        if (mode.requireRigidbody) {
            rb = parent.getComponent(Rigidbody2D.class);
            if (rb == null)
                Logger.log("%s needs a Rigidbody to function!", getClass().getSimpleName());
        }
    }

    /**
     * Detect the player's input.
     *
     * @return the input vector
     */
    protected abstract Vector2 getRawInput();

    /**
     * How this script should affect the object's position or movement.
     */
    public enum Mode {
        /**
         * Follow the mouse pointer.
         */
        FOLLOW_MOUSE(false),
        /**
         * Change the object's position.
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

        private final boolean requireRigidbody;

        Mode(boolean requireRigidbody) {
            this.requireRigidbody = requireRigidbody;
        }
    }

}
