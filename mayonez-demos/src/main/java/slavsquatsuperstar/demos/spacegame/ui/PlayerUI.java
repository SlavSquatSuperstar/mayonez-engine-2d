package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;
import slavsquatsuperstar.demos.spacegame.events.AutoBrakeToggleEvent;
import slavsquatsuperstar.demos.spacegame.events.SpaceGameEvents;

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
        var weaponHotbar = new WeaponHotbar(wpHbPosition, wpHbSize, ProjectilePrefabs.PROJECTILE_TYPES);
        addComponent(weaponHotbar);

        addComponent(new PlayerUIController(hpSlider, shSlider, weaponHotbar));

        var font = Fonts.DEFAULT_FONT;
        var fontSize = 20;
        var lineSpacing = 1;

        // Auto-Brake Indicator
        var autoBrakeToolTip = new TextLabel(
                "Auto-Brake: On",
                new Vec2(20, Preferences.getScreenHeight() - 125),
                font, Colors.WHITE, fontSize, lineSpacing
        );
        autoBrakeToolTip.setAnchor(Anchor.LEFT);
        addComponent(autoBrakeToolTip);
        SpaceGameEvents.getPlayerEventSystem().subscribe(
                event -> {
                    if (event instanceof AutoBrakeToggleEvent e) {
                        if (e.isEnabled()) {
                            autoBrakeToolTip.setMessage("Auto-Brake: On");
                        } else {
                            autoBrakeToolTip.setMessage("Auto-Brake: Off");
                        }
                    }
                }
        );

        // Hints
        var hintsTooltip = new TextLabel(
                "Show Hints (H)",
                new Vec2(Preferences.getScreenWidth() - 80, 15),
                font, Colors.WHITE, fontSize, lineSpacing
        );
        addComponent(hintsTooltip);
        hintsTooltip.setAnchor(Anchor.RIGHT);

        var hpShHint = new TextLabel(
                "Health\n\nShield",
                new Vec2(295, Preferences.getScreenHeight() - 65),
                font, Colors.WHITE, fontSize, lineSpacing
        );
        addComponent(hpShHint);

        var hotbarHints = new TextLabel(
                "(1)   (2)   (3)   (4)", new Vec2(106, 65),
                font, Colors.WHITE, fontSize, lineSpacing
        );
        hintsTooltip.setAnchor(Anchor.LEFT);
        addComponent(hotbarHints);

        var controlText = new TextLabel(
                """
                        Controls:
                        
                        Movement
                        - (W) Forward
                        - (S) Backward
                        - (Q) Left
                        - (E) Right
                        - (A) Turn Left
                        - (D) Turn Right
                        - (Space) Brake
                        - (B) Auto-Brake
                        
                        Weapons
                        - (Mouse 1) Fire
                        - (1-4) Select
                        """,
                new Vec2(Preferences.getScreenWidth() - 105,
                        Preferences.getScreenHeight() - 170),
                font, Colors.WHITE, fontSize, lineSpacing
        );
        addComponent(controlText);
        hintsTooltip.setAnchor(Anchor.TOP_RIGHT);

        addComponent(new ToggleHints(hintsTooltip,
                new TextLabel[]{hotbarHints, controlText, hpShHint}));
    }

}
