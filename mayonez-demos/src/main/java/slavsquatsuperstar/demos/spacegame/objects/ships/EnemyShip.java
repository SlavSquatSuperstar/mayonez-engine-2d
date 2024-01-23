package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.math.*;
import mayonez.physics.dynamics.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.EnemyFireController;
import slavsquatsuperstar.demos.spacegame.movement.EnemyThrustController;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterPrefabs;

/**
 * A computer-controlled enemy spaceship.
 *
 * @author SlavSquatSuperstar
 */
public class EnemyShip extends Spaceship {

    private static final float ENEMY_HEALTH = 5f;

    public EnemyShip(String name, String spriteName) {
        super(name, spriteName, ENEMY_HEALTH);
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
        rb.setVelocity(transform.getUp().mul(Random.randomFloat(2f, 15f)));

        var thrusters = ThrusterPrefabs.addThrustersToObject(this);
        addComponent(new EnemyThrustController(thrusters));

        // Weapons
        addComponent(new EnemyFireController());
    }

}
