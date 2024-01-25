package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.input.*;
import mayonez.math.*;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameConfig;

import java.util.*;

/**
 * Controls the visibility of the player's thruster exhaust plumes.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerThrustController extends ThrustController {

    private static final Key BRAKE_KEY = SpaceGameConfig.getBreakKey();

    private MovementScript playerMovement;

    public PlayerThrustController(List<Thruster> thrusters) {
        super(thrusters);
    }

    @Override
    public void start() {
        super.start();
        playerMovement = gameObject.getComponent(MovementScript.class);
    }

    @Override
    protected Vec2 getMoveDirection() {
        if (playerMovement != null) return playerMovement.getUserInput();
        else return new Vec2();
    }

    @Override
    protected float getTurnDirection() {
        if (playerMovement != null) return playerMovement.getUserInputValue();
        else return 0f;
    }

    @Override
    protected boolean isBraking() {
        return KeyInput.keyDown(BRAKE_KEY);
    }

}
