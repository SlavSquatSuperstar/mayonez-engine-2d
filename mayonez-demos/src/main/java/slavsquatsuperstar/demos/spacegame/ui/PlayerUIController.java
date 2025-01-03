package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.event.*;
import slavsquatsuperstar.demos.spacegame.combat.ShieldedDamageable;
import slavsquatsuperstar.demos.spacegame.events.*;

/**
 * Updates GUI elements based on the player's actions and states.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerUIController extends Script implements EventListener<SpaceGameEvent> {

    // UI Components
    private final SliderBar healthBar, shieldBar;
    private final WeaponHotbar weaponHotbar;

    // Ship Components
    private ShieldedDamageable damageable;

    // Player Status
    private float respawnPercent;

    public PlayerUIController(SliderBar healthBar, SliderBar shieldBar, WeaponHotbar weaponHotbar) {
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
            healthBar.setSliderValue(healthPercent);

            // Update shield bar
            var shieldPercent = damageable.getShield() / damageable.getMaxShield();
            shieldBar.setSliderValue(shieldPercent);
        } else {
            // Recharge health bar
            healthBar.setSliderValue(respawnPercent);
        }
    }

    private void setPlayer(GameObject player) {
        if (player == null) {
            damageable = null;
        } else {
            damageable = player.getComponent(ShieldedDamageable.class);
        }
    }

    // Script Callbacks

    @Override
    public void onEvent(SpaceGameEvent event) {
        if (event instanceof PlayerSpawnedEvent e) {
            // Player has (re)spawned
            setPlayer(e.getPlayer());
        } else if (event instanceof PlayerDestroyedEvent) {
            // Player was destroyed
            setPlayer(null);
            weaponHotbar.setAllCooldownPercents(0f);
            // cooldown overlay doesn't vanish until explosion animation is finished
        } else if (event instanceof PlayerRespawnUpdate u) {
            // Update respawn percent
            respawnPercent = u.getRespawnPercent();
        } else if (event instanceof WeaponCooldownUpdate u) {
            // Update weapon cooldown
            weaponHotbar.setCooldownPercent(u.getWeaponIndex(), u.getCooldownPercent());
        } else if (event instanceof WeaponSelectedEvent e) {
            // Update weapon selection
            weaponHotbar.setSelection(e.getWeaponIndex());
        }
    }

    @Override
    protected void onDestroy() {
        SpaceGameEvents.getPlayerEventSystem().unsubscribe(this);
    }

}
