package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;

/**
 * A fragment of a destroyed asteroid that either creates more fragments or despawns.
 *
 * @author SlavSquatSuperstar
 */
class AsteroidFragment extends Asteroid {

    private final Vec2 startImpulse;
    private final float startAngularImpulse;

    AsteroidFragment(
            String name, Vec2 position, AsteroidProperties properties,
            Vec2 startImpulse, float startAngularImpulse
    ) {
        super(name, position, properties);
        this.startImpulse = startImpulse;
        this.startAngularImpulse = startAngularImpulse;
    }

    @Override
    protected void init() {
        super.init();
        var startingHealth = properties.getHealth();
        addRigidbody(startingHealth, startImpulse, startAngularImpulse);
        addComponent(new Damageable(startingHealth));

        var radius = properties.radius();
        if (radius > AsteroidDestruction.MIN_SPAWN_FRAGMENTS_RADIUS) {
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
