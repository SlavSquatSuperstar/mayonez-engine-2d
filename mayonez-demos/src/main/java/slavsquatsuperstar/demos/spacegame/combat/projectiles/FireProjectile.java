package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.math.*;

/**
 * Allows ships to fire damaging projectiles repeatedly and defines criteria for when
 * ships may fire.
 *
 * @author SlavSquatSuperstar
 */
public abstract class FireProjectile extends Script {

    @Override
    protected void update(float dt) {
        if (shouldFire()) fireProjectiles();
    }

    /**
     * Decide whether to fire the projectile if the weapon is reloaded and other
     * conditions are met
     *
     * @return if able and ready to fire
     */
    protected abstract boolean shouldFire();

    /**
     * Instantiate the projectile(s) to be fired and update the weapon cooldown
     * and other state variables after firing.
     */
    protected abstract void fireProjectiles();

    /**
     * Instantiates a projectile prefab object and spawns it in the world.
     *
     * @param type        the projectile type
     * @param offsetPos   the projectile spawn position in relation to the source
     * @param offsetAngle the projectile spawn angle in relation to the source
     */
    protected void spawnPrefab(ProjectileType type, Vec2 offsetPos, float offsetAngle) {
        getScene().addObject(ProjectilePrefabs.createProjectilePrefab(
                type, gameObject, offsetPos, offsetAngle
        ));
    }

}
