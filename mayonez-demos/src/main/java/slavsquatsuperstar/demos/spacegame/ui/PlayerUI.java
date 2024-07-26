package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.textures.*;
import mayonez.input.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.DemosAssets;
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

    public PlayerUI(String name) {
        super(name);
    }

    @Override
    protected void init() {
        // TODO UI containers
        var uiSpacingX = 12f;
        var uiSpacingY = 12f;
        var labelSize = new Vec2(32, 32);
        var barSize = new Vec2(192, 32);

        // Player Health
        var hpLabelPosition = new Vec2(32, Preferences.getScreenHeight() - 32);
        var hpLabel = new ImageLabel(hpLabelPosition, labelSize, HEALTH_ICON_TEXTURE, LABEL_BACKGROUND_TEXTURE, LABEL_BORDER_TEXTURE);
        addComponent(hpLabel);

        var hpBarPosition = hpLabelPosition.add(new Vec2(labelSize.x * 0.5f + uiSpacingX + barSize.x * 0.5f, 0));
        var healthBar = new SliderBar(hpBarPosition, barSize, new Color(192, 0, 0), Colors.GREEN);
        addComponent(healthBar);

        // Player Shield
        var shLabelPosition = hpLabelPosition.sub(new Vec2(0, labelSize.y + uiSpacingY));
        var shLabel = new ImageLabel(shLabelPosition, labelSize, SHIELD_ICON_TEXTURE, LABEL_BACKGROUND_TEXTURE, LABEL_BORDER_TEXTURE);
        addComponent(shLabel);

        var shBarPosition = shLabelPosition.add(new Vec2(labelSize.x * 0.5f + uiSpacingX + barSize.x * 0.5f, 0));
        var shieldBar = new SliderBar(shBarPosition, barSize, new Color(96, 96, 96), Colors.LIGHT_BLUE);
        addComponent(shieldBar);

        // Weapon Hotbar
        var wpHbPosition = new Vec2(32, 32);
        var wpHbSize = new Vec2(32, 32);
        var weaponHotbar = new WeaponHotbar(wpHbPosition, wpHbSize, ProjectilePrefabs.NUM_PROJECTILES);
        addComponent(weaponHotbar);

        addComponent(new PlayerUIController(healthBar, shieldBar, weaponHotbar));

        var font = DemosAssets.getFont();
        if (font == null) return;

        // Hints Text
        TextLabel hintsText;
        addComponent(hintsText = new UITextLabel(
                "Show Hints (H)", new Vec2(1110, 18),
                font, Colors.WHITE,
                16, 2
        ));

        TextLabel hotbarText;
        addComponent(hotbarText = new UITextLabel(
                "(1)   (2)   (3)", new Vec2(82, 68),
                font, Colors.WHITE,
                16, 2
        ));

        addComponent(new Script() {
            private boolean hintsShown;

            @Override
            protected void start() {
                toggleHints(false);
            }

            @Override
            protected void update(float dt) {
                if (KeyInput.keyPressed("h")) {
                    toggleHints(!hintsShown);
                }
                if (KeyInput.keyPressed("l")) {
                    hpLabel.setPosition(hpLabel.getPosition().add(new Vec2(10, -10)));
                    shLabel.setPosition(shLabel.getPosition().add(new Vec2(10, -10)));
                }
                if (KeyInput.keyPressed("m")) {
                    hpLabel.setSize(hpLabel.getSize().add(new Vec2(5)));
                    shLabel.setSize(shLabel.getSize().add(new Vec2(5)));
                }
            }

            private void toggleHints(boolean hintsShown) {
                this.hintsShown = hintsShown;
                hintsText.setMessage(hintsShown ? "Hide Hints (H)" : "Show Hints (H)");
                hotbarText.setEnabled(hintsShown);
            }
        });
    }

}
