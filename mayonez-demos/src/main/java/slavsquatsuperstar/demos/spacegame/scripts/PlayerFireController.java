package slavsquatsuperstar.demos.spacegame.scripts;

import mayonez.*;
import mayonez.input.*;
import mayonez.scripts.combat.*;
import slavsquatsuperstar.demos.spacegame.objects.ShipProjectiles;

/**
 * Allows the player's ship to fire different weapons.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerFireController extends FireProjectile {

    private int weaponChoice;

    public PlayerFireController(float cooldown) {
        super(cooldown);
    }

    @Override
    public void start() {
        weaponChoice = 1;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (KeyInput.keyPressed("1")) {
            weaponChoice = 1;
            setCooldown(0.2f);
        } else if (KeyInput.keyPressed("2")) {
            weaponChoice = 2;
            setCooldown(0.4f);
        }
    }

    @Override
    protected boolean readyToFire() {
        return MouseInput.buttonDown("left mouse");
    }

    @Override
    protected GameObject spawnProjectile() {
        if (weaponChoice == 1) {
            return ShipProjectiles.createLaser(gameObject);
        } else if (weaponChoice == 2) {
            return ShipProjectiles.createPlasma(gameObject);
        } else return null;
    }

}
