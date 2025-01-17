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

    @Override
    protected void start() {
        super.start();
        brakesActive = false;
    }

    // TODO Move spaceship

    // Movement Script Other

    @Override
    public Vec2 getUserInput() {
        if (isBraking()) return new Vec2();
        else return rb.getVelocity().rotate(-transform.getRotation());
    }

    @Override
    public float getUserInputValue() {
        if (isBraking()) return 0;
        else return -rb.getAngVelocity();
    }

    @Override
    protected boolean isBraking() {
        if (!brakesActive) {
            // Start braking if too fast
            if (rb.getSpeed() > 20f || rb.getAngSpeed() > 270f) {
                brakesActive = true;
            }
        } else {
            // Start firing thrusters once slow enough
            if (rb.getSpeed() < 5f && rb.getAngSpeed() < 45f) {
                brakesActive = false;
            }
        }
        // Keep braking
        return brakesActive;
    }

    @Override
    protected void brake(Vec2 brakeDir, float angBrakeDir) {
    }

}
