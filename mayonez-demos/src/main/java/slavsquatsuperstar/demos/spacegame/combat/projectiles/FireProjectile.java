package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.scripts.*;

/**
 * Allows ships to fire damaging projectiles repeatedly and defines criteria for when
 * ships may fire.
 *
 * @author SlavSquatSuperstar
 */
public abstract class FireProjectile extends Script {

    // TODO store in subclass
    protected final Timer fireTimer;

    public FireProjectile() {
        fireTimer = new Timer(0f);
    }

    @Override
    protected void start() {
        fireTimer.reset();
    }

    @Override
    protected void update(float dt) {
        fireTimer.countDown(dt);
        if (isReloaded() && shouldFire()) {
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
     * If additional conditions for firing the projectile, such as user input or AI,
     * are met.
     *
     * @return if ready to fire
     */
    protected abstract boolean shouldFire();

    /**
     * Instantiate the projectile to be fired.
     *
     * @return the projectile game object
     */
    protected abstract GameObject spawnProjectile();

    /**
     * Set the fire cooldown between each projectile.
     *
     * @param cooldown the pause in seconds
     */
    public void setCooldown(float cooldown) {
        fireTimer.setDuration(cooldown);
    }

}
