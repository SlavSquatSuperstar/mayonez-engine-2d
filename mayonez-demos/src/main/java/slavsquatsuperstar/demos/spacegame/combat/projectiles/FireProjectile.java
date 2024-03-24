package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;

/**
 * Allows ships to fire damaging projectiles repeatedly and defines criteria for when
 * ships may fire.
 *
 * @author SlavSquatSuperstar
 */
public abstract class FireProjectile extends Script {

    @Override
    protected void update(float dt) {
        // Fire weapons
        updateCooldowns(dt);
        if (shouldFire()) {
            getScene().addObject(spawnProjectile());
            onFire();
        }
    }

    /**
     * Update weapon cooldowns.
     *
     * @param dt the time between the last frame
     */
    protected abstract void updateCooldowns(float dt);

    /**
     * Decide whether to fire the projectile if the weapon is reloaded and other
     * conditions are met
     *
     * @return if able and ready to fire
     */
    protected abstract boolean shouldFire();

    /**
     * Instantiate the projectile to be fired.
     *
     * @return the projectile game object
     */
    protected abstract GameObject spawnProjectile();

    /**
     * Update the weapon cooldown and other state variables after firing.
     */
    protected abstract void onFire();

}
