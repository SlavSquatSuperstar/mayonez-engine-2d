package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;

/**
 * Allows objects to take damage from collisions.
 *
 * @author SlavSquatSuperstar
 */
public class CollisionDamage extends Script {

    private static final float DEFAULT_SPEED_THRESHOLD = 8f;
    private static final float DEFAULT_COLLISION_DAMAGE = 1f;

    private Damageable damageable;
    private final float speedThreshold;
    private final float collisionDamage;

    public CollisionDamage() {
        this(DEFAULT_SPEED_THRESHOLD, DEFAULT_COLLISION_DAMAGE);
    }

    public CollisionDamage(float speedThreshold, float collisionDamage) {
        this.speedThreshold = speedThreshold;
        this.collisionDamage = collisionDamage;
    }

    @Override
    protected void start() {
        damageable = gameObject.getComponent(Damageable.class);
    }

    @Override
    protected void onCollisionEnter(GameObject other, Vec2 direction, Vec2 velocity) {
        if (other.hasLayer(SpaceGameLayer.SHIPS) || other.hasLayer(SpaceGameLayer.ASTEROIDS)) {
            var speed = velocity.len();
            if (speed > speedThreshold) {
                var damage = speed / speedThreshold * collisionDamage;
                damageable.onObjectDamaged(damage);
            }
        }
    }
}
