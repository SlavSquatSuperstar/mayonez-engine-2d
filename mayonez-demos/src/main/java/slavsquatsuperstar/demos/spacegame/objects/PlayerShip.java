package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.physics.*;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.spacegame.combat.PlayerFireController;
import slavsquatsuperstar.demos.spacegame.movement.PlayerThrustController;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterPrefabs;

/**
 * A player-controlled spaceship.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerShip extends Spaceship {

    private static final float PLAYER_HEALTH = 6f;

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
        addComponent(new KeyMovement(10f, MoveMode.FORCE, "horizontal2", "vertical").setObjectAligned(true));
        addComponent(new KeyRotation(180f, MoveMode.VELOCITY, "horizontal"));
//        addComponent(new ClickToMove(10f, MoveMode.VELOCITY, true));

        var thrusters = ThrusterPrefabs.addThrustersToObject(this);
        addComponent(new PlayerThrustController(thrusters));

        // Weapons
        addComponent(new PlayerFireController());
    }

}
