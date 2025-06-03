package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.math.*;

/**
 * A base asteroid that spawns fragments when destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class BaseAsteroid extends Asteroid {

    public BaseAsteroid(String name, Vec2 position, AsteroidProperties properties) {
        super(name, position, properties);
    }

    @Override
    protected void init() {
        super.init();
        var startingHealth = properties.getHealth();
        addRigidbody(properties.radius())
                .setVelocity(transform.getUp().mul(Random.randomFloat(0f, 3f)));

        // Create more fragments
        addComponent(new AsteroidDestruction(startingHealth, properties));
        addCollider();
    }

}
