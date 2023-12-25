package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;

import java.util.*;

/**
 * Controls the visibility of thruster exhaust plumes based on a spaceship's acceleration.
 *
 * @author SlavSquatSuperstar
 */
public abstract class ThrustController extends Script {

    private final static float BRAKE_THRESHOLD_SPEED = 0.1f; // Don't fire when moving very slow
    private final static float MOVE_DRAG = 0f;
    private final static float BRAKE_DRAG = 0.2f;

    private final List<Thruster> thrusters;
    protected Rigidbody rb;

    public ThrustController(List<Thruster> thrusters) {
        this.thrusters = thrusters;
    }

    @Override
    public void start() {
        rb = getRigidbody();
        if (rb == null) setEnabled(false);
    }

    @Override
    public void update(float dt) {
        // Get user input
        var moveInputDir = getMoveInputDirection();
        var turnInputDir = getTurnInputDirection();

        // Calculate brake direction and slow motion
        Vec2 brakeDir;
        float angBrakeDir;
        // TODO update dynamically and rotate with ship
        if (isBraking()) {
            rb.setDrag(BRAKE_DRAG).setAngDrag(BRAKE_DRAG);
            brakeDir = rb.getVelocity().mul(-1f)
                    .rotate(-transform.getRotation());
            angBrakeDir = rb.getAngVelocity(); // Using right-handed coords here, so choose positive
        } else {
            rb.setDrag(MOVE_DRAG).setAngDrag(MOVE_DRAG);
            brakeDir = new Vec2();
            angBrakeDir = 0f;
        }

        // Fire thrusters and move
        activateMoveThrusters(moveInputDir, brakeDir);
        activateTurnThrusters(turnInputDir, angBrakeDir);
    }

    // Movement Methods

    /**\
     * Fire this spaceship's thrusters and move or brake in the specified direction.
     *
     * @param moveDir  the move direction, relative to the spaceship
     * @param brakeDir the brake direction, relative to the world
     */
    protected void activateMoveThrusters(Vec2 moveDir, Vec2 brakeDir) {
        for (var thr : thrusters) {
            var shouldBrake = thr.moveDir.faces(brakeDir) && rb.getSpeed() > BRAKE_THRESHOLD_SPEED;
            thr.setMoveEnabled(thr.moveDir.faces(moveDir) || shouldBrake);
        }
    }

    /**
     * Fire this spaceship's rotational thrusters and turn or brake in the specified
     * direction.
     *
     * @param turnDir     the turn rotation direction
     * @param angBrakeDir the brake rotation direction
     */
    protected void activateTurnThrusters(float turnDir, float angBrakeDir) {
        for (var thr : thrusters) {
            var shouldBrake = thr.turnDir.faces(angBrakeDir) && rb.getAngSpeed() > BRAKE_THRESHOLD_SPEED;
            thr.setTurnEnabled(thr.turnDir.faces(turnDir) || shouldBrake);
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
