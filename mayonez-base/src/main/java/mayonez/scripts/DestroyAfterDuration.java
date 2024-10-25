package mayonez.scripts;

import mayonez.*;

/**
 * Removes this game object from the scene after some time as passed.
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
    protected void start() {
        lifetime = maxLifetime;
    }

    @Override
    protected void update(float dt) {
        lifetime -= dt;
        if (lifetime <= 0) gameObject.destroy();
    }

    /**
     * Get the remaining lifetime in seconds of this object.
     * @return the lifetime
     */
    public float getLifetime() {
        return lifetime;
    }

    /**
     * Get the total lifetime in seconds of this object.
     * @return the max lifetime
     */
    public float getMaxLifetime() {
        return maxLifetime;
    }

}
