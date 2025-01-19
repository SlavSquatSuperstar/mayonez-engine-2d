package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.math.Random;
import mayonez.math.*;
import mayonez.physics.dynamics.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.EnemyFireController;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;
import slavsquatsuperstar.demos.spacegame.movement.EnemyMovement;

import java.util.*;

/**
 * A computer-controlled enemy spaceship.
 *
 * @author SlavSquatSuperstar
 */
public class EnemySpaceship extends Spaceship {

    public EnemySpaceship(String name, Vec2 position, SpaceshipProperties properties) {
        super(name, position, properties);
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

        // Weapons (Randomly Select Projectiles)
        var projectiles = new ArrayList<>(ProjectilePrefabs.PROJECTILE_TYPES);
        var toRemove = Random.randomInt(1, 3);
        for (var i = 0; i < toRemove; i++) {
            var randomIndex = Random.randomInt(0, projectiles.size() - 1);
            projectiles.remove(randomIndex);
        }
        addComponent(new EnemyFireController(properties.hardpoints(), projectiles));
    }

}
