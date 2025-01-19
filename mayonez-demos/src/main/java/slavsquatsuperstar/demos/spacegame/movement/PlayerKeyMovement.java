package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.input.*;
import mayonez.math.*;
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
    private boolean autoBrake; // TODO add UI label

    public PlayerKeyMovement(float moveSpeed, float turnSpeed) {
        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;
    }

    @Override
    protected void start() {
        super.start();
        autoBrake = true;
    }

    @Override
    protected void update(float dt) {
        // Toggle auto-brake
        if (KeyInput.keyPressed("b")) {
            autoBrake = !autoBrake;
        }

        // Get move input (relative to ship)
        var moveInput = getUserInput().mul(moveSpeed);
        var turnInput = getUserInputValue() * turnSpeed;

        // Calculate brake amount
        var brakeDir = getBrakeDir(moveInput);
        var turnBrakeDir = getTurnBrakeDir(turnInput);

        // Move and brake
        moveObject(moveInput.rotate(transform.getRotation()), dt); // Align with object direction
        rotateObject(turnInput, dt);
        brake(brakeDir, turnBrakeDir);

        // Fire thrusters
        getThrustController().fireMoveThrusters(moveInput, brakeDir.rotate(-transform.getRotation()));
        getThrustController().fireTurnThrusters(turnInput, turnBrakeDir);
    }

    // Movement Script Overrides

    @Override
    public void moveObject(Vec2 amount, float dt) {
        rb.applyForce(amount);
    }

    @Override
    public void rotateObject(float amount, float dt) {
        rb.applyTorque(amount);
    }

    @Override
    protected void brake(Vec2 brakeDir, float angBrakeDir) {
        moveObject(brakeDir.mul(moveSpeed), 0f);
        rotateObject(angBrakeDir * turnSpeed, 0f);

        // Stop moving if too slow
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
        var shouldBrake = (KeyInput.keyDown(BRAKE_KEY) ||
                // Auto-brake if no move input
                (autoBrake && moveInput.equals(new Vec2()))) &&
                // Don't burn when turning very slow
                rb.getSpeed() > BRAKE_THRESHOLD_SPEED;

        if (!shouldBrake) return new Vec2();
        else {
            var brakeDir = rb.getVelocity().mul(-1f).unit();
            // Lower brake amount when moving
            if (!MathUtils.equals(moveInput.x, 0f)) brakeDir.x *= 0.5f;
            if (!MathUtils.equals(moveInput.y, 0f)) brakeDir.y *= 0.5f;
            return brakeDir;
        }
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
