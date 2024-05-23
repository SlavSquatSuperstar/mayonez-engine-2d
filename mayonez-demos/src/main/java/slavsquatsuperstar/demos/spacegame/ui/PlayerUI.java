package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;

/**
 * Displays the player's GUI elements.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerUI extends GameObject {

    private static final Texture HEALTH_LABEL_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/health_bar_icon.png");
    private static final Texture SHIELD_LABEL_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/shield_bar_icon.png");

    private static final Texture BACKGROUND_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_background.png");
    private static final Texture BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_border.png");

    public PlayerUI(String name) {
        super(name);
    }

    @Override
    protected void init() {
        var uiSpacingX = 12f;
        var uiSpacingY = 16f;
        var labelSize = new Vec2(32, 32);
        var barSize = new Vec2(192, 32);

        // Player Health
        var hpLabelPosition = new Vec2(25, 775);
        var healthBackground = new UISprite(hpLabelPosition, labelSize, BACKGROUND_TEXTURE);
        addComponent(healthBackground);

        var healthLabel = new UISprite(hpLabelPosition, labelSize, HEALTH_LABEL_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 1;
            }
        };
        addComponent(healthLabel);

        var healthBorder = new UISprite(hpLabelPosition, labelSize, BORDER_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 1;
            }
        };
        addComponent(healthBorder);

        var hpBarPosition = hpLabelPosition.add(new Vec2(labelSize.x * 0.5f + uiSpacingX + barSize.x * 0.5f, 0));
        var healthBar = new SliderBar(hpBarPosition, barSize, new Color(192, 0, 0), Colors.GREEN);
        addComponent(healthBar);

        // Player Shield
        var shLabelPosition = hpLabelPosition.sub(new Vec2(0, labelSize.y + uiSpacingY));
        var shieldBackground = new UISprite(shLabelPosition, labelSize, BACKGROUND_TEXTURE);
        addComponent(shieldBackground);

        var shieldLabel = new UISprite(shLabelPosition, labelSize, SHIELD_LABEL_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 1;
            }
        };
        addComponent(shieldLabel);

        var shieldBorder = new UISprite(shLabelPosition, labelSize, BORDER_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 1;
            }
        };
        addComponent(shieldBorder);

        var shBarPosition = hpBarPosition.sub(new Vec2(0, barSize.y + uiSpacingY));
        var shieldBar = new SliderBar(shBarPosition, barSize, new Color(96, 96, 96), Colors.LIGHT_BLUE);
        addComponent(shieldBar);

        // Weapon Hotbar
        var whPosition = new Vec2(32, 32);
        var whSize = new Vec2(32, 32);
        var weaponHotbar = new WeaponHotbar(whPosition, whSize, ProjectilePrefabs.NUM_PROJECTILES);
        addComponent(weaponHotbar);

        addComponent(new PlayerUIController(healthBar, shieldBar, weaponHotbar));
    }

}
