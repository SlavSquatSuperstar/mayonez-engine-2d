package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.graphics.Color;
import mayonez.math.*;
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
        transform.setScale(Random.randomVector(new Vec2(2f), new Vec2(4f)));

        AsteroidPrefabs.addCollider(this);
        AsteroidPrefabs.addSprite(this, color);
        AsteroidPrefabs.addRigidbody(this, startingHealth)
                .setVelocity(transform.getUp().mul(Random.randomFloat(0f, 3f)));

        addComponent(new Damageable(startingHealth));
        addComponent(new AsteroidDestruction(obstacleSpawner, color));
    }

}
