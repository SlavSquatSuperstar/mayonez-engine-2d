package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.assets.*;
import mayonez.assets.text.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.WeaponHardpoint;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.PlayerFireController;
import slavsquatsuperstar.demos.spacegame.movement.PlayerKeyMovement;

import java.util.*;

/**
 * A player-controlled spaceship.
 *
 * @author SlavSquatSuperstar
 */
// TODO flash spawn immunity
public class PlayerSpaceship extends Spaceship {

    // Assets
    private static final CSVFile HARDPOINTS_DATA;
    private static final List<WeaponHardpoint> HARDPOINTS;

    static {
        HARDPOINTS_DATA = Assets.getAsset(
                "assets/spacegame/data/shuttle_hardpoints.csv", CSVFile.class
        );
        if (HARDPOINTS_DATA == null) HARDPOINTS = Collections.emptyList();
        else HARDPOINTS = HARDPOINTS_DATA.readCSV().stream()
                .map(WeaponHardpoint::new).toList();

    }

    // Constants
    private static final float PLAYER_HEALTH = 8f;

    public PlayerSpaceship(String name, String spriteName) {
        super(name, new Vec2(), new SpaceshipProperties(spriteName, PLAYER_HEALTH, PLAYER_HEALTH * 0.5f));
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
        addComponent(new PlayerFireController(HARDPOINTS));

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
