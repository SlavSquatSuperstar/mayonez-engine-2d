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
    private WeaponHotbar weaponHotbar;

    // Ship Components
    private Damageable damageable;
    private PlayerFireController fireController;

    // Player Status
    private float respawnPercent;

    @Override
    protected void start() {
        SpaceGameEvents.getPlayerEventSystem().subscribe(this);
        setPlayer(null);
        respawnPercent = 1f;

        healthBar = gameObject.getComponent(HealthBar.class);
        weaponHotbar = gameObject.getComponent(WeaponHotbar.class);
        if (healthBar == null || weaponHotbar == null) setEnabled(false);
    }

    // TODO show fire cooldown
    @Override
    protected void update(float dt) {
        if (damageable != null) {
            // Update health bar
            var healthPercent = damageable.getHealth() / damageable.getMaxHealth();
            healthBar.setHealthPercent(healthPercent);
        } else {
            // Recharge health bar
            healthBar.setHealthPercent(respawnPercent);
        }

        // Update weapons selection
        if (fireController != null) {
            weaponHotbar.setSelection(fireController.getSelectedWeapon());
        }
    }

    private void setPlayer(GameObject player) {
        if (player == null) {
            damageable = null;
            fireController = null;
        } else {
            damageable = player.getComponent(Damageable.class);
            fireController = player.getComponent(PlayerFireController.class);
        }
    }

    // Script Callbacks

    @Override
    public void onEvent(Event event) {
        if (event instanceof PlayerSpawnedEvent e) {
            setPlayer(e.getPlayer());
        } else if (event instanceof PlayerDestroyedEvent) {
            setPlayer(null);
            weaponHotbar.setCooldownPercent(0f);
            // cooldown overlay is messed up when player is destroyed
            // find after explosion animation is finished
        } else if (event instanceof PlayerRespawnUpdate u) {
            respawnPercent = u.getRespawnPercent();
        } else if (event instanceof WeaponCooldownUpdate u) {
            weaponHotbar.setCooldownPercent(u.getRechargePercent());
        }
    }

    @Override
    protected void onDestroy() {
        SpaceGameEvents.getPlayerEventSystem().unsubscribe(this);
    }

}
