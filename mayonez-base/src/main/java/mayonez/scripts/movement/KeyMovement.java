package mayonez.scripts.movement;

import mayonez.input.*;
import mayonez.math.*;

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
        var input = getUserInput().unit().mul(speed); // Normalize so don't move faster diagonally
        if (objectAligned) input = input.rotate(transform.getRotation()); // Align to object space if enabled

        switch (mode) {
            case POSITION -> transform.move(input.mul(dt));
            case VELOCITY -> rb.addVelocity(input.mul(dt));
            case ACCELERATION -> rb.applyAcceleration(input);
            case IMPULSE -> rb.applyImpulse(input);
            case FORCE -> rb.applyForce(input);
        }

        // Limit Top Speed
        if (rb != null && topSpeed > 0 && rb.getSpeed() > topSpeed)
            rb.setVelocity(rb.getVelocity().mul(topSpeed / rb.getSpeed()));
    }

    @Override
    public Vec2 getUserInput() {
        return new Vec2(Input.getAxis(xAxis), Input.getAxis(yAxis));
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
