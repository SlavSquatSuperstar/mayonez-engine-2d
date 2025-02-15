package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;

/**
 * Makes an asteroid destructible and spawns fragments after it is destroyed.
 *
 * @author SlavSquatSuperstar
 */
class AsteroidDestruction extends Damageable {

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
        if (radius < AsteroidPrefabs.MIN_FRAG_RADIUS) {
            // Prevent infinitely spawning fragments
            return;
        }

        var fragmentCount = Random.randomInt(2, 4);
        var fragmentRadius = radius / fragmentCount;

        var angle = 360f / fragmentCount;
        var offsetAngle = transform.getRotation();

        for (var i = 0; i < fragmentCount; i++) {
            getScene().addObject(createAsteroidFragment(fragmentRadius, offsetAngle));
            offsetAngle += AsteroidProperties.getRandomError(angle, 0.5f);
        }
    }

    private GameObject createAsteroidFragment(float fragmentRadius, float offsetAngle) {
        Texture fragmentTexture = AsteroidPrefabs.getAsteroidTexture(fragmentRadius);
        var impulse = Random.randomFloat(3f, 6f);
        var angularImpulse = Random.randomFloat(-5f, 5f);

        return new AsteroidFragment(
                "Asteroid Fragment",
                transform.getPosition(),
                new AsteroidProperties(fragmentRadius, fragmentTexture, properties.color()),
                new Vec2(impulse, 0).rotate(offsetAngle),
                angularImpulse
        );
    }

}
