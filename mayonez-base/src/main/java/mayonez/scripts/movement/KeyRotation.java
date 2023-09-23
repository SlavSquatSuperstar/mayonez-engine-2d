package mayonez.scripts.movement;

import mayonez.input.*;
import mayonez.math.*;

/**
 * Allows objects to be rotated with keyboard controls.
 *
 * @author SlavSquatSuperstar
 */
public class KeyRotation extends MovementScript {

    private final InputAxis axis;

    /**
     * Constructs a new KeyRotation script with the given input axis.
     *
     * @param angSpeed how fast to rotate the object
     * @param mode     how to rotate the object
     * @param axis     the input axis
     */
    public KeyRotation(float angSpeed, MoveMode mode, InputAxis axis) {
        super(angSpeed, mode);
        this.axis = axis;
    }

    @Override
    public void update(float dt) {
        var input = -getUserInput().x * speed;

        rotateGameObject(dt, input);
        clampSpeed();
    }

    private void rotateGameObject(float dt, float input) {
        switch (mode) {
            case POSITION -> transform.rotate(input * dt);
            case VELOCITY -> rb.addAngularVelocity(input * dt);
            case ACCELERATION -> rb.applyAngularAcceleration(input);
            case IMPULSE -> rb.applyAngularImpulse(input);
            case FORCE -> rb.applyTorque(input);
        }
    }

    private void clampSpeed() {
        if (rb == null) return;
        if ((topSpeed > 0) && (rb.getAngSpeed() > topSpeed)) {
            rb.setAngVelocity(topSpeed * Math.signum(rb.getAngVelocity()));
        }
    }

    @Override
    public Vec2 getUserInput() {
        return new Vec2(KeyInput.getAxis(axis), 0);
    }

}
