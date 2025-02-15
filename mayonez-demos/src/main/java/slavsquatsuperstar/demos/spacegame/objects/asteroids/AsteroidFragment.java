package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.math.*;

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
        var radius = properties.radius();

        // Need to keep mass high enough to make sure fragments move
        addRigidbody(radius, startImpulse, startAngularImpulse);

        if (radius > AsteroidPrefabs.MIN_MEDIUM_RADIUS) {
            // Create more fragments
            addComponent(new AsteroidDestruction(startingHealth, properties));
            addCollider();
        } else {
            // Don't create any fragments
            var lifetime = Random.randomFloat(2f, 5f);
            addComponent(new DespawnAsteroid(lifetime, properties.color()));
        }
    }

}
