package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.math.*;

import java.util.*;

/**
 * Controls the visibility of an enemy's thruster exhaust plumes.
 *
 * @author SlavSquatSuperstar
 */
public class EnemyThrustController extends ThrustController {

    private boolean brakesActive;

    public EnemyThrustController(List<Thruster> thrusters) {
        super(thrusters);
    }

    @Override
    public void start() {
        super.start();
        brakesActive = false;
    }

    @Override
    protected Vec2 getMoveInputDirection() {
        if (isBraking()) return new Vec2();
        else return rb.getVelocity().rotate(-transform.getRotation());
    }

    @Override
    protected float getTurnInputDirection() {
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

}
