package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.*;

/**
 * Controls the visibility of thruster exhaust plumes based on a spaceship's acceleration.
 *
 * @author SlavSquatSuperstar
 */
public abstract class ThrustController extends Script {

    private final static float BRAKE_THRESHOLD = 0.1f; // Don't fire when moving very slow
    private final Thruster[] thrusters;
    protected Rigidbody rb;

    public ThrustController(Thruster... thrusters) {
        this.thrusters = thrusters;
    }

    @Override
    public void start() {
        rb = getRigidbody();
        if (rb == null) setEnabled(false);
    }

    @Override
    public void update(float dt) {
        // Slow movement and turning
        if (isBraking()) rb.setDrag(2f).setAngDrag(2f);
        else rb.setDrag(0f).setAngDrag(0f);

        // Calculate brake direction
        Vec2 brakeDir;
        float angBrakeDir;
        if (isBraking()) {
            brakeDir = rb.getVelocity().mul(-1f);
            angBrakeDir = rb.getAngVelocity(); // Using right-handed coords here, so choose positive
        } else {
            brakeDir = new Vec2();
            angBrakeDir = 0f;
        }

        // Move
        activateMoveThrusters(brakeDir);
        activateTurnThrusters(angBrakeDir);
    }

    // Movement Methods

    private void activateMoveThrusters(Vec2 brakeDir) {
        var moveInputDir = getMoveInputDirection();
        for (var thr : thrusters) {
            var shouldBrake = thr.moveDir.faces(brakeDir) && rb.getSpeed() > BRAKE_THRESHOLD;
            thr.setMoveEnabled(thr.moveDir.faces(moveInputDir) || shouldBrake);
        }
    }

    private void activateTurnThrusters(float angBrakeDir) {
        var turnInputDir = getTurnInputDirection();
        for (var thr : thrusters) {
            var shouldBrake = thr.turnDir.faces(angBrakeDir) && rb.getAngSpeed() > BRAKE_THRESHOLD;
            thr.setTurnEnabled(thr.turnDir.faces(turnInputDir) || shouldBrake);
        }
    }

    protected abstract boolean isBraking();

    protected abstract Vec2 getMoveInputDirection();

    protected abstract float getTurnInputDirection();

    // Callback Methods

    @Override
    public void onDisable() {
        for (var thruster : thrusters) {
            thruster.setEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        for (var thruster : thrusters) {
            thruster.getGameObject().destroy();
        }
    }
}
