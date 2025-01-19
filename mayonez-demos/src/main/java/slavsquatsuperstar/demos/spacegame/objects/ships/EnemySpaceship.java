package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.math.*;
import mayonez.math.Random;
import mayonez.physics.dynamics.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.EnemyFireController;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.WeaponHardpoint;
import slavsquatsuperstar.demos.spacegame.movement.EnemyMovement;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterPrefabs;

import java.util.*;

/**
 * A computer-controlled enemy spaceship.
 *
 * @author SlavSquatSuperstar
 */
public class EnemySpaceship extends Spaceship {

    private static final float ENEMY_HEALTH = 5f;

    public EnemySpaceship(String name, Vec2 position, String spriteName) {
        super(name, position, new SpaceshipProperties(spriteName,
                ENEMY_HEALTH, 0f,
                ThrusterPrefabs.THRUSTER_PROPERTIES, HARDPOINTS));
    }

    @Override
    protected void init() {
        super.init();
        transform.setRotation(Random.randomAngle());

        // Movement
        Rigidbody rb;
        addComponent(rb = new Rigidbody(1f, 0.01f, 0.8f));
        rb.setVelocity(transform.getUp().mul(Random.randomFloat(2f, 15f)));
        addComponent(new EnemyMovement());

        // Weapons (Randomly Select Hardpoints)
        addComponent(new EnemyFireController(getHardpoints(properties.hardpoints())));
    }

    private static List<WeaponHardpoint> getHardpoints(List<WeaponHardpoint> hardpoints) {
        var rand = Random.randomInt(1, 10);
        if (rand < 6) return hardpoints.subList(0, 1);
        else if (rand < 9) return  hardpoints.subList(0, 2);
        else return hardpoints.subList(0, 3);
    }

}
