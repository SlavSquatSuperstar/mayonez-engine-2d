package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.dynamics.Rigidbody;

/**
 * Controls a spaceship's movement and rotation and activates its thrusters.
 *
 * @author SlavSquatSuperstar
 */
public abstract class SpaceshipMovement extends Node {

    // Constants
    public final static float BRAKE_THRESHOLD_SPEED = 0.1f;
    public final static float TURN_BRAKE_THRESHOLD_SPEED = 2f;

    // Node References
    protected Rigidbody rb;
    private ThrustController thrustController;

    @Override
    protected void start() {
        setUpdateOrder(UpdateOrder.INPUT);
        rb = getSibling(Rigidbody.class);
        thrustController = getSibling(ThrustController.class);
        if (rb == null) {
            Logger.warn("%s has a null rigidbody", this);
            if (rb == null) setEnabled(false);
        }
    }

    // Movement Methods

    /**
     * Move the object by the specified amount.
     *
     * @param amount the move input
     * @param dt     the duration of the frame
     */
    public void moveObject(Vec2 amount, float dt) {
        rb.applyForce(amount);
    }

    /**
     * Rotate the object by the specified amount.
     *
     * @param amount the turn input
     * @param dt     the duration of the frame
     */
    public void rotateObject(float amount, float dt) {
        rb.applyTorque(amount);
    }

    // Brake Methods

    /**
     * Slow down the object's motion in the specific directions.
     *
     * @param brakeDir    the linear brake direction
     * @param angBrakeDir the rotational brake direction
     */
    protected void brake(Vec2 brakeDir, float angBrakeDir) {
    }

    /**
     * Get the linear brake direction given the user input.
     *
     * @param moveInput the linear move input
     * @return the linear brake direction
     */
    protected Vec2 getBrakeDir(Vec2 moveInput) {
        return new Vec2();
    }

    /**
     * Get the rotational brake direction given the user input.
     *
     * @param turnInput the rotational move input
     * @return the rotational brake direction
     */
    protected float getTurnBrakeDir(float turnInput) {
        return 0f;
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
     * Detect the user's linear move input vector.
     *
     * @return the input vector, (0, 0) by default
     */
    protected Vec2 getMoveInput() {
        return new Vec2();
    }

    /**
     * Detect the user's rotational input scalar.
     *
     * @return the input value, 0 by default
     */
    protected float getTurnInput() {
        return 0f;
    }

    // Component Getters

    protected ThrustController getThrustController() {
        return thrustController;
    }

}
