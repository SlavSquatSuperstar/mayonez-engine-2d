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

    private final Node source;
    private final ProjectileType type;

    public Projectile(Node source, ProjectileType type) {
        this.source = source;
        this.type = type;
    }

    @Override
    protected void init() {
        getParent().addChild(new DestroyAfterDuration(type.lifetime()));
    }

    @Override
    protected void start() {
        var rb = getSibling(Rigidbody.class);
        if (rb == null) {
            this.setEnabled(false);
            return;
        }

        // Set initial velocity
        var sourceRb = source.getChild(Rigidbody.class);
        if (sourceRb != null) rb.setVelocity(sourceRb.getVelocity());
        rb.addVelocity(getTransform().getUp().mul(type.speed()));
    }

    public void onImpactObject(CollisionEvent event) {
        if (event.other.getParent().equals(source)) return; // Don't collide with source
        if (!event.trigger || event.type != CollisionEventType.ENTER) return;

        // Get particle position
        var particleXf = getTransform().copy();
        var contacts = event.contacts;
        if (contacts.size() == 1) {
            particleXf.setPosition(contacts.getFirst());
        } else if (contacts.size() == 2) {
            particleXf.setPosition(contacts.get(0).midpoint(contacts.get(1)));
        }

        // Spawn particle
        getScene().addObject(ProjectilePrefabs.createImpactPrefab(type, particleXf, event.other));
        getParent().setDestroyed();
    }

    public float getDamage() {
        return type.damage();
    }

    public Node getSource() {
        return source;
    }

}
