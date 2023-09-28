package mayonez.scripts.movement;

import mayonez.input.*;
import mayonez.math.*;

/**
 * Allows objects to be moved along two axes with keyboard controls.
 *
 * @author SlavSquatSuperstar
 */
public class KeyMovement extends MovementScript {

    private final InputAxis xAxis, yAxis;
    private boolean objectAligned; // Whether to align movement with object's rotation

    /**
     * Constructs a new KeyMovement script with the WASD keys.
     *
     * @param speed how fast to move the object
     * @param mode  how to move the object
     */
    public KeyMovement(float speed, MoveMode mode) {
        this(speed, mode, DefaultKeyAxis.findWithName("horizontal"),
                DefaultKeyAxis.findWithName("vertical"));
    }

    /**
     * Constructs a new KeyMovement script with the given input axes.
     *
     * @param speed how fast to move the object
     * @param mode  how to move the object
     * @param xAxis the horizontal input axis
     * @param yAxis the vertical input axis
     */
    public KeyMovement(float speed, MoveMode mode, InputAxis xAxis, InputAxis yAxis) {
        super(speed, mode);
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        objectAligned = false;
    }

    @Override
    public void update(float dt) {
        var input = getUserInput().unit().mul(speed); // Normalize so don't move faster diagonally
        if (objectAligned) input = input.rotate(transform.getRotation()); // Align to object space if enabled

        moveGameObject(dt, input);
        clampSpeed();
    }

    private void moveGameObject(float dt, Vec2 input) {
        switch (mode) {
            case POSITION -> transform.move(input.mul(dt));
            case VELOCITY -> rb.addVelocity(input.mul(dt));
            case IMPULSE -> rb.applyImpulse(input);
            case FORCE -> rb.applyForce(input);
        }
    }

    private void clampSpeed() {
        if (rb == null) return;
        // If enabled and exceeding top speed
        if ((topSpeed > 0f) && (rb.getSpeed() > topSpeed)) {
            rb.setVelocity(rb.getVelocity().mul(topSpeed / rb.getSpeed()));
        }
    }

    @Override
    public Vec2 getUserInput() {
        return new Vec2(KeyInput.getAxis(xAxis), KeyInput.getAxis(yAxis));
    }

    public KeyMovement setObjectAligned(boolean objectAligned) {
        this.objectAligned = objectAligned;
        return this;
    }
}
