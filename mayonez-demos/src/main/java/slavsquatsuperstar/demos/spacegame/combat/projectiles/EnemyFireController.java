package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.math.*;
import mayonez.scripts.*;

/**
 * Allows enemy ships to fire different weapons.
 *
 * @author SlavSquatSuperstar
 */
public class EnemyFireController extends FireProjectile {

    private int weaponChoice, shotsLeft;
    private FiringState state;
    private final Timer waitTimer;

    public EnemyFireController() {
        super();
        waitTimer = new Timer(0);
    }

    @Override
    public void init() {
        super.init();
        gameObject.addComponent(waitTimer);
    }

    @Override
    protected void start() {
        state = FiringState.DECIDING;
    }

    @Override
    protected boolean shouldFire() {
        switch (state) {
            case FIRING -> {
                // Stop shooting if out of ammo
                if (shotsLeft <= 0) {
                    waitTimer.setDuration(Random.randomFloat(2f, 4f));
                    waitTimer.setStarted(true);
                    state = FiringState.WAITING;
                }
            }
            case WAITING -> {
                // Wait until shooting again
                if (waitTimer.isReady()) {
                    waitTimer.setStarted(false);
                    state = FiringState.DECIDING;
                }
            }
            case DECIDING -> {
                // Decide to start shooting
                if (Random.randomBoolean()) {
                    selectRandomWeapon();
                    state = FiringState.FIRING;
                }
            }
        }
        return state == FiringState.FIRING;
    }

    @Override
    protected GameObject spawnProjectile() {
        shotsLeft -= 1;
        return ProjectilePrefabs.createPrefab(weaponChoice, gameObject);
    }

    private void selectRandomWeapon() {
        weaponChoice = Random.randomInt(0, ProjectilePrefabs.count() - 1);
        var type = ProjectilePrefabs.getProjectileType(weaponChoice);
        if (type != null) {
            setCooldown(type.fireCooldown()); // Update fire cooldown
            shotsLeft = Random.randomInt(1, 10);
        }
    }

    /**
     * What an enemy spaceship's weapons is currently doing.
     *
     * @author SlavSquatSuperstar
     */
    private enum FiringState {
        FIRING,
        WAITING,
        DECIDING,
    }

}
