package slavsquatsuperstar.demos.spacegame.scripts.combat;

import mayonez.GameObject;
import mayonez.Script;
import mayonez.scripts.Timer;

/**
 * Allows objects to spawn projectiles repeatedly. This script is abstract and any instances must define
 * what projectiles to spawn and under what criteria.
 *
 * @author SlavSquatSuperstar
 */
public abstract class FireProjectile extends Script {

    private final Timer fireTimer;

    public FireProjectile(float cooldown) {
        fireTimer = new Timer(cooldown);
    }

    @Override
    public void init() {
        fireTimer.reset();
        gameObject.addComponent(fireTimer);
    }

    @Override
    public void update(float dt) {
        if (isReloaded() && readyToFire()) {
            getScene().addObject(spawnProjectile());
            fireTimer.reset();
        }
    }

    /**
     * Whether the fire cooldown has reached zero.
     *
     * @return if able to fire
     */
    protected boolean isReloaded() {
        return fireTimer.isReady();
    }

    /**
     * Additional conditions for firing the projectile, such as user input.
     *
     * @return if ready to fire
     */
    protected abstract boolean readyToFire();

    /**
     * Instantiate the projectile to be fired.
     *
     * @return the projectile game object
     */
    protected abstract GameObject spawnProjectile();

    public void setCooldown(float cooldown) {
        fireTimer.setDuration(cooldown);
        fireTimer.reset();
    }
}
