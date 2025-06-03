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
    private static final Key BRAKE_KEY = SpaceGameConfig.getBrakeKey();

    // Movement Fields
    private static boolean lastAutoBrake = true;

    private final float moveThrust, turnThrust;
    // private final float maxMoveSpeed, maxTurnSpeed;
    private boolean autoBrake;

    public PlayerKeyMovement(float moveThrust, float turnThrust) {
        this.moveThrust = moveThrust;
        this.turnThrust = turnThrust;
//        this.maxMoveSpeed = moveThrust * 3f;
//        this.maxTurnSpeed = turnThrust * 60f;
    }

    @Override
    protected void start() {
        super.start();
        autoBrake = lastAutoBrake;
        SpaceGameEvents.getPlayerEventSystem()
                .broadcast(new AutoBrakeToggleEvent(autoBrake));
    }

    @Override
    protected void update(float dt) {
        // Toggle auto-brake
        if (KeyInput.keyPressed(SpaceGameConfig.getAutoBrakeKey())) {
            autoBrake = !autoBrake;
            SpaceGameEvents.getPlayerEventSystem()
                    .broadcast(new AutoBrakeToggleEvent(autoBrake));
        }

        // Get move input (relative to ship)
        var moveInput = getUserInput().mul(moveThrust);
        var turnInput = getUserInputValue() * turnThrust;

        // Calculate brake amount (relative to ship)
        var brakeDir = getBrakeDir(moveInput);
        var turnBrakeDir = getTurnBrakeDir(turnInput);

        // Check whether to zero out velocity
        // TODO Maybe compare against frame delta-v instead
        // Zero out velocity if braking and too slow
        // Due to float imprecision brake/local velocity is not always zero
        var localVelocity = rb.getVelocity().rotate(-transform.getRotation());
        var brakeMod = new Vec2(1f);
        if (shouldZeroVelocity(brakeDir.x, localVelocity.x, BRAKE_THRESHOLD_SPEED)) {
            brakeMod.x = 0f;
        }
        if (shouldZeroVelocity(brakeDir.y, localVelocity.y, BRAKE_THRESHOLD_SPEED)) {
            brakeMod.y = 0f;
        }
        var turnBrakeMod = 1f;
        if (shouldZeroVelocity(turnBrakeDir, rb.getAngVelocity(), TURN_BRAKE_THRESHOLD_SPEED)) {
            turnBrakeMod = 0f;
        }

        // Move (relative to world)
        var worldMoveInput = moveInput.rotate(transform.getRotation());
        moveObject(worldMoveInput, dt);
        rotateObject(turnInput, dt);

        // Brake (relative to world)
        // Don't brake if component too small
        if (Math.abs(brakeDir.x) < 1e-5) brakeDir.x = 0f;
        if (Math.abs(brakeDir.y) < 1e-5) brakeDir.y = 0f;
        var worldBrakeDir = brakeDir.mul(brakeMod).rotate(transform.getRotation());

        // Should either zero or thrust
        moveObject(worldBrakeDir.mul(moveThrust), 0f);
        rb.setVelocity(localVelocity.mul(brakeMod).rotate(transform.getRotation()));

        rotateObject(turnBrakeDir * turnBrakeMod * turnThrust, 0f);
        rb.setAngVelocity(rb.getAngVelocity() * turnBrakeMod);

//        // Enforce max speed
//        if (rb.getSpeed() > maxMoveSpeed) {
//            moveObject(rb.getVelocity().unit().mul(-moveThrust), dt);
//        }
//        if (rb.getAngSpeed() > maxTurnSpeed) {
//            rotateObject(Math.signum(rb.getAngVelocity()) * -turnThrust, dt);
//        }

        // Fire thrusters
        getThrustController().fireMoveThrusters(moveInput, brakeDir.mul(brakeMod));
        getThrustController().fireTurnThrusters(turnInput, turnBrakeDir * turnBrakeMod);
    }

    @Override
    protected void onDestroy() {
        lastAutoBrake = autoBrake;
    }

    // Brake Overrides

    @Override
    protected Vec2 getBrakeDir(Vec2 moveInput) {
        // Velocity relative to ship
        var localVelocity = rb.getVelocity().rotate(-transform.getRotation());
        var brakeDir = localVelocity.mul(-1f).unit();

        // Lower brake power when input down
        if (!shouldBrake(moveInput.x)) {
            brakeDir.x = 0f;
        } else if (!MathUtils.equals(moveInput.x, 0f)) {
            brakeDir.x *= 0.5f;
        }
        if (!shouldBrake(moveInput.y)) {
            brakeDir.y = 0f;
        } else if (!MathUtils.equals(moveInput.y, 0f)) {
            brakeDir.y *= 0.5f;
        }
        return brakeDir;
    }

    @Override
    protected float getTurnBrakeDir(float turnInput) {
        if (!shouldBrake(turnInput)) return 0f;

        var angBrakeDir = -Math.signum(rb.getAngVelocity());
        // Lower brake amount when input down
        if (!MathUtils.equals(turnInput, 0f)) angBrakeDir *= 0.5f;
        return angBrakeDir;
    }

    private boolean shouldBrake(float moveInputComp) {
        // Always brake if brake key down
        return (KeyInput.keyDown(BRAKE_KEY) ||
                // Auto-brake if no move input
                (autoBrake && MathUtils.equals(moveInputComp, 0f)));
    }

    private boolean shouldZeroVelocity(
            float brakeDirComp, float velocityComp, float thresholdSpeedComp
    ) {
//        return !MathUtils.equals(brakeDirComp, 0f) && Math.abs(velocityComp) < thresholdSpeedComp;
        return Math.abs(brakeDirComp) > 1e-5f && Math.abs(velocityComp) < thresholdSpeedComp;
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
