package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.input.*;

/**
 * Allows the player's ship to fire different weapons.
 *
 * @author SlavSquatSuperstar
 */
// TODO weapon spread
// TODO click to target
public class PlayerFireController extends FireProjectile {

    private int weaponChoice;

    public PlayerFireController() {
        super();
    }

    @Override
    public void start() {
        setWeaponChoice(0);
    }

    @Override
    public void update(float dt) {
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
        setCooldown(type.getFireCooldown());
    }

}
