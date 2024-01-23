package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.graphics.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;

/**
 * An asteroid in space that can be destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class Asteroid extends BaseAsteroid {

    public Asteroid(String name) {
        super(name, new Vec2(), getRandomProperties());
    }

    @Override
    protected void init() {
        super.init();

        var radius = properties.radius();
        var startingHealth = Math.round(radius * 3f);

        transform.setPosition(getScene().getRandomPosition());
        addRigidbody(startingHealth)
                .setVelocity(transform.getUp().mul(Random.randomFloat(0f, 3f)));

        addComponent(new Damageable(startingHealth));
        addComponent(new AsteroidDestruction(properties));
    }

    private static AsteroidProperties getRandomProperties() {
        var radius = Random.randomFloat(1f, 4f);
        var tint = Random.randomInt(96, 176);
        var color = new Color(tint, tint, tint);
        return new AsteroidProperties(radius, color, 0);
    }

}
