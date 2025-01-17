package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.math.*;
import mayonez.scripts.movement.*;

/**
 * Controls a spaceship's movement and activates its thrusters.
 *
 * @author SlavSquatSuperstar
 */
public abstract class SpaceshipMovement extends MovementScript {

    // Constants
    protected final static float BRAKE_THRESHOLD_SPEED = 0.1f;
    protected final static float TURN_BRAKE_THRESHOLD_SPEED = 2f;

    // Script References
    private ThrustController thrustController;

    @Override
    protected void start() {
        super.start();
        if (rb == null) setEnabled(false);
        thrustController = gameObject.getComponent(ThrustController.class);
    }

    @Override
    protected void update(float dt) {
        // Get move input
        var moveInput = getUserInput();
        var turnInput = getUserInputValue();

        // Determine brake amount
        Vec2 brakeDir;
        float angBrakeDir;
        if (isBraking()) {
            brakeDir = getBrakeDir();
            angBrakeDir = getAngBrakeDir();

            // Lower brake amount when moving
            if (!MathUtils.equals(moveInput.x, 0f)) brakeDir.x *= 0.5f;
            if (!MathUtils.equals(moveInput.y, 0f)) brakeDir.y *= 0.5f;
            if (!MathUtils.equals(turnInput, 0f)) angBrakeDir *= 0.5f;
            brake(brakeDir, angBrakeDir);
        } else {
            brakeDir = new Vec2();
            angBrakeDir = 0f;
        }

        // Calculate brake direction and fire thrusters
        thrustController.fireMoveThrusters(moveInput, brakeDir
                .rotate(-transform.getRotation())); // Orient with ship
        thrustController.fireTurnThrusters(turnInput, angBrakeDir);
    }

    // Brake Methods

    protected abstract boolean isBraking();

    protected abstract void brake(Vec2 brakeDir, float angBrakeDir);

    private Vec2 getBrakeDir() {
        // Don't burn when moving very slow
        if (rb.getSpeed() > BRAKE_THRESHOLD_SPEED) {
            return rb.getVelocity().mul(-1f).unit();
        }
        return new Vec2();
    }

    private float getAngBrakeDir() {
        // Don't burn when turning very slow
        if (rb.getAngSpeed() > TURN_BRAKE_THRESHOLD_SPEED) {
            return -Math.signum(rb.getAngVelocity());
        }
        return 0f;
    }

}
