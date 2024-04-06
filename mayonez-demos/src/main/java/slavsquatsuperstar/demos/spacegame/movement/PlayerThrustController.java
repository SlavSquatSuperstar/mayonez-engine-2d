package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.math.*;

import java.util.*;

/**
 * Controls the visibility of the player's thruster exhaust plumes.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerThrustController extends Script {

    // Movement Fields
    private final List<Thruster> thrusters;
    private Vec2 moveDir, brakeDir;
    private float turnDir, angBrakeDir;

    public PlayerThrustController(List<Thruster> thrusters) {
        this.thrusters = thrusters;
    }

    @Override
    protected void start() {
        moveDir = new Vec2();
        brakeDir = new Vec2();
        turnDir = 0f;
        angBrakeDir = 0f;
    }

    @Override
    protected void update(float dt) {
        // Fire thrusters
        activateMoveThrusters(moveDir, brakeDir);
        activateTurnThrusters(turnDir, angBrakeDir);
    }

    // Movement Methods

    /**
     * Set the directions this spaceship's move thrusters should fire.
     *
     * @param moveDir  the move direction, relative to the spaceship
     * @param brakeDir the brake direction, relative to the world
     */
    public void setMoveDirection(Vec2 moveDir, Vec2 brakeDir) {
        this.moveDir.set(moveDir);
        this.brakeDir.set(brakeDir);
    }

    /**
     * Set the directions this spaceship's turn thrusters should fire.
     *
     * @param turnDir     the turn direction
     * @param angBrakeDir the brake direction
     */
    public void setTurnDirection(float turnDir, float angBrakeDir) {
        this.turnDir = turnDir;
        this.angBrakeDir = angBrakeDir;
    }

    /**
     * Fire this spaceship's thrusters to move or brake in the specified direction.
     *
     * @param moveDir  the move direction
     * @param brakeDir the brake direction
     */
    public void activateMoveThrusters(Vec2 moveDir, Vec2 brakeDir) {
        for (var thr : thrusters) {
            thr.setMoveEnabled(thr.moveDir.faces(moveDir) || thr.moveDir.faces(brakeDir));
        }
    }

    /**
     * Fire this spaceship's rotational thrusters to turn or brake in the specified
     * direction.
     *
     * @param turnDir     the turn rotation direction
     * @param angBrakeDir the brake rotation direction
     */
    public void activateTurnThrusters(float turnDir, float angBrakeDir) {
        for (var thr : thrusters) {
            thr.setTurnEnabled(thr.turnDir.faces(turnDir) || thr.turnDir.faces(angBrakeDir));
        }
    }

    // Callback Methods

    @Override
    protected void onDisable() {
        for (var thruster : thrusters) {
            thruster.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        for (var thruster : thrusters) {
            thruster.getGameObject().destroy();
        }
    }

}
