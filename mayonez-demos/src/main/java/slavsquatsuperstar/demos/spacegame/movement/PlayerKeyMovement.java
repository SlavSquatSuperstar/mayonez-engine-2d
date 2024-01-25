package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.input.*;
import mayonez.math.*;
import mayonez.scripts.movement.*;

/**
 * Controls the player spaceship's movement using the keyboard.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerKeyMovement extends MovementScript {

    private final float moveSpeed, turnSpeed;
    private final MoveMode moveMove, turnMode;
    private final InputAxis xAxis, yAxis, turnAxis;

    public PlayerKeyMovement(
            float moveSpeed, MoveMode moveMove, InputAxis xAxis, InputAxis yAxis,
            float turnSpeed, MoveMode turnMode, InputAxis turnAxis
    ) {
        this.moveSpeed = moveSpeed;
        this.moveMove = moveMove;
        this.xAxis = xAxis;
        this.yAxis = yAxis;

        this.turnSpeed = turnSpeed;
        this.turnMode = turnMode;
        this.turnAxis = turnAxis;
    }

    @Override
    protected void update(float dt) {
        moveObject(getUserInput().mul(moveSpeed), moveMove, dt);
        rotateObject(-getUserInputValue() * turnSpeed, turnMode, dt);
    }

    @Override
    public Vec2 getUserInput() {
        return new Vec2(KeyInput.getAxis(xAxis), KeyInput.getAxis(yAxis))
                .rotate(transform.getRotation()); // Align with object direction
    }

    @Override
    public float getUserInputValue() {
        return KeyInput.getAxis(turnAxis);
    }

}
