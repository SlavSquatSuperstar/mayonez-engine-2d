package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.input.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameConfig;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.PlayerFireController;
import slavsquatsuperstar.demos.spacegame.movement.PlayerThrustController;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterPrefabs;
import slavsquatsuperstar.demos.spacegame.objects.SpawnManager;
import slavsquatsuperstar.demos.spacegame.ui.HealthBar;

/**
 * A player-controlled spaceship.
 *
 * @author SlavSquatSuperstar
 */
// TODO flashing spawn immunity
public class PlayerShip extends Spaceship {

    private static final float PLAYER_HEALTH = 8f;
    private static final InputAxis VERTICAL_MOVE_AXIS = SpaceGameConfig.getVerticalMoveAxis();
    private static final InputAxis HORIZONTAL_MOVE_AXIS = SpaceGameConfig.getHorizontalMoveAxis();
    private static final InputAxis TURN_AXIS = SpaceGameConfig.getTurnAxis();

    public PlayerShip(String name, String spriteName, SpawnManager playerSpawner) {
        super(name, spriteName, PLAYER_HEALTH, playerSpawner);
    }

    @Override
    protected void init() {
        super.init();

        getScene().getCamera().setSubject(this).setFollowAngle(false);

        // Movement
        addComponent(new Rigidbody(1f));
        addComponent(new KeyMovement(10f, MoveMode.FORCE, HORIZONTAL_MOVE_AXIS, VERTICAL_MOVE_AXIS)
                .setObjectAligned(true));
        addComponent(new KeyRotation(180f, MoveMode.VELOCITY, TURN_AXIS));
//        addComponent(new ClickToMove(10f, MoveMode.VELOCITY, true));

        var thrusters = ThrusterPrefabs.addThrustersToObject(this);
        addComponent(new PlayerThrustController(thrusters));

        // Weapons
        addComponent(new PlayerFireController());

        // UI
        addComponent(new Script() {
            private Damageable damageable;
            private HealthBar healthBar;

            @Override
            protected void start() {
                getScene().getCamera().setKeepInScene(true);
                damageable = getComponent(Damageable.class);
                if (damageable == null) setEnabled(false);

                // TODO pass UI in c'tor?
                var ui = getScene().getObject("Player UI");
                System.out.println("ui is " + ui);
                if (ui != null) healthBar = ui.getComponent(HealthBar.class);
                if (healthBar == null) setEnabled(false);
            }

            @Override
            protected void update(float dt) {
                var healthPercent = damageable.getHealth() / damageable.getMaxHealth();
                healthBar.setValue(healthPercent);
            }
        });
    }

}
