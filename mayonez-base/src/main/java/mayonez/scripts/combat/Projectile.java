package mayonez.scripts.combat;

import mayonez.GameObject;
import mayonez.Logger;
import mayonez.Script;
import mayonez.scripts.Timer;

/**
 * Allows a {@link mayonez.GameObject} to be launched with an initial velocity from a source object, and
 * allows it to damage other objects with a {@link Damageable} component.
 */
public class Projectile extends Script {

    private final GameObject source;
    private final float damage, speed;
    private final Timer lifetime;

    public Projectile(GameObject source, float damage, float speed, float lifetime) {
        this.source = source;
        this.damage = damage;
        this.speed = speed;
        this.lifetime = new Timer(lifetime);
    }

    @Override
    public void init() {
        gameObject.addComponent(lifetime);
    }

    @Override
    public void start() {
        lifetime.reset();
        gameObject.addTag("Projectile");
        if (getCollider() == null) Logger.debug("%s needs a collider to function!", this);
        if (getRigidbody() == null) Logger.debug("%s needs a rigidbody to function!", this);
        else getRigidbody().addVelocity(transform.getUp().mul(speed));
    }

    @Override
    public void update(float dt) {
        if (lifetime.isReady()) gameObject.setDestroyed();
    }

    @Override
    public void onTriggerEnter(GameObject other) {
        // TODO tell physics to ignore collision
        if (other == source) return; // don't collide with source
        gameObject.setDestroyed();
//        if (other.hasTag("Damageable")) gameObject.setDestroyed();
    }

    public float getDamage() {
        return damage;
    }

    public GameObject getSource() {
        return source;
    }
}
