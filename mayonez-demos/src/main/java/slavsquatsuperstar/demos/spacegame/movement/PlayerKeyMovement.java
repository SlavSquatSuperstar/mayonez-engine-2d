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
        // Zero out velocity if braking and too slow
        var brakeMod = new Vec2(1f);
        if (shouldZeroVelocity(brakeDir.x, rb.getVelocity().x, BRAKE_THRESHOLD_SPEED)) {
            brakeMod.x = 0f;
        }
        if (shouldZeroVelocity(brakeDir.y, rb.getVelocity().y, BRAKE_THRESHOLD_SPEED)) {
            brakeMod.y = 0f;
        }
        var turnBrakeMod = 1f;
        if (shouldZeroVelocity(turnBrakeDir, rb.getAngVelocity(), TURN_BRAKE_THRESHOLD_SPEED)) {
            turnBrakeMod = 0f;
        }

        // Move and brake (relative to world)
        var worldMoveInput = moveInput.rotate(transform.getRotation());
        var worldBrakeDir = brakeDir.rotate(transform.getRotation());

        moveObject(worldMoveInput, dt);
        rotateObject(turnInput, dt);

        // Should either zero or thrust
        moveObject(worldBrakeDir.mul(brakeMod).mul(moveThrust), 0f);
        rb.setVelocity(rb.getVelocity().mul(brakeMod));
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
        getThrustController().fireMoveThrusters(moveInput, brakeDir.mul(turnBrakeMod));
        getThrustController().fireTurnThrusters(turnInput, turnBrakeDir * turnBrakeMod);
    }

    @Override
    protected void onDestroy() {
        lastAutoBrake = autoBrake;
    }

    // Brake Overrides

    @Override
    protected void brake(Vec2 brakeDir, float angBrakeDir) {
        moveObject(brakeDir.mul(moveThrust), 0f);
        rotateObject(angBrakeDir * turnThrust, 0f);

        // Zero out velocity if braking and too slow
        // Should either zero or thrust
        if (shouldZeroVelocity(brakeDir.x, rb.getVelocity().x, BRAKE_THRESHOLD_SPEED)) {
            rb.getVelocity().x = 0f;
        }
        if (shouldZeroVelocity(brakeDir.y, rb.getVelocity().y, BRAKE_THRESHOLD_SPEED)) {
            rb.getVelocity().y = 0f;
        }
        if (shouldZeroVelocity(angBrakeDir, rb.getAngVelocity(), TURN_BRAKE_THRESHOLD_SPEED)) {
            rb.setAngVelocity(0f);
        }
    }

    @Override
    protected Vec2 getBrakeDir(Vec2 moveInput) {
        // Velocity relative to ship
        var localVelocity = rb.getVelocity().rotate(-transform.getRotation());
        var brakeDir = localVelocity.mul(-1f).unit();

        // Lower brake amount when input down
        if (!shouldBrake(moveInput.x, localVelocity.x, BRAKE_THRESHOLD_SPEED)) {
            brakeDir.x = 0f;
        } else if (!MathUtils.equals(moveInput.x, 0f)) {
            brakeDir.x *= 0.5f;
        }
        if (!shouldBrake(moveInput.y, localVelocity.y, BRAKE_THRESHOLD_SPEED)) {
            brakeDir.y = 0f;
        } else if (!MathUtils.equals(moveInput.y, 0f)) {
            brakeDir.y *= 0.5f;
        }
        return brakeDir;
    }

    @Override
    protected float getTurnBrakeDir(float turnInput) {
        if (!shouldBrake(turnInput, rb.getAngVelocity(), TURN_BRAKE_THRESHOLD_SPEED)) {
            return 0f;
        }
        var angBrakeDir = -Math.signum(rb.getAngVelocity());
        // Lower brake amount when input down
        if (!MathUtils.equals(turnInput, 0f)) angBrakeDir *= 0.5f;
        return angBrakeDir;
    }

    private boolean shouldBrake(
            float moveInputComp, float localVelocityComp, float thresholdSpeedComp
    ) {
        // Always brake if brake key down
        return (KeyInput.keyDown(BRAKE_KEY) ||
                // Auto-brake if no move input
                (autoBrake && MathUtils.equals(moveInputComp, 0f)));

//        return (KeyInput.keyDown(BRAKE_KEY) ||
//                // Auto-brake if no move input
//                (autoBrake && MathUtils.equals(moveInputComp, 0f))) &&
//                // Don't burn when moving very slow
//                Math.abs(localVelocityComp) > thresholdSpeedComp;
    }

    private boolean shouldZeroVelocity(
            float brakeDirComp, float velocityComp, float thresholdSpeedComp
    ) {
//        return Math.abs(velocityComp) < thresholdSpeedComp;
        return !MathUtils.equals(brakeDirComp, 0f) && Math.abs(velocityComp) < thresholdSpeedComp;
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
