package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.math.*;

import java.util.*;

/**
 * Controls the visibility of thruster exhaust plumes based on a spaceship's movement.
 *
 * @author SlavSquatSuperstar
 */
public class ThrustController extends Script {

    // Movement Fields
    private final List<Thruster> thrusters;

    public ThrustController(List<Thruster> thrusters) {
        this.thrusters = thrusters;
    }

    // Thruster Methods

    /**
     * Fire this spaceship's thrusters to move toward the given direction.
     *
     * @param moveDir  the move direction
     * @param brakeDir the brake direction
     */
    public void fireMoveThrusters(Vec2 moveDir, Vec2 brakeDir) {
        for (var thr : thrusters) {
            thr.setMoveEnabled(thr.moveDir.faces(moveDir)
                    || thr.moveDir.faces(brakeDir));
        }
    }

    /**
     * Fire this spaceship's thrusters to turn toward the given direction.
     *
     * @param turnDir     the turn rotation direction
     * @param angBrakeDir the brake rotation direction
     */
    public void fireTurnThrusters(float turnDir, float angBrakeDir) {
        for (var thr : thrusters) {
            thr.setTurnEnabled(thr.turnDir.faces(turnDir)
                    || thr.turnDir.faces(angBrakeDir));
        }
    }

    // Callback Methods

    @Override
    protected void onEnable() {
        for (var thruster : thrusters) thruster.setEnabled(true);
    }

    @Override
    protected void onDisable() {
        for (var thruster : thrusters) thruster.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        for (var thruster : thrusters) thruster.getGameObject().destroy();
    }

}
