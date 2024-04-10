package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;

/**
 * Displays the player's GUI elements.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerUI extends GameObject {

    public PlayerUI(String name) {
        super(name);
    }

    @Override
    protected void init() {
        // Player Health
        var hpPosition = new Vec2(105f, 775);
        var hpSize = new Vec2(192, 32);
        var healthBar = new SliderBar(hpPosition, hpSize, new Color(192, 0, 0), Colors.GREEN);
        addComponent(healthBar);

        // Player Shield
        var shPosition = hpPosition.sub(hpSize.mul(new Vec2(0f, 1.5f)));
        var shieldBar = new SliderBar(shPosition, hpSize, new Color(96, 96, 96), Colors.LIGHT_BLUE);
        addComponent(shieldBar);

        // Weapon Hotbar
        var whPosition = new Vec2(32, 32);
        var whSize = new Vec2(32, 32);
        var weaponHotbar = new WeaponHotbar(whPosition, whSize, ProjectilePrefabs.NUM_PROJECTILES);
        addComponent(weaponHotbar);

        addComponent(new PlayerUIController(healthBar, shieldBar, weaponHotbar));
    }

}
