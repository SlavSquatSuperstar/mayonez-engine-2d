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
    private final static float BRAKE_THRESHOLD_SPEED = 0.1f;
    private final static float TURN_BRAKE_THRESHOLD_SPEED = 5f;

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
        var turnInput = -getUserInputValue();

        // Slow spaceship motion
        var braking = isBraking();
        Vec2 brakeDir;
        float angBrakeDir;

        if (braking) {
            brakeDir = getBrakeDir();
            angBrakeDir = getAngBrakeDir();
            brake(brakeDir, angBrakeDir); // TODO lower brake when moving
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

    protected Vec2 getBrakeDir() {
        // Don't burn when moving very slow
        if (rb.getSpeed() > BRAKE_THRESHOLD_SPEED) {
            return rb.getVelocity().mul(-1f).unit();
        }
        return new Vec2();
    }

    protected float getAngBrakeDir() {
        // Don't burn when turning very slow
        if (rb.getAngSpeed() > TURN_BRAKE_THRESHOLD_SPEED) {
            return Math.signum(rb.getAngVelocity());
        }
        return 0f;
    }

}
