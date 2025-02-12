package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;

/**
 * A fragment of a destroyed asteroid that either crates more fragments or despawns.
 *
 * @author SlavSquatSuperstar
 */
public class AsteroidFragment extends Asteroid {

    private static final float MIN_SPAWN_FRAGMENTS_RADIUS = 1.25f;

    private final Vec2 startImpulse;

    public AsteroidFragment(String name, Vec2 position, AsteroidProperties properties, Vec2 startImpulse) {
        super(name, position, properties);
        this.startImpulse = startImpulse;
    }

    @Override
    protected void init() {
        super.init();
        var startingHealth = properties.getHealth();
        addRigidbody(startingHealth).applyImpulse(startImpulse);
        addComponent(new Damageable(startingHealth));

        var radius = properties.radius();
        if (radius > MIN_SPAWN_FRAGMENTS_RADIUS) {
            // Create more fragments
            addComponent(new AsteroidDestruction(startingHealth, properties));
            addCollider();
        } else {
            // Don't create any fragments
            addComponent(new DespawnAsteroid(
                    Math.max(0.5f, Random.randomFloat(radius * 3f, radius * 5f)), properties.color()
            ));
        }
    }

}
