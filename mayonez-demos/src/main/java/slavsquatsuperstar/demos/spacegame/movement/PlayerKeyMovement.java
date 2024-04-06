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
public class PlayerKeyMovement extends MovementScript {

    // Config
    private static final InputAxis HORIZONTAL_MOVE_AXIS = SpaceGameConfig.getHorizontalMoveAxis();
    private static final InputAxis VERTICAL_MOVE_AXIS = SpaceGameConfig.getVerticalMoveAxis();
    private static final InputAxis TURN_AXIS = SpaceGameConfig.getTurnAxis();
    private static final Key BRAKE_KEY = SpaceGameConfig.getBreakKey();

    // Constants
    private final static float BRAKE_THRESHOLD_SPEED = 0.1f;
    private final static float TURN_BRAKE_THRESHOLD_SPEED = 5f;
    private final static float MOVE_DRAG = 0f;
    private final static float BRAKE_DRAG = 0.2f;

    // Movement Fields
    private final float moveSpeed, turnSpeed;
    private final MoveMode moveMove, turnMode;
    private final InputAxis xAxis, yAxis, turnAxis;

    // Script References
    private PlayerThrustController thrustController;

    public PlayerKeyMovement(
            float moveSpeed, MoveMode moveMove,
            float turnSpeed, MoveMode turnMode
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
    protected void start() {
        super.start();
        thrustController = gameObject.getComponent(PlayerThrustController.class);
    }

    @Override
    protected void update(float dt) {
        // Get user input
        var moveInput = getUserInput();
        var turnInput = -getUserInputValue();

        moveObject(moveInput.mul(moveSpeed), moveMove, dt);
        rotateObject(turnInput * turnSpeed, turnMode, dt);

        // Calculate brake direction and slow motion
        var braking = KeyInput.keyDown(BRAKE_KEY);
        Vec2 brakeDir = getBrakeDir(braking);
        float angBrakeDir = getAngBrakeDir(braking);
        if (braking) {
            rb.setDrag(BRAKE_DRAG).setAngDrag(BRAKE_DRAG);
        } else {
            rb.setDrag(MOVE_DRAG).setAngDrag(MOVE_DRAG);
        }

        // Fire thrusters
        thrustController.setMoveDirection(moveInput, brakeDir);
        thrustController.setTurnDirection(turnInput, angBrakeDir);
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

    // Helper Methods

    private Vec2 getBrakeDir(boolean braking) {
        // Don't burn when moving very slow
        if (braking && rb.getSpeed() > BRAKE_THRESHOLD_SPEED) {
            return rb.getVelocity().mul(-1f)
                    .rotate(-transform.getRotation()); // Orient with ship
        }
        return new Vec2();
    }

    private float getAngBrakeDir(boolean braking) {
        // Don't burn when turning very slow
        if (braking && rb.getAngSpeed() > TURN_BRAKE_THRESHOLD_SPEED) {
            return rb.getAngVelocity();
        }
        return 0f;
    }

}
