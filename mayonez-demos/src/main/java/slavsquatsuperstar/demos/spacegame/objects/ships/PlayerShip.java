package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.input.*;
import mayonez.physics.*;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.spacegame.combat.PlayerFireController;
import slavsquatsuperstar.demos.spacegame.movement.PlayerThrustController;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterPrefabs;
import slavsquatsuperstar.demos.spacegame.objects.SpawnManager;

/**
 * A player-controlled spaceship.
 *
 * @author SlavSquatSuperstar
 */
// TODO flashing spawn immunity
public class PlayerShip extends Spaceship {

    private static final float PLAYER_HEALTH = 8f;
    private static final KeyAxis PLAYER_VERT_MOVE_AXIS = new KeyAxis(Key.S, Key.W);
    private static final KeyAxis PLAYER_HORIZ_MOVE_AXIS = new KeyAxis(Key.Q, Key.E);
    private static final KeyAxis PLAYER_TURN_AXIS = new KeyAxis(Key.A, Key.D);

    public PlayerShip(String name, String spriteName, SpawnManager playerSpawner) {
        super(name, spriteName, PLAYER_HEALTH, playerSpawner);
    }

    @Override
    protected void init() {
        super.init();

        getScene().getCamera().setSubject(this).setFollowAngle(false);

        // Movement
        // TODO allow set controls in config
        addComponent(new Rigidbody(1f));
//        addComponent(new KeyMovement(10f, MoveMode.FORCE, "horizontal2", "vertical").setObjectAligned(true));
        addComponent(new KeyMovement(10f, MoveMode.FORCE, PLAYER_HORIZ_MOVE_AXIS, PLAYER_VERT_MOVE_AXIS)
                .setObjectAligned(true));
        addComponent(new KeyRotation(180f, MoveMode.VELOCITY, "horizontal"));
//        addComponent(new ClickToMove(10f, MoveMode.VELOCITY, true));

        var thrusters = ThrusterPrefabs.addThrustersToObject(this);
        addComponent(new PlayerThrustController(thrusters));

        // Weapons
        addComponent(new PlayerFireController());
    }

}
