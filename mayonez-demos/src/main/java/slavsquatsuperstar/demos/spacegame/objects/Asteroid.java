package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.graphics.Color;
import mayonez.math.Random;
import slavsquatsuperstar.demos.spacegame.ZIndex;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;

/**
 * An asteroid in space that can be destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class Asteroid extends GameObject {

    private final SpawnManager obstacleSpawner;
    private final int startingHealth;
    private final Color color;

    public Asteroid(String name, SpawnManager obstacleSpawner) {
        super(name);
        setZIndex(ZIndex.ASTEROID);
        this.obstacleSpawner = obstacleSpawner;
        this.startingHealth = Random.randomInt(8, 12);

        var tint = Random.randomInt(96, 176);
        color = new Color(tint, tint, tint);
    }

    @Override
    protected void init() {
        transform.setPosition(getScene().getRandomPosition());
        transform.setRotation(Random.randomAngle());
        transform.setScale(Random.randomVector(2f, 4f, 2f, 4f));

        AsteroidPrefabs.addCollider(this);
        AsteroidPrefabs.addSprite(this, color);
        AsteroidPrefabs.setStartingVelocity(this, startingHealth, transform.getUp().mul(Random.randomFloat(0f, 3f)));

        addAsteroidDamageable();
    }

    private void addAsteroidDamageable() {
        addComponent(new Damageable(startingHealth) {
            @Override
            public void onDestroy() {
                if (obstacleSpawner != null) obstacleSpawner.markObjectDestroyed();
                if (Random.randomPercent(0.2f)) return; // don't spawn fragments

                var fragmentCount = Random.randomInt(2, 4);
                for (var i = 0; i < fragmentCount; i++) {
                    var angle = 360f / fragmentCount;
                    var velocity = getRigidbody().getVelocity().mul(Random.randomFloat(0.5f, 2f))
                            .rotate(angle + Random.randomFloat(-30f, 30f));

                    getScene().addObject(new AsteroidFragment(
                            "Asteroid Fragment", transform.getPosition(),
                            transform.getScale().div(fragmentCount),
                            velocity, color));
                }
            }
        });
    }

}
