package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.event.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.PlayerFireController;
import slavsquatsuperstar.demos.spacegame.events.*;

/**
 * Updates GUI elements based on the player's actions and states.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerUIController extends Script implements EventListener<Event> {

    // UI Components
    private HealthBar healthBar;
    private WeaponSelectPanel weaponSelect;

    // Ship Components
    private Damageable damageable;
    private PlayerFireController fireController;

    // Player Status
    private boolean playerAlive;
    private float rechargePercent;

    @Override
    protected void start() {
        SpaceGameEvents.getPlayerEventSystem().subscribe(this);
        setPlayer(null);
        rechargePercent = 1f;

        healthBar = gameObject.getComponent(HealthBar.class);
        weaponSelect = gameObject.getComponent(WeaponSelectPanel.class);
        if (healthBar == null || weaponSelect == null) setEnabled(false);
    }

    // TODO recharge health bar
    // TODO show fire cooldown
    @Override
    protected void update(float dt) {
        if (damageable != null) {
            // Update health bar
            var healthPercent = damageable.getHealth() / damageable.getMaxHealth();
            healthBar.setValue(healthPercent);
        } else {
            // Recharge health bar
            healthBar.setValue(rechargePercent);
        }

        // Update weapons selection
        if (fireController != null) {
            weaponSelect.setSelection(fireController.getWeaponChoice());
        }
    }

    public void setPlayer(GameObject player) {
        if (player == null) {
            damageable = null;
            fireController = null;
            playerAlive = false;
        } else {
            damageable = player.getComponent(Damageable.class);
            fireController = player.getComponent(PlayerFireController.class);
            playerAlive = true;
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof PlayerSpawnedEvent e) {
            setPlayer(e.getPlayer());
        } else if (event instanceof PlayerDestroyedEvent) {
            setPlayer(null);
        } else if (event instanceof PlayerRespawnUpdate u) {
            rechargePercent = u.getRespawnPercent();
        }
    }

    @Override
    protected void onDestroy() {
        SpaceGameEvents.getPlayerEventSystem().unsubscribe(this);
    }

}
