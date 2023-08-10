package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.graphics.Color;
import mayonez.math.Random;
import mayonez.math.Vec2;
import mayonez.scripts.DestroyAfterDuration;
import slavsquatsuperstar.demos.spacegame.ZIndex;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;

/**
 * A fragment of a destroyed asteroid.
 *
 * @author SlavSquatSuperstar
 */
public class AsteroidFragment extends GameObject {

    private final Vec2 position, size, velocity;
    private final int startingHealth;
    private final Color color;

    public AsteroidFragment(String name, Vec2 position, Vec2 size, Vec2 velocity, Color color) {
        super(name);
        setZIndex(ZIndex.ASTEROID);
        this.position = position;
        this.size = size;
        this.velocity = velocity;
        startingHealth = 2;
        this.color = color;
    }

    @Override
    protected void init() {
        transform.setPosition(position);
        transform.setRotation(Random.randomAngle());
        transform.setScale(size);

        AsteroidPrefabs.addCollider(this);
        AsteroidPrefabs.addSprite(this, color);
        AsteroidPrefabs.setStartingVelocity(this, startingHealth, velocity);

        addComponent(new Damageable(startingHealth));
        addComponent(new DestroyAfterDuration(Random.randomFloat(10, 15)));
    }

}
