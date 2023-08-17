package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.math.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.ZIndex;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;

/**
 * A fragment of a destroyed asteroid.
 *
 * @author SlavSquatSuperstar
 */
public class AsteroidFragment extends GameObject {

    private final Vec2 offsetNormal;
    private final AsteroidProperties properties;

    public AsteroidFragment(String name, Vec2 position, Vec2 offsetNormal, AsteroidProperties properties) {
        super(name, position);
        setZIndex(ZIndex.ASTEROID);
        this.offsetNormal = offsetNormal;
        this.properties = properties;
    }

    @Override
    protected void init() {
        var radius = properties.radius();
        var startingHealth = Math.round(radius * 3f);
        transform.setRotation(Random.randomAngle());
        transform.setScale(properties.getScale());

        AsteroidPrefabs.addCollider(this);
        AsteroidPrefabs.addSprite(this, properties.color());
        AsteroidPrefabs.addRigidbody(this, startingHealth)
                .applyImpulse(offsetNormal.mul(6f));

        addComponent(new Damageable(startingHealth));
        addComponent(new DestroyAfterDuration(Random.randomFloat(10, 15)));
    }

}
