package mayonez.scripts.movement;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;

/**
 * Provides basic methods for moving and rotating game objects using input devices.
 * This class does not move the object by itself, so subclasses should override and
 * call the {@link #moveObject} or {@link #rotateObject} methods.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MovementScript extends Script {

    protected Rigidbody rb = null; // Reference to object rigidbody

    public MovementScript() {
        super(UpdateOrder.INPUT);
    }

    @Override
    protected void start() {
        rb = getRigidbody();
        if (rb == null) {
            Logger.warn("%s has a null rigidbody", this);
        }
    }

    // Move Methods

    /**
     * Move the game object by the specified amount.
     *
     * @param amount the move input
     * @param dt     the duration of the frame
     */
    public void moveObject(Vec2 amount, float dt) {
    }

    /**
     * Rotate the game object by the specified amount.
     *
     * @param amount the turn input
     * @param dt     the duration of the frame
     */
    public void rotateObject(float amount, float dt) {
    }

    // Clamp Speed Methods

    /**
     * Limit the linear speed of the game object.
     *
     * @param topSpeed the top speed in m/s
     */
    public void clampSpeed(float topSpeed) {
        if (rb == null) return;
        var speed = rb.getSpeed();
        if ((topSpeed > 0f) && (speed > topSpeed)) {
            rb.setVelocity(rb.getVelocity().mul(topSpeed / speed));
        }
    }

    /**
     * Limit the rotational speed of the game object.
     *
     * @param topSpeed the top angular speed in deg/s
     */
    public void clampAngularSpeed(float topSpeed) {
        if (rb == null) return;
        if ((topSpeed > 0f) && (rb.getAngSpeed() > topSpeed)) {
            rb.setAngVelocity(topSpeed * Math.signum(rb.getAngVelocity()));
        }
    }

    // Input Methods

    /**
     * Detect the user's two-dimensional input.
     *
     * @return the input vector, (0, 0) by default
     */
    public Vec2 getUserInput() {
        return new Vec2();
    }

    /**
     * Detect the user's one-dimensional input.
     *
     * @return the input value, 0 by default
     */
    public float getUserInputValue() {
        return 0f;
    }

}
