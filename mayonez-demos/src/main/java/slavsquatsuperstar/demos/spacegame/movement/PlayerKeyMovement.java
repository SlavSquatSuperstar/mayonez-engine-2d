package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.input.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameConfig;
import slavsquatsuperstar.demos.spacegame.events.AutoBrakeToggleEvent;
import slavsquatsuperstar.demos.spacegame.events.SpaceGameEvents;

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
    private boolean autoBrake;

    public PlayerKeyMovement(float moveSpeed, float turnSpeed) {
        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;
    }

    @Override
    protected void start() {
        super.start();
        autoBrake = false;
    }

    @Override
    protected void update(float dt) {
        // Toggle auto-brake
        if (KeyInput.keyPressed("b")) {
            autoBrake = !autoBrake;
            SpaceGameEvents.getPlayerEventSystem()
                    .broadcast(new AutoBrakeToggleEvent(autoBrake));
        }

        // Get move input (relative to ship)
        var moveInput = getUserInput().mul(moveSpeed);
        var turnInput = getUserInputValue() * turnSpeed;

        // Calculate brake amount (relative to ship)
        var brakeDir = getBrakeDir(moveInput);
        var turnBrakeDir = getTurnBrakeDir(turnInput);

        // Move and brake (relative to world)
        moveObject(moveInput.rotate(transform.getRotation()), dt);
        rotateObject(turnInput, dt);
        brake(brakeDir.rotate(transform.getRotation()), turnBrakeDir);

        // Fire thrusters
        getThrustController().fireMoveThrusters(moveInput, brakeDir);
        getThrustController().fireTurnThrusters(turnInput, turnBrakeDir);
    }

    @Override
    protected void brake(Vec2 brakeDir, float angBrakeDir) {
        moveObject(brakeDir.mul(moveSpeed), 0f);
        rotateObject(angBrakeDir * turnSpeed, 0f);

        // Zero out velocity if too slow
        if (Math.abs(rb.getVelocity().x) < BRAKE_THRESHOLD_SPEED) {
            rb.getVelocity().x = 0f;
        }
        if (Math.abs(rb.getVelocity().y) < BRAKE_THRESHOLD_SPEED) {
            rb.getVelocity().y = 0f;
        }
        if (rb.getAngSpeed() < TURN_BRAKE_THRESHOLD_SPEED) {
            rb.setAngVelocity(0f);
        }
    }

    @Override
    protected Vec2 getBrakeDir(Vec2 moveInput) {
        var localVelocity = rb.getVelocity().rotate(-transform.getRotation());

        // Auto-brake if no move input
        // Don't burn when turning very slow
        var shouldBrakeX = (KeyInput.keyDown(BRAKE_KEY) ||
                (autoBrake && MathUtils.equals(moveInput.x, 0f))) &&
                Math.abs(localVelocity.x) > BRAKE_THRESHOLD_SPEED;
        var shouldBrakeY = (KeyInput.keyDown(BRAKE_KEY) ||
                (autoBrake && MathUtils.equals(moveInput.y, 0f))) &&
                Math.abs(localVelocity.y) > BRAKE_THRESHOLD_SPEED;

        // Lower brake amount when input down
        var brakeDir = localVelocity.mul(-1f).unit();
        if (!shouldBrakeX) brakeDir.x = 0f;
        else if (!MathUtils.equals(moveInput.x, 0f)) brakeDir.x *= 0.5f;
        if (!shouldBrakeY) brakeDir.y = 0f;
        else if (!MathUtils.equals(moveInput.y, 0f)) brakeDir.y *= 0.5f;

        return brakeDir;
    }

    @Override
    protected float getTurnBrakeDir(float turnInput) {
        var shouldBrake = (KeyInput.keyDown(BRAKE_KEY) ||
                // Auto-brake if no turn input
                (autoBrake && MathUtils.equals(turnInput, 0f))) &&
                // Don't burn when turning very slow
                rb.getAngSpeed() > TURN_BRAKE_THRESHOLD_SPEED;

        if (!shouldBrake) return 0f;
        else {
            var angBrakeDir = -Math.signum(rb.getAngVelocity());
            // Lower brake amount when input down
            if (!MathUtils.equals(turnInput, 0f)) angBrakeDir *= 0.5f;
            return angBrakeDir;
        }
    }

    // Input Overrides

    @Override
    public Vec2 getUserInput() {
        return new Vec2(KeyInput.getAxis(HORIZONTAL_MOVE_AXIS), KeyInput.getAxis(VERTICAL_MOVE_AXIS));
    }

    @Override
    public float getUserInputValue() {
        return -KeyInput.getAxis(TURN_AXIS);
    }

}
