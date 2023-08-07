package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.math.*;

/**
 * Allows enemy ships to fire different weapons.
 *
 * @author SlavSquatSuperstar
 */
public class EnemyFireController extends FireProjectile {

    private int weaponChoice;
    private boolean isFiring;
    private int shotsLeft;

    public EnemyFireController(float cooldown) {
        super(cooldown);
    }

    @Override
    public void start() {
        weaponChoice = Random.randomBoolean() ? 2 : 1;
        isFiring = false;
    }

    @Override
    protected boolean readyToFire() {
        if (isReloaded() && isFiring) {
            // Decide to stop shooting
            if (--shotsLeft > 0) isFiring = false;
        } else {
            // Decide to start shooting
            isFiring = Random.randomPercent(0.01f);
            if (isFiring) shotsLeft = Random.randomInt(1, 5);
        }
        return isFiring;
    }

    @Override
    protected GameObject spawnProjectile() {
        if (weaponChoice == 1) {
            return ProjectilePrefabs.createLaser(gameObject);
        } else if (weaponChoice == 2) {
            return ProjectilePrefabs.createPlasma(gameObject);
        } else return null;
    }

}
