package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.input.*;

/**
 * Allows the player's ship to fire different weapons.
 *
 * @author SlavSquatSuperstar
 */
// TODO click to target
// TODO save between deaths
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
    }

}
