package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.input.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.DemosAssets;
import slavsquatsuperstar.demos.font.TextLabel;
import slavsquatsuperstar.demos.font.UITextLabel;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;

/**
 * Displays the player's GUI elements.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerUI extends GameObject {

    private static final Texture HEALTH_ICON_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/health_bar_icon.png");
    private static final Texture SHIELD_ICON_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/shield_bar_icon.png");

    private static final Texture LABEL_BACKGROUND_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_background.png");
    private static final Texture LABEL_BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_border.png");

    private boolean hintsShown;

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
        var hpLabel = new ImageLabel(hpLabelPosition, labelSize, HEALTH_ICON_TEXTURE, LABEL_BACKGROUND_TEXTURE, LABEL_BORDER_TEXTURE);
        addComponent(hpLabel);

        var hpBarPosition = hpLabelPosition.add(new Vec2(labelSize.x * 0.5f + uiSpacingX + barSize.x * 0.5f, 0));
        var healthBar = new SliderBar(hpBarPosition, barSize, new Color(192, 0, 0), Colors.GREEN);
        addComponent(healthBar);

        // Player Shield
        var shLabelPosition = hpLabelPosition.sub(new Vec2(0, labelSize.y + uiSpacingY));
        var shLabel = new ImageLabel(shLabelPosition, labelSize, SHIELD_ICON_TEXTURE, LABEL_BACKGROUND_TEXTURE, LABEL_BORDER_TEXTURE);
        addComponent(shLabel);

        var shBarPosition = hpBarPosition.sub(new Vec2(0, barSize.y + uiSpacingY));
        var shieldBar = new SliderBar(shBarPosition, barSize, new Color(96, 96, 96), Colors.LIGHT_BLUE);
        addComponent(shieldBar);

        // Weapon Hotbar
        var whPosition = new Vec2(32, 32);
        var whSize = new Vec2(32, 32);
        var weaponHotbar = new WeaponHotbar(whPosition, whSize, ProjectilePrefabs.NUM_PROJECTILES);
        addComponent(weaponHotbar);

        addComponent(new PlayerUIController(healthBar, shieldBar, weaponHotbar));

        // Hints Text
        TextLabel showHintsText;
        addComponent(showHintsText = new UITextLabel(
                "Show Hints (H)", new Vec2(1040, 25),
                DemosAssets.getFont(), Colors.WHITE,
                16, 2
        ));

        TextLabel hideHintsText;
        addComponent(hideHintsText = new UITextLabel(
                "Hide Hints (H)", new Vec2(1040, 25),
                DemosAssets.getFont(), Colors.WHITE,
                16, 2
        ));

        hintsShown = false;
        addComponent(new Script() {
            @Override
            protected void start() {
                hideHintsText.setEnabled(false);
            }

            @Override
            protected void update(float dt) {
                if (KeyInput.keyPressed("h")) {
                    hintsShown = !hintsShown;
                    showHintsText.setEnabled(!hintsShown);
                    hideHintsText.setEnabled(hintsShown);
                }
            }
        });
    }

}
