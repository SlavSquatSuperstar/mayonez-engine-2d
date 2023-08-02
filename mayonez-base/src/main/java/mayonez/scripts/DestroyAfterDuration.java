package mayonez.scripts;

import mayonez.*;

/**
 * Remove this game object from the scene after some time as passed.
 *
 * @author SlavSquatSuperstar
 */
public class DestroyAfterDuration extends Script {

    private final float maxLifetime;
    private float lifetime;

    public DestroyAfterDuration(float lifetime) {
        this.maxLifetime = lifetime;
        this.lifetime = maxLifetime;
    }

    @Override
    public void start() {
        lifetime = maxLifetime;
    }

    @Override
    public void update(float dt) {
        lifetime -= dt;
        if (lifetime <= 0) gameObject.setDestroyed();
    }

}
