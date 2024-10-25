package mayonez.scripts.movement;

import mayonez.input.*;
import mayonez.math.*;

/**
 * Allows objects to be moved along two axes with keyboard controls.
 *
 * @author SlavSquatSuperstar
 */
public class KeyMovement extends MovementScript {

    private final float speed;
    private final MoveMode mode;
    private final InputAxis xAxis, yAxis;

    /**
     * Constructs a new KeyMovement script with the given input axes.
     *
     * @param speed how fast to move the object
     * @param mode  how to move the object
     * @param xAxis the horizontal input axis
     * @param yAxis the vertical input axis
     */
    public KeyMovement(float speed, MoveMode mode, InputAxis xAxis, InputAxis yAxis) {
        this.speed = speed;
        this.mode = mode;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

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

    @Override
    protected void update(float dt) {
        moveObject(getUserInput().mul(speed), mode, dt);
    }

    @Override
    public Vec2 getUserInput() {
        return new Vec2(KeyInput.getAxis(xAxis), KeyInput.getAxis(yAxis));
    }

}
