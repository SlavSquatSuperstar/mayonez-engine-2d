package slavsquatsuperstar.demos.spacegame.combat;

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

    public PlayerFireController(float cooldown) {
        super(cooldown);
    }

    @Override
    public void start() {
        weaponChoice = 0;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        for (var i = 0; i < ProjectilePrefabs.count(); i++) {
            if (KeyInput.keyPressed(String.valueOf(i + 1))) {
                var type = ProjectilePrefabs.getProjectileType(i);
                if (type == null) continue;

                weaponChoice = i;
                setCooldown(type.getFireCooldown());
            }
        }
    }

    @Override
    protected boolean readyToFire() {
        return MouseInput.buttonDown("left mouse");
    }

    @Override
    protected GameObject spawnProjectile() {
        return ProjectilePrefabs.createPrefab(weaponChoice, gameObject);
    }

}
