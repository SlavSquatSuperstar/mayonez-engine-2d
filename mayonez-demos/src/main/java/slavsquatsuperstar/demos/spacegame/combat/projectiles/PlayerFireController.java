package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.input.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.events.SpaceGameEvents;
import slavsquatsuperstar.demos.spacegame.events.WeaponCooldownUpdate;

/**
 * Allows the player's ship to fire different weapons.
 *
 * @author SlavSquatSuperstar
 */
// TODO click to target
// TODO save choice between deaths
public class PlayerFireController extends FireProjectile {

    private final int numWeapons;
    private final Timer[] fireTimers;
    private int selectedWeapon;

    public PlayerFireController() {
        super();
        numWeapons = ProjectilePrefabs.count();
        fireTimers = initializeFireTimers();
    }

    @Override
    protected void start() {
        for (int i = 0; i < numWeapons; i++) fireTimers[i].reset();
        setSelectedWeapon(0);
    }

    @Override
    protected void update(float dt) {
        // Select weapon from keyboard
        for (var projIdx = 0; projIdx < ProjectilePrefabs.count(); projIdx++) {
            if (KeyInput.keyPressed(String.valueOf(projIdx + 1))) {
                setSelectedWeapon(projIdx);
            }
        }

        super.update(dt);
    }

    @Override
    protected void updateCooldowns(float dt) {
        for (var timer : fireTimers) timer.countDown(dt);

        // Send cooldown updates
        for (int i = 0; i < numWeapons; i++) {
            var fireTimer = fireTimers[i];
            if (fireTimer.getValue() > -dt) { // Don't let the timer count too negative
                var timerRemainingPercent = fireTimer.getValue() / fireTimer.getDuration();
                SpaceGameEvents.getPlayerEventSystem().broadcast(new WeaponCooldownUpdate(i, timerRemainingPercent));
            }
        }
    }

    @Override
    protected boolean shouldFire() {
        return fireTimers[selectedWeapon].isReady()
                && MouseInput.buttonDown("left mouse");
    }

    @Override
    protected GameObject spawnProjectile() {
        return ProjectilePrefabs.createPrefab(selectedWeapon, gameObject);
    }

    @Override
    protected void onFire() {
        fireTimers[selectedWeapon].reset();
    }

    // Helper Methods

    public int getSelectedWeapon() {
        return selectedWeapon;
    }

    private void setSelectedWeapon(int i) {
        selectedWeapon = i;
    }

    private Timer[] initializeFireTimers() {
        final Timer[] fireTimers = new Timer[numWeapons];
        for (int i = 0; i < numWeapons; i++) {
            var type = ProjectilePrefabs.getProjectileType(i);
            var duration = (type != null) ? type.fireCooldown() : 0f;
            fireTimers[i] = new Timer(duration);
        }
        return fireTimers;
    }

}
