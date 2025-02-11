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
    private final ProjectileType type;

    public Projectile(GameObject source, ProjectileType type) {
        this.source = source;
        this.type = type;
    }

    @Override
    protected void init() {
        gameObject.addComponent(new DestroyAfterDuration(type.lifetime()));
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
        rb.addVelocity(transform.getUp().mul(type.speed()));

        getCollider().addCollisionCallback(event -> {
            // On trigger
            if (event.trigger && event.type == CollisionEventType.ENTER) {
                onImpactObject(event.other);
            }
        });
    }

    private void onImpactObject(GameObject object) {
        if (object.equals(source)) return; // Don't collide with source
        // Spawn particle
        getScene().addObject(ProjectilePrefabs.createImpactPrefab(type, transform.copy()));
        gameObject.destroy();
    }

    public float getDamage() {
        return type.damage();
    }

    public GameObject getSource() {
        return source;
    }

}
