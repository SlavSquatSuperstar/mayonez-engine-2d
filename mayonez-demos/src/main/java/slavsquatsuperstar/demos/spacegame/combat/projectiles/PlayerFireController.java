package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.input.*;
import mayonez.math.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.events.SpaceGameEvents;
import slavsquatsuperstar.demos.spacegame.events.WeaponCooldownUpdate;
import slavsquatsuperstar.demos.spacegame.events.WeaponSelectedEvent;

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
        updateCooldowns(dt);
        super.update(dt);
    }

    @Override
    protected boolean shouldFire() {
        return fireTimers[selectedWeapon].isReady()
                && MouseInput.buttonDown("left mouse");
    }

    @Override
    protected void fireProjectiles() {
        Vec2[] offsetPositions = {
                new Vec2(0, 0.4f),
                new Vec2(-0.2f, 0.2f),
                new Vec2(0.2f, 0.2f)
        };
        float offsetAngle = 0f;
        for (var pos : offsetPositions) {
            spawnPrefab(selectedWeapon, pos, offsetAngle);
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
            var type = ProjectilePrefabs.getProjectileType(i);
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
