package mayonez.scripts.movement;

import mayonez.input.KeyInput;
import mayonez.math.Vec2;

/**
 * Allows objects to be moved with keyboard controls.
 *
 * @author SlavSquatSuperstar
 */
public class KeyMovement extends MovementScript {

    private final String xAxis, yAxis;
    private boolean objectAligned; // Whether to align movement with object's rotation

    public KeyMovement(MoveMode mode, float speed) {
        this(mode, speed, "horizontal", "vertical");
    }

    public KeyMovement(MoveMode mode, float speed, String xAxis, String yAxis) {
        super(mode, speed);
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        objectAligned = false;
    }

    @Override
    public void update(float dt) {
        Vec2 input = getRawInput().unit().mul(speed); // Normalize so don't move faster diagonally
        if (objectAligned) input = input.rotate(transform.rotation); // Align to object space if enabled

        switch (mode) {
            case POSITION -> transform.move(input.mul(dt));
            case VELOCITY -> rb.addVelocity(input);
            case ACCELERATION -> rb.applyAcceleration(input);
            case IMPULSE -> rb.applyImpulse(input);
            case FORCE -> rb.applyForce(input);
        }

        // Limit Top Speed
        if (rb != null && topSpeed > 0 && rb.getSpeed() > topSpeed)
            rb.setVelocity(rb.getVelocity().mul(topSpeed / rb.getSpeed()));
    }

    @Override
    protected Vec2 getRawInput() {
        return new Vec2(KeyInput.getAxis(xAxis), KeyInput.getAxis(yAxis));
    }

    public KeyMovement setTopSpeed(float topSpeed) {
        this.topSpeed = topSpeed;
        return this;
    }

    public KeyMovement setObjectAligned(boolean objectAligned) {
        this.objectAligned = objectAligned;
        return this;
    }
}
