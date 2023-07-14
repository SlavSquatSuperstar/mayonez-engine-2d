package mayonez.scripts.movement;

import mayonez.input.*;
import mayonez.math.*;

/**
 * Allows objects to be rotated with keyboard controls.
 *
 * @author SlavSquatSuperstar
 */
public class KeyRotation extends MovementScript {

    private final String axis;

    public KeyRotation(float angSpeed, MoveMode mode) {
        this(angSpeed, mode, "horizontal2");
    }

    public KeyRotation(float angSpeed, MoveMode mode, String axis) {
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
