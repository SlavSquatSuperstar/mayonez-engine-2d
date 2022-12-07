package mayonez.scripts.movement;

import mayonez.Mayonez;
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
        float input = -getRawInput().x * speed; // Normalize so don't move faster diagonally
        switch (mode) {
            case POSITION -> transform.rotate(input * Mayonez.TIME_STEP);
            case VELOCITY -> rb.addAngularVelocity(input);
            case ACCELERATION -> rb.applyAngularAcceleration(input);
            case IMPULSE -> rb.applyAngularImpulse(input);
            case FORCE -> rb.applyTorque(input);
        }
        // Limit Top Speed
        if (rb != null && topSpeed > 0 && rb.getAngSpeed() > topSpeed)
            rb.setAngVelocity(topSpeed * Math.signum(rb.getAngVelocity()));
    }

    @Override
    protected Vec2 getRawInput() {
        return new Vec2(KeyInput.getAxis(axis));
    }

    public KeyRotation setTopSpeed(float topSpeed) {
        this.topSpeed = topSpeed;
        return this;
    }

}
