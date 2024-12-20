package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.input.*;
import mayonez.math.*;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameConfig;

/**
 * Controls the player spaceship's movement using the keyboard.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerKeyMovement extends SpaceshipMovement {

    // Config
    private static final InputAxis HORIZONTAL_MOVE_AXIS = SpaceGameConfig.getHorizontalMoveAxis();
    private static final InputAxis VERTICAL_MOVE_AXIS = SpaceGameConfig.getVerticalMoveAxis();
    private static final InputAxis TURN_AXIS = SpaceGameConfig.getTurnAxis();
    private static final Key BRAKE_KEY = SpaceGameConfig.getBreakKey();

    // Movement Fields
    private final float moveSpeed, turnSpeed;
    private final MoveMode moveMove, turnMode;
    private final InputAxis xAxis, yAxis, turnAxis;

    public PlayerKeyMovement(
            float moveSpeed, MoveMode moveMove, float turnSpeed, MoveMode turnMode
    ) {
        this.moveSpeed = moveSpeed;
        this.moveMove = moveMove;
        xAxis = HORIZONTAL_MOVE_AXIS;
        yAxis = VERTICAL_MOVE_AXIS;

        this.turnSpeed = turnSpeed;
        this.turnMode = turnMode;
        turnAxis = TURN_AXIS;
    }

    @Override
    protected void update(float dt) {
        super.update(dt);

        // Move player
        var moveDirection = getUserInput().mul(moveSpeed)
                .rotate(transform.getRotation()); // Align with object direction
        moveObject(moveDirection, moveMove, dt);
        rotateObject(-getUserInputValue() * turnSpeed, turnMode, dt);
    }

    @Override
    public Vec2 getUserInput() {
        return new Vec2(KeyInput.getAxis(xAxis), KeyInput.getAxis(yAxis));
    }

    @Override
    public float getUserInputValue() {
        return KeyInput.getAxis(turnAxis);
    }

    @Override
    protected boolean isBraking() {
        return KeyInput.keyDown(BRAKE_KEY);
    }

}
