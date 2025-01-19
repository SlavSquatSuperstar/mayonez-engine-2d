package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.PlayerFireController;
import slavsquatsuperstar.demos.spacegame.movement.PlayerKeyMovement;

/**
 * A player-controlled spaceship.
 *
 * @author SlavSquatSuperstar
 */
// TODO flash spawn immunity
public class PlayerSpaceship extends Spaceship {

    public PlayerSpaceship(String name, Vec2 position, SpaceshipProperties properties) {
        super(name, position, properties);
    }

    @Override
    protected void init() {
        super.init();
        getScene().getCamera().setSubject(this);

        // Movement
        var rb = new Rigidbody(1f);
        addComponent(rb);
        addComponent(new PlayerKeyMovement(12f, 3f));
//        addComponent(new ClickToMove(10f, MoveMode.VELOCITY, true));

        // Weapons
        addComponent(new PlayerFireController(properties.hardpoints()));

        addComponent(new Script() {
            @Override
            protected void update(float dt) {
                // Destroy the player (debug only)
                if (KeyInput.keyDown("backspace")) {
                    getComponent(Damageable.class).onObjectDamaged(100);
                }
            }
        });
    }

}
