package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.math.*;
import mayonez.physics.*;
import slavsquatsuperstar.demos.spacegame.combat.EnemyFireController;
import slavsquatsuperstar.demos.spacegame.movement.EnemyThrustController;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterPrefabs;

/**
 * An enemy spaceship.
 *
 * @author SlavSquatSuperstar
 */
public class EnemyShip extends Spaceship {

    private static final float ENEMY_HEALTH = 4f;

    public EnemyShip(String name, String spriteName, SpawnManager enemySpawner) {
        super(name, spriteName, ENEMY_HEALTH, enemySpawner);
        addTag("Enemy");
    }

    @Override
    protected void init() {
        super.init();

        // Position
        transform.setPosition(getScene().getRandomPosition());
        transform.setRotation(Random.randomAngle());

        // Movement
        Rigidbody rb;
        addComponent(rb = new Rigidbody(1f, 0.01f, 0.8f));
        rb.setVelocity(transform.getUp().mul(Random.randomFloat(2f, 10f)));

        var thrusters = ThrusterPrefabs.addThrustersToObject(this);
        addComponent(new EnemyThrustController(thrusters));

        // Weapons
        addComponent(new EnemyFireController(0.5f));

    }
}
