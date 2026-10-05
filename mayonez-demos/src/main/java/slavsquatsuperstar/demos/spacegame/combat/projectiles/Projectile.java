package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.physics.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;


/**
 * Allows a {@link mayonez.Node} to be launched with an initial velocity from a source object, and
 * allows it to damage other objects with a
 * {@link slavsquatsuperstar.demos.spacegame.combat.Damageable} component.
 *
 * @author SlavSquatSuperstar
 */
public class Projectile extends Node {

    private final ProjectileType type;
    private final Node source;

    public Projectile(ProjectileType type, Node source) {
        this.type = type;
        this.source = source;
    }

    @Override
    protected void init() {
        setName(type.name() + " Projectile");
        addTag(SpaceGameScene.PROJECTILE_TAG);

        addChild(new DestroyAfterDuration(type.lifetime()));
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
        getScene().addNode(ProjectilePrefabs.createImpactPrefab(type, particleXf, event.other));
        setDestroyed();
    }

    public float getDamage() {
        return type.damage();
    }

    // TODO change to ignoreCollision
    public Node getSource() {
        return source;
    }

}
