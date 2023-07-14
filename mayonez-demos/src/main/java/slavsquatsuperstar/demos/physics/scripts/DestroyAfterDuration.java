package slavsquatsuperstar.demos.physics.scripts;

import mayonez.*;

/**
 * Remove this game object from the scene after some time as passed.
 *
 * @author SlavSquatSuperstar
 */
public class DestroyAfterDuration extends Script {

    private float lifetime;

    public DestroyAfterDuration(float lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    public void update(float dt) {
        lifetime -= dt;
        if (lifetime <= 0) gameObject.setDestroyed();
    }

}
