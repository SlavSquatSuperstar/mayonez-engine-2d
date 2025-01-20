package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.input.*;
import mayonez.scripts.Timer;
import slavsquatsuperstar.demos.spacegame.events.SpaceGameEvents;
import slavsquatsuperstar.demos.spacegame.events.WeaponCooldownUpdate;
import slavsquatsuperstar.demos.spacegame.events.WeaponSelectedEvent;

import java.util.*;

/**
 * Allows the player's ship to fire different weapons.
 *
 * @author SlavSquatSuperstar
 */
// TODO click to target
public class PlayerFireController extends FireProjectile {

    private static int savedChoice = 0; // Keep choice between deaths

    private final int numWeapons;
    private final Timer[] fireTimers;
    private final List<WeaponHardpoint> hardpoints;
    private final List<ProjectileType> projectiles;
    private int selectedWeapon;

    public PlayerFireController(List<WeaponHardpoint> hardpoints, List<ProjectileType> projectiles) {
        this.hardpoints = hardpoints;
        this.projectiles = projectiles;
        numWeapons = projectiles.size();
        fireTimers = initializeFireTimers();
    }

    @Override
    protected void start() {
        for (int i = 0; i < numWeapons; i++) fireTimers[i].setValue(0f); // Start off cooldown
        savedChoice = Math.min(savedChoice, numWeapons); // In case loadout changed
        setSelectedWeapon(savedChoice);
    }

    @Override
    protected void update(float dt) {
        // Select weapon from keyboard
        for (var projIdx = 0; projIdx < numWeapons; projIdx++) {
            if (KeyInput.keyPressed(String.valueOf(projIdx + 1))) {
                setSelectedWeapon(projIdx);
            }
        }
        updateCooldowns(dt);
        super.update(dt);
    }

    @Override
    protected void onDestroy() {
        savedChoice = selectedWeapon;
    }

    @Override
    protected boolean shouldFire() {
        return fireTimers[selectedWeapon].isReady()
                && MouseInput.buttonDown("left mouse");
    }

    @Override
    protected void fireProjectiles() {
        for (var hardPoint : hardpoints) {
            spawnPrefab(projectiles.get(selectedWeapon), hardPoint.offset(), hardPoint.angle());
        }
        fireTimers[selectedWeapon].reset();
    }

    // Helper Methods

    private void updateCooldowns(float dt) {
        for (var timer : fireTimers) timer.countDown(dt);

        // Send cooldown updates
        for (int i = 0; i < numWeapons; i++) {
            var fireTimer = fireTimers[i];
            if (fireTimer.getValue() > -dt) { // Don't send event if timer is too negative
                var timerRemainingPercent = fireTimer.getValue() / fireTimer.getDuration();
                SpaceGameEvents.getPlayerEventSystem()
                        .broadcast(new WeaponCooldownUpdate(i, timerRemainingPercent));
            }
        }
    }

    private Timer[] initializeFireTimers() {
        final Timer[] fireTimers = new Timer[numWeapons];
        for (int i = 0; i < numWeapons; i++) {
            var type = projectiles.get(i);
            var duration = (type != null) ? type.fireCooldown() : 0f;
            fireTimers[i] = new Timer(duration);
        }
        return fireTimers;
    }

    private void setSelectedWeapon(int index) {
        if (index == selectedWeapon) return;
        selectedWeapon = index;
        SpaceGameEvents.getPlayerEventSystem().broadcast(new WeaponSelectedEvent(index));
    }

}
