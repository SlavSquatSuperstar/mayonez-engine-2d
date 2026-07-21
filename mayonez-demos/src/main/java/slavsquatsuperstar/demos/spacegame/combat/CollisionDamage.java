package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.*;
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

        var collider = getCollider();
        if (collider != null) {
            // On collision
            collider.addCollisionCallback(this::onObjectCollision);
        }
    }

    // TODO Should listen to collision
    public void onObjectCollision(CollisionEvent event) {
        if (event.trigger || event.type != CollisionEventType.ENTER) return;

        var object = event.other;
        if (object.hasLayer(SpaceGameLayer.SHIPS) || object.hasLayer(SpaceGameLayer.ASTEROIDS)) {
            var speed = event.velocity.len();
            if (speed > speedThreshold) {
                var damage = speed / speedThreshold * collisionDamage;
                damageable.onObjectDamaged(damage);
            }
        }
    }
}
