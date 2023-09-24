package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.math.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;

/**
 * A fragment of a destroyed asteroid.
 *
 * @author SlavSquatSuperstar
 */
public class AsteroidFragment extends BaseAsteroid {

    private final Vec2 offsetNormal;

    public AsteroidFragment(String name, Vec2 position, AsteroidProperties properties, Vec2 offsetDirection) {
        super(name, position, properties);
        this.offsetNormal = offsetDirection;
    }

    @Override
    protected void init() {
        super.init();

        var startingHealth = Math.round(properties.radius() * 3f);
        addRigidbody(startingHealth).applyImpulse(offsetNormal.mul(6f));

        addComponent(new Damageable(startingHealth)); 
        addComponent(new DestroyAfterDuration(Random.randomFloat(10, 15)));
    }

}
