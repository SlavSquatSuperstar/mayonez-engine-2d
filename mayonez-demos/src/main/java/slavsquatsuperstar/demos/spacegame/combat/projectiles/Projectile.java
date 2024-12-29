package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.physics.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;

/**
 * Allows a {@link mayonez.GameObject} to be launched with an initial velocity from a source object, and
 * allows it to damage other objects with a {@link slavsquatsuperstar.demos.spacegame.combat.Damageable} component.
 */
public class Projectile extends Script {

    private final GameObject source;
    private final float damage, speed;
    private final float lifetime;

    public Projectile(GameObject source, float damage, float speed, float lifetime) {
        this.source = source;
        this.damage = damage;
        this.speed = speed;
        this.lifetime = lifetime;
    }

    @Override
    protected void init() {
        gameObject.addComponent(new DestroyAfterDuration(lifetime));
    }

    @Override
    protected void start() {
        var rb = getRigidbody();
        if (getCollider() == null || rb == null) {
            this.setEnabled(false);
            return;
        }

        // Set initial velocity
        var sourceRb = source.getComponent(Rigidbody.class);
        if (sourceRb != null) rb.setVelocity(sourceRb.getVelocity());
        rb.addVelocity(transform.getUp().mul(speed));

        getCollider().addCollisionCallback(event -> {
            // On trigger
            if (event.trigger && event.type == CollisionEventType.ENTER) {
                onImpactObject(event.other);
            }
        });
    }

    private void onImpactObject(GameObject object) {
        if (!object.equals(source)) gameObject.destroy(); // Don't collide with source
    }

    public float getDamage() {
        return damage;
    }

    public GameObject getSource() {
        return source;
    }

}
