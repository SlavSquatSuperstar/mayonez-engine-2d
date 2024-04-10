package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.event.*;
import slavsquatsuperstar.demos.spacegame.combat.ShieldedDamageable;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.PlayerFireController;
import slavsquatsuperstar.demos.spacegame.events.*;

/**
 * Updates GUI elements based on the player's actions and states.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerUIController extends Script implements EventListener<Event> {

    // UI Components
    private final HealthBar healthBar, shieldBar;
    private final WeaponHotbar weaponHotbar;

    // Ship Components
    private ShieldedDamageable damageable;
    private PlayerFireController fireController;

    // Player Status
    private float respawnPercent;

    public PlayerUIController(HealthBar healthBar, HealthBar shieldBar, WeaponHotbar weaponHotbar) {
        this.healthBar = healthBar;
        this.shieldBar = shieldBar;
        this.weaponHotbar = weaponHotbar;
    }

    @Override
    protected void start() {
        SpaceGameEvents.getPlayerEventSystem().subscribe(this);
        setPlayer(null);
        respawnPercent = 1f;

        if (healthBar == null || shieldBar == null || weaponHotbar == null) setEnabled(false);
    }

    @Override
    protected void update(float dt) {
        if (damageable != null) {
            // Update health bar
            var healthPercent = damageable.getHealth() / damageable.getMaxHealth();
            healthBar.setHealthPercent(healthPercent);

            // Update shield bar
            var shieldPercent = damageable.getShield() / damageable.getMaxShield();
            shieldBar.setHealthPercent(shieldPercent);
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
            damageable = player.getComponent(ShieldedDamageable.class);
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
            weaponHotbar.setAllCooldownPercents(0f);
            // cooldown overlay doesn't vanish until explosion animation is finished
        } else if (event instanceof PlayerRespawnUpdate u) {
            respawnPercent = u.getRespawnPercent();
        } else if (event instanceof WeaponCooldownUpdate u) {
            weaponHotbar.setCooldownPercent(u.getWeaponIndex(), u.getCooldownPercent());
        }
    }

    @Override
    protected void onDestroy() {
        SpaceGameEvents.getPlayerEventSystem().unsubscribe(this);
    }

}
