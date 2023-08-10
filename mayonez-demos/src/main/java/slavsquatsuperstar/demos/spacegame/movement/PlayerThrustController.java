package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.input.*;
import mayonez.math.*;
import mayonez.scripts.movement.*;

import java.util.*;

/**
 * Controls the visibility of the player's thruster exhaust plumes.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerThrustController extends ThrustController {

    public PlayerThrustController(List<Thruster> thrusters) {
        super(thrusters);
    }

    private KeyMovement keyMovement;
    private KeyRotation keyRotation;

    @Override
    public void start() {
        super.start();
        keyMovement = gameObject.getComponent(KeyMovement.class);
        keyRotation = gameObject.getComponent(KeyRotation.class);
    }

    @Override
    protected Vec2 getMoveInputDirection() {
        if (keyMovement != null) return keyMovement.getUserInput();
        else return new Vec2();
    }

    @Override
    protected float getTurnInputDirection() {
        if (keyRotation != null) return keyRotation.getUserInput().x;
        else return 0f;
    }

    @Override
    protected boolean isBraking() {
        return KeyInput.keyDown("space");
    }


}
