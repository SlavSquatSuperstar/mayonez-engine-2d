package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.ExplosionPrefabs;

/**
 * Makes an asteroid destructible and spawns fragments after it is destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class AsteroidDestruction extends Damageable {

    // Constants
    private static final float EXPLOSION_DURATION = 0.25f;
    private static final float MIN_SPAWN_FRAGMENTS_RADIUS = 1f;

    private final AsteroidProperties properties;

    public AsteroidDestruction(float startingHealth, AsteroidProperties properties) {
        super(startingHealth);
        this.properties = properties;
    }

    @Override
    protected void onDestroy() {
        createExplosion();
        spawnAsteroidFragments();
    }

    private void createExplosion() {
        getScene().addObject(ExplosionPrefabs.createAsteroidExplosionPrefab(
                "Asteroid Explosion",
                new Transform(
                        transform.getPosition(), Random.randomAngle(),
                        new Vec2(properties.radius() * 0.5f)
                ),
                EXPLOSION_DURATION
        ));
    }

    private void spawnAsteroidFragments() {
        var radius = properties.radius();
        if (radius < MIN_SPAWN_FRAGMENTS_RADIUS) return; // Don't spawn fragments if too small

        var fragmentCount = MathUtils.clamp(
                (int) Random.randomFloat(radius * 0.5f, radius * 1.5f), 2, 4
        );
        var fragmentRadius = radius / fragmentCount;

        var angle = 360f / fragmentCount;
        var offsetAngle = transform.getRotation();

        for (var i = 0; i < fragmentCount; i++) {
            offsetAngle += AsteroidProperties.getRandomError(angle, 0.5f);
            var impulse = Random.randomFloat(fragmentRadius * 3f, fragmentRadius * 6f);
            getScene().addObject(new AsteroidFragment(
                    "Asteroid Fragment",
                    transform.getPosition(),
                    properties.copyWithRadius(fragmentRadius),
                    new Vec2(impulse, 0).rotate(offsetAngle)
            ));
        }
    }

}
