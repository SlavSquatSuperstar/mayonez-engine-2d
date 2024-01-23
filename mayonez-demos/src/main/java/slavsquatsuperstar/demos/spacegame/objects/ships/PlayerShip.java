package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameConfig;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.PlayerFireController;
import slavsquatsuperstar.demos.spacegame.movement.PlayerThrustController;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterPrefabs;

/**
 * A player-controlled spaceship.
 *
 * @author SlavSquatSuperstar
 */
// TODO flash spawn immunity
public class PlayerShip extends Spaceship {

    private static final float PLAYER_HEALTH = 8f;
    private static final InputAxis VERTICAL_MOVE_AXIS = SpaceGameConfig.getVerticalMoveAxis();
    private static final InputAxis HORIZONTAL_MOVE_AXIS = SpaceGameConfig.getHorizontalMoveAxis();
    private static final InputAxis TURN_AXIS = SpaceGameConfig.getTurnAxis();

    public PlayerShip(String name, String spriteName) {
        super(name, spriteName, PLAYER_HEALTH);
    }

    @Override
    protected void init() {
        super.init();
        getScene().getCamera().setSubject(this);

        // Movement
        addComponent(new Rigidbody(1f));
        addComponent(new KeyMovement(10f, MoveMode.FORCE, HORIZONTAL_MOVE_AXIS, VERTICAL_MOVE_AXIS) {
            @Override
            public Vec2 getUserInput() {
                return super.getUserInput()
                        .rotate(transform.getRotation()); // Align to object space
            }
        });
        addComponent(new KeyRotation(180f, MoveMode.VELOCITY, TURN_AXIS));
//        addComponent(new ClickToMove(10f, MoveMode.VELOCITY, true));

        var thrusters = ThrusterPrefabs.addThrustersToObject(this);
        addComponent(new PlayerThrustController(thrusters));

        // Weapons
        addComponent(new PlayerFireController());

        addComponent(new Script() {
            @Override
            protected void update(float dt) {
                // Destroy the player (debug only)
                if (KeyInput.keyDown("backspace")) {
                    getComponent(Damageable.class).damage(100);
                }
            }
        });
    }

}
