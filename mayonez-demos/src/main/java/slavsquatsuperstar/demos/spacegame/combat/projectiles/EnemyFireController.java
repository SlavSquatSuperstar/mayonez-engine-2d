package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.math.Random;
import mayonez.scripts.Timer;

import java.util.*;

/**
 * Allows enemy ships to fire different weapons.
 *
 * @author SlavSquatSuperstar
 */
public class EnemyFireController extends FireProjectile {

    private int weaponChoice, shotsLeft;
    private FiringState state;
    private final Timer fireTimer;
    private final Timer waitTimer; // Time between shoot bursts
    private final List<WeaponHardpoint> hardpoints;

    public EnemyFireController(List<WeaponHardpoint> hardpoints) {
        fireTimer = new Timer(0f);
        waitTimer = new Timer(0f);
        this.hardpoints = hardpoints;
    }

    @Override
    protected void start() {
        state = FiringState.DECIDING;
        fireTimer.reset();
        waitTimer.reset();
    }

    @Override
    protected void update(float dt) {
        fireTimer.countDown(dt);
        super.update(dt);
        updateFiringState();
        waitTimer.countDown(dt);
    }

    @Override
    protected boolean shouldFire() {
        return fireTimer.isReady()
        && state == FiringState.FIRING;
    }

    @Override
    protected void fireProjectiles() {
        for (var hardPoint : hardpoints) {
            spawnPrefab(weaponChoice, hardPoint.offset(), hardPoint.angle());
        }
        fireTimer.reset();
        shotsLeft -= 1;
    }

    // Helper Methods

    private void updateFiringState() {
        // Decide whether to keep firing or wait
        switch (state) {
            case FIRING -> {
                // Stop shooting if out of ammo
                if (shotsLeft <= 0) {
                    waitTimer.setDuration(Random.randomFloat(2f, 4f));
                    waitTimer.reset();
                    waitTimer.setPaused(false);
                    state = FiringState.WAITING;
                }
            }
            case WAITING -> {
                // Wait until shooting again
                if (waitTimer.isReady()) {
                    waitTimer.setPaused(true);
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
    }

    private void selectRandomWeapon() {
        weaponChoice = Random.randomInt(0, ProjectilePrefabs.count() - 1);
        var type = ProjectilePrefabs.getProjectileType(weaponChoice);
        if (type != null) {
            setCooldown(type.fireCooldown()); // Update fire cooldown
            shotsLeft = Random.randomInt(1, 10);
        }
    }

    private void setCooldown(float cooldown) {
        fireTimer.setDuration(cooldown);
    }

    // Helper Enum

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
