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
    // TODO spawnable script: mark destroyed

    public Asteroid(String name, SpawnManager obstacleSpawner) {
        super(name);
        setZIndex(ZIndex.ASTEROID);
        this.obstacleSpawner = obstacleSpawner;
    }

    @Override
    protected void init() {
        var radius = Random.randomFloat(1f, 4f);
        var startingHealth = Math.round(radius * 3f);
        var tint = Random.randomInt(96, 176);
        var color = new Color(tint, tint, tint);
        var properties = new AsteroidProperties(radius, color, 0);

        transform.setPosition(getScene().getRandomPosition());
        transform.setRotation(Random.randomAngle());
        transform.setScale(properties.getScale());

        AsteroidPrefabs.addCollider(this);
        AsteroidPrefabs.addSprite(this, color);
        AsteroidPrefabs.addRigidbody(this, startingHealth)
                .setVelocity(transform.getUp().mul(Random.randomFloat(0f, 3f)));

        addComponent(new Damageable(startingHealth));
        addComponent(new AsteroidDestruction(obstacleSpawner, properties));
    }

}
