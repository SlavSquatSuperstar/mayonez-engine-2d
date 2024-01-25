package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;

/**
 * Displays the player's GUI elements.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerUI extends GameObject {

    private final PlayerUIController playerUI;

    public PlayerUI(String name) {
        super(name);
        playerUI = new PlayerUIController();
    }

    @Override
    protected void init() {
        // Player Health
        var hpPosition = new Vec2(105f, 775);
        var hpSize = new Vec2(192, 32);
        addComponent(new HealthBar(hpPosition, hpSize));

        // Weapon Select
        var wsPosition = new Vec2(32, 32);
        var wsSize = new Vec2(32, 32);
        addComponent(new WeaponSelectPanel(wsPosition, wsSize, ProjectilePrefabs.NUM_PROJECTILES));

        addComponent(playerUI);
    }

    public PlayerUIController getPlayerUI() {
        return playerUI;
    }
}