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
    private final static float MOVE_DRAG = 0f;
    private final static float BRAKE_DRAG = 0.2f;

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
        if (braking) {
            rb.setDrag(BRAKE_DRAG).setAngDrag(BRAKE_DRAG);
        } else {
            rb.setDrag(MOVE_DRAG).setAngDrag(MOVE_DRAG);
        }

        // Calculate brake direction and fire thrusters
        thrustController.fireMoveThrusters(moveInput, getBrakeDir(braking));
        thrustController.fireTurnThrusters(turnInput, getAngBrakeDir(braking));
    }

    // Brake Helper Methods

    protected abstract boolean isBraking();

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
