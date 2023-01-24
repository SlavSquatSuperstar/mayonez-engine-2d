package mayonez.scripts.movement;

import mayonez.input.KeyInput;
import mayonez.math.Vec2;

/**
 * Allows objects to be rotated with keyboard controls.
 *
 * @author SlavSquatSuperstar
 */
public class KeyRotation extends MovementScript {

    private final String axis;

    public KeyRotation(MoveMode mode, float angSpeed) {
        this(mode, angSpeed, "horizontal2");
    }

    public KeyRotation(MoveMode mode, float angSpeed, String axis) {
        super(mode, angSpeed);
        this.axis = axis;
    }

    @Override
    public void update(float dt) {
        var input = -getUserInput().x * speed;
        switch (mode) {
            case POSITION -> transform.rotate(input * dt);
            case VELOCITY -> rb.addAngularVelocity(input * dt);
            case ACCELERATION -> rb.applyAngularAcceleration(input);
            case IMPULSE -> rb.applyAngularImpulse(input);
            case FORCE -> rb.applyTorque(input);
        }
        // Limit Top Speed
        if (rb != null && topSpeed > 0 && rb.getAngSpeed() > topSpeed)
            rb.setAngVelocity(topSpeed * Math.signum(rb.getAngVelocity()));
    }

    @Override
    public Vec2 getUserInput() {
        return new Vec2(KeyInput.getAxis(axis), 0);
    }

    public KeyRotation setTopSpeed(float topSpeed) {
        this.topSpeed = topSpeed;
        return this;
    }

}
