package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.PlayerFireController;

/**
 * Manages the player's GUI elements.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerUIController extends Script {

    // Ship Components
    private Damageable damageable;
    private PlayerFireController fireController;

    // UI Components
    private HealthBar healthBar;
    private WeaponSelectPanel weaponSelect;

    @Override
    protected void start() {
        var ui = getScene().getObject("Player UI");
        if (ui == null) {
            setEnabled(false);
            return;
        }

        damageable = getGameObject().getComponent(Damageable.class);
        fireController = getGameObject().getComponent(PlayerFireController.class);
        if (damageable == null || fireController == null) setEnabled(false);

        // TODO pass UI in c'tor?
        healthBar = ui.getComponent(HealthBar.class);
        weaponSelect = ui.getComponent(WeaponSelectPanel.class);
        if (healthBar == null || weaponSelect == null) setEnabled(false);
    }

    @Override
    protected void update(float dt) {
        // Update health bar
        var healthPercent = damageable.getHealth() / damageable.getMaxHealth();
        healthBar.setValue(healthPercent);

        // Update weapons selection
        weaponSelect.setSelection(fireController.getWeaponChoice());
    }

}
