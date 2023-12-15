package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.ExplosionPrefabs;

/**
 * Destroys an asteroid and spawns fragments after it is destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class AsteroidDestruction extends Script {

    // Constants
    private static final float EXPLOSION_DURATION = 0.5f;

    private final AsteroidProperties properties;

    public AsteroidDestruction(AsteroidProperties properties) {
        this.properties = properties;
    }

    @Override
    public void onDestroy() {
        // createExplosion();
        spawnAsteroidFragments();
    }

    private void createExplosion() {
        getScene().addObject(ExplosionPrefabs.createPrefab(
                "Asteroid Explosion",
                new Transform(transform.getPosition(), Random.randomAngle(),
                        new Vec2(properties.radius() * 0.75f)),
                EXPLOSION_DURATION
        ));
    }

    private void spawnAsteroidFragments() {
        var radius = properties.radius();
        var fragmentCount = (int) Random.randomFloat(2, radius * 1.5f);
        var fragmentRadius = radius / fragmentCount;

        var angle = 360f / fragmentCount;
        var offsetAngle = transform.getRotation();

        for (var i = 0; i < fragmentCount; i++) {
            offsetAngle += AsteroidProperties.addRandomError(angle, 0.3f);
            getScene().addObject(new AsteroidFragment(
                    "Asteroid Fragment",
                    transform.getPosition(),
                    properties.setRadius(fragmentRadius),
                    new Vec2(1, 0).rotate(offsetAngle)
            ));
        }
    }

}
