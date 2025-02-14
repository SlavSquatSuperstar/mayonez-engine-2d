package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.graphics.textures.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;

/**
 * Makes an asteroid destructible and spawns fragments after it is destroyed.
 *
 * @author SlavSquatSuperstar
 */
class AsteroidDestruction extends Damageable {

    static final float MIN_SPAWN_FRAGMENTS_RADIUS = 1.5f;

    private final AsteroidProperties properties;

    AsteroidDestruction(float startingHealth, AsteroidProperties properties) {
        super(startingHealth);
        this.properties = properties;
    }

    @Override
    protected void onDestroy() {
        spawnAsteroidFragments();
    }

    private void spawnAsteroidFragments() {
        var radius = properties.radius();
        if (radius < MIN_SPAWN_FRAGMENTS_RADIUS) {
            // Prevent infinitely spawning fragments
            return;
        }

        var fragmentCount = MathUtils.clamp(
                (int) Random.randomFloat(radius * 0.5f, radius * 1.5f), 2, 4
        );
        var fragmentRadius = radius / fragmentCount;

        var angle = 360f / fragmentCount;
        var offsetAngle = transform.getRotation();

        for (var i = 0; i < fragmentCount; i++) {
            Texture fragmentTexture;
            if (fragmentRadius > MIN_SPAWN_FRAGMENTS_RADIUS) {
                fragmentTexture = Asteroid.getRandomLargeTexture();
            } else {
                fragmentTexture = Asteroid.getRandomSmallTexture();
            }

            var impulse = fragmentRadius * Random.randomFloat(3f, 6f);
            var angularImpulse = fragmentRadius * Random.randomFloat(-5f, 5f);
            offsetAngle += AsteroidProperties.getRandomError(angle, 0.5f);

            getScene().addObject(new AsteroidFragment(
                    "Asteroid Fragment",
                    transform.getPosition(),
                    new AsteroidProperties(fragmentRadius, fragmentTexture, properties.color()),
                    new Vec2(impulse, 0).rotate(offsetAngle),
                    angularImpulse
            ));
        }
    }

}
