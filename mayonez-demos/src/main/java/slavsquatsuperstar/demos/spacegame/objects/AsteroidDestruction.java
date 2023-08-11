package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.graphics.*;
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

    // Component References
    private final SpawnManager obstacleSpawner;
    private final Color color;

    public AsteroidDestruction(SpawnManager obstacleSpawner, Color color) {
        this.obstacleSpawner = obstacleSpawner;
        this.color = color;
    }

    @Override
    public void onDestroy() {
        if (obstacleSpawner != null) obstacleSpawner.markObjectDestroyed();
        // Average scale x and y
        var size = (transform.getScale().x + transform.getScale().y) * 0.5f;
//        createExplosion(size);
        spawnAsteroidFragments(size);
    }

    private void createExplosion(float asteroidSize) {
        getScene().addObject(ExplosionPrefabs.createPrefab(
                "Asteroid Explosion",
                new Transform(transform.getPosition(), Random.randomAngle(),
                        new Vec2(asteroidSize * 0.75f)),
                EXPLOSION_DURATION
        ));
    }

    private void spawnAsteroidFragments(float asteroidSize) {
        var fragmentCount = (int) Random.randomFloat(2, asteroidSize * 1.5f);
        for (var i = 0; i < fragmentCount; i++) {
            var angle = 360f / fragmentCount;
            var angleError = Random.randomFloat(-angle * 0.1f, angle * 0.1f);
            var speedMod = Random.randomFloat(0.5f, asteroidSize);
            // todo apply impulse
            // todo position offset
            var velocity = getRigidbody().getVelocity().mul(speedMod)
                    .rotate(angle + angleError);

            getScene().addObject(new AsteroidFragment(
                    "Asteroid Fragment", transform.getPosition(),
                    transform.getScale().div(fragmentCount),
                    velocity, color));
        }
    }

}
