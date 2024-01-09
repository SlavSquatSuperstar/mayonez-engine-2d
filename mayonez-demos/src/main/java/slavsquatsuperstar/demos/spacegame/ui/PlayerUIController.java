package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.event.*;
import slavsquatsuperstar.demos.spacegame.combat.Damageable;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.PlayerFireController;
import slavsquatsuperstar.demos.spacegame.events.PlayerDestroyedEvent;
import slavsquatsuperstar.demos.spacegame.events.PlayerSpawnedEvent;
import slavsquatsuperstar.demos.spacegame.events.SpaceGameEvents;

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

    @Override
    protected void start() {
        SpaceGameEvents.getPlayerEventSystem().subscribe(this);
        setPlayer(null);
        healthBar = gameObject.getComponent(HealthBar.class);
        weaponSelect = gameObject.getComponent(WeaponSelectPanel.class);
    }

    // TODO recharge health bar
    // TODO show fire cooldown
    @Override
    protected void update(float dt) {
        // Update health bar
        if (healthBar != null && damageable != null) {
            var healthPercent = damageable.getHealth() / damageable.getMaxHealth();
            healthBar.setValue(healthPercent);
        }

        // Update weapons selection
        if (weaponSelect != null && fireController != null) {
            weaponSelect.setSelection(fireController.getWeaponChoice());
        }
    }

    public void setPlayer(GameObject player) {
        if (player == null) {
            damageable = null;
            fireController = null;
        } else {
            damageable = player.getComponent(Damageable.class);
            fireController = player.getComponent(PlayerFireController.class);
        }
    }

    @Override
    public void onEvent(Event event) {
        System.out.println("Event: " + event.getMessage());

        if (event instanceof PlayerSpawnedEvent e) {
           setPlayer(e.getPlayer());
        } else if (event instanceof PlayerDestroyedEvent) {
            setPlayer(null);
        }
    }

    @Override
    public void onDestroy() {
        SpaceGameEvents.getPlayerEventSystem().unsubscribe(this);
    }

}
