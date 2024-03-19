package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.input.*;
import slavsquatsuperstar.demos.spacegame.events.SpaceGameEvents;
import slavsquatsuperstar.demos.spacegame.events.WeaponCooldownUpdate;

/**
 * Allows the player's ship to fire different weapons.
 *
 * @author SlavSquatSuperstar
 */
// TODO click to target
// TODO save choice between deaths
// TODO timer for each weapon
public class PlayerFireController extends FireProjectile {

    private int weaponChoice;

    public PlayerFireController() {
        super();
    }

    @Override
    protected void start() {
        setWeaponChoice(0);
    }

    @Override
    protected void update(float dt) {
        super.update(dt);
        for (var projIdx = 0; projIdx < ProjectilePrefabs.count(); projIdx++) {
            if (KeyInput.keyPressed(String.valueOf(projIdx + 1))) {
                setWeaponChoice(projIdx);
            }
        }

        if (fireTimer.getValue() > -dt) { // Don't let the timer count too negative
            var timerRemainingPercent = fireTimer.getValue() / fireTimer.getDuration();
            SpaceGameEvents.getPlayerEventSystem().broadcast(new WeaponCooldownUpdate(timerRemainingPercent));
        }
    }

    @Override
    protected boolean shouldFire() {
        return MouseInput.buttonDown("left mouse");
    }

    @Override
    protected GameObject spawnProjectile() {
        return ProjectilePrefabs.createPrefab(weaponChoice, gameObject);
    }

    public int getWeaponChoice() {
        return weaponChoice;
    }

    private void setWeaponChoice(int i) {
        var type = ProjectilePrefabs.getProjectileType(i);
        if (type == null) return;

        weaponChoice = i;
        setCooldown(type.fireCooldown());
        fireTimer.setValue(0f);
    }

}
