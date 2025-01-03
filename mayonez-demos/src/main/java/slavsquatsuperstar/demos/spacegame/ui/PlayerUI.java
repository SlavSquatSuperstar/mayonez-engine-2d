package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
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
        var sliderSize = new Vec2(192, 32);

        // Player Health
        var hpLabelPos = new Vec2(32, Preferences.getScreenHeight() - 32);
        var hpLabel = new ImageLabel(hpLabelPos, labelSize,
                HEALTH_ICON_TEXTURE, LABEL_BACKGROUND_TEXTURE, LABEL_BORDER_TEXTURE);
        addComponent(hpLabel);

        var hpSliderPos = hpLabelPos.add(new Vec2(labelSize.x * 0.5f + uiSpacingX + sliderSize.x * 0.5f, 0));
        var hpSlider = new SliderBar(hpSliderPos, sliderSize, new Color(192, 0, 0), Colors.GREEN);
        addComponent(hpSlider);

        // Player Shield
        var shLabelPos = hpLabelPos.sub(new Vec2(0, labelSize.y + uiSpacingY));
        var shLabel = new ImageLabel(shLabelPos, labelSize,
                SHIELD_ICON_TEXTURE, LABEL_BACKGROUND_TEXTURE, LABEL_BORDER_TEXTURE);
        addComponent(shLabel);

        var shSliderPos = shLabelPos.add(new Vec2(labelSize.x * 0.5f + uiSpacingX + sliderSize.x * 0.5f, 0));
        var shSlider = new SliderBar(shSliderPos, sliderSize, new Color(96, 96, 96), Colors.LIGHT_BLUE);
        addComponent(shSlider);

        // Weapon Hotbar
        var wpHbPosition = new Vec2(32, 32);
        var wpHbSize = new Vec2(32, 32);
        var weaponHotbar = new WeaponHotbar(wpHbPosition, wpHbSize, ProjectilePrefabs.NUM_PROJECTILES);
        addComponent(weaponHotbar);

        addComponent(new PlayerUIController(hpSlider, shSlider, weaponHotbar));

        var font = DemosAssets.getFont();
        if (font == null) return;

        // Hints
        var hintsTooltip = new UITextLabel(
                "Show Hints (H)",
                new Vec2(Preferences.getScreenWidth() - 80, 20),
                font, Colors.WHITE,
                16, 2
        );
        addComponent(hintsTooltip);
        hintsTooltip.setAnchor(Anchor.RIGHT);

        var hpShHint = new UITextLabel(
                "Health\n\nShield",
                new Vec2(295, Preferences.getScreenHeight() - 55),
                font, Colors.WHITE,
                16, 2
        );
        addComponent(hpShHint);

        var hotbarHints = new UITextLabel(
                "(1)   (2)   (3)", new Vec2(82, 68),
                font, Colors.WHITE,
                16, 2
        );
        addComponent(hotbarHints);

        var controlText = new UITextLabel(
                """
                        Controls:
                        
                        Movement
                        - (W) Forward
                        - (S) Backward
                        - (Q) Left
                        - (E) Right
                        - (A) Turn Left
                        - (D) Turn Right
                        - (Space) Break
                        
                        Weapons
                        - (Mouse 1) Fire
                        - (1/2/3) Select
                        """,
                new Vec2(Preferences.getScreenWidth() - 105,
                        Preferences.getScreenHeight() - 155),
                font, Colors.WHITE,
                16, 2
        );
        addComponent(controlText);
        hintsTooltip.setAnchor(Anchor.TOP_RIGHT);

        addComponent(new ToggleHints(hintsTooltip,
                new TextLabel[]{hotbarHints, controlText, hpShHint}));
    }

}
