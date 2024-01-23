package mayonez.scripts.movement;

import mayonez.input.*;

/**
 * Allows objects to be rotated with keyboard controls.
 *
 * @author SlavSquatSuperstar
 */
public class KeyRotation extends MovementScript {

    private final float angSpeed;
    private final MoveMode mode;
    private final InputAxis axis;

    /**
     * Constructs a new KeyRotation script with the given input axis.
     *
     * @param angSpeed how fast to rotate the object
     * @param mode     how to rotate the object
     * @param axis     the input axis
     */
    public KeyRotation(float angSpeed, MoveMode mode, InputAxis axis) {
        this.angSpeed = angSpeed;
        this.mode = mode;
        this.axis = axis;
    }

    @Override
    public void update(float dt) {
        rotateObject(-getUserInputValue() * angSpeed, mode, dt);
    }

    @Override
    public float getUserInputValue() {
        return KeyInput.getAxis(axis);
    }

}
