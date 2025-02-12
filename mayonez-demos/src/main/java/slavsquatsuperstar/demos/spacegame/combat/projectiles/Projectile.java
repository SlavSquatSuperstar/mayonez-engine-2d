package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.physics.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;

/**
 * Allows a {@link mayonez.GameObject} to be launched with an initial velocity from a source object, and
 * allows it to damage other objects with a
 * {@link slavsquatsuperstar.demos.spacegame.combat.Damageable} component.
 *
 * @author SlavSquatSuperstar
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
                onImpactObject(event);
            }
        });
    }

    private void onImpactObject(CollisionEvent event) {
        if (event.other.equals(source)) return; // Don't collide with source

        // Get particle position
        var particleXf = transform.copy();
        var contacts = event.contacts;
        if (contacts.size() == 1) {
            particleXf.setPosition(contacts.getFirst());
        } else if (contacts.size() == 2) {
            particleXf.setPosition(contacts.get(0).midpoint(contacts.get(1)));
        }

        // Spawn particle
        getScene().addObject(ProjectilePrefabs.createImpactPrefab(type, particleXf, event.other));
        gameObject.destroy();
    }

    public float getDamage() {
        return type.damage();
    }

    public GameObject getSource() {
        return source;
    }

}
