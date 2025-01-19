package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.math.Random;
import mayonez.math.*;
import mayonez.physics.dynamics.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.EnemyFireController;
import slavsquatsuperstar.demos.spacegame.movement.EnemyMovement;

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

        // Weapons (Randomly Select Hardpoints)
        addComponent(new EnemyFireController(properties.hardpoints()));
    }

}
