package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.annotations.*;
import mayonez.math.*;

/**
 * Controls an enemy spaceship's movement.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
public class EnemyMovement extends SpaceshipMovement {

    // Movement Fields
    private boolean brakesActive;
    private boolean braking, turnBraking;

    @Override
    protected void start() {
        super.start();
        brakesActive = false;
        braking = false;
        turnBraking = false;
    }

    @Override
    protected void update(float dt) {
        // Update brake states
        if (!braking) {
            // Start braking if too fast
            if (rb.getSpeed() > 20f) {
                braking = true;
            }
        } else {
            // Start moving once slow enough
            if (rb.getSpeed() < 5f) {
                braking = false;
            }
        }
        if (!turnBraking) {
            if (rb.getAngSpeed() > 270f) {
                turnBraking = true;
            }
        } else {
            if (rb.getAngSpeed() < 45f) {
                turnBraking = false;
            }
        }

        // TODO move/slow spaceship
        getThrustController().fireMoveThrusters(getUserInput(), getBrakeDir(null));
        getThrustController().fireTurnThrusters(getUserInputValue(), getTurnBrakeDir(0f));
    }

    // Movement Script Overrides

    @Override
    public Vec2 getUserInput() {
        if (braking) return new Vec2();
        else return rb.getVelocity().rotate(-transform.getRotation());
    }

    @Override
    public float getUserInputValue() {
        if (turnBraking) return 0f;
        else return rb.getAngVelocity();
    }

    @Override
    protected Vec2 getBrakeDir(Vec2 moveInput) {
        if (braking) return rb.getVelocity().mul(-1f).unit();
        else return new Vec2();
    }

    @Override
    protected float getTurnBrakeDir(float turnInput) {
        if (turnBraking) return -Math.signum(rb.getAngVelocity());
        else return 0f;
    }

}
