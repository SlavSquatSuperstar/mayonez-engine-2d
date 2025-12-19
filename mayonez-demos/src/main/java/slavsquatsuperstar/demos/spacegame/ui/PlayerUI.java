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

    private static final String CONTROL_HINTS_MESSAGE = """
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
            """;

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
        var hpLabelPos = new Vec2(32, Mayonez.getScreenHeight() - 32);
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

        var style = TextStyle.DEFAULT_STYLE
                .setColor(Colors.WHITE)
                .setFontSize(20);

        // Auto-Brake Indicator
        var autoBrakeToolTip = new TextLabel(
                "Auto-Brake (B): On",
                new Vec2(20, Mayonez.getScreenHeight() - 125))
                .setStyle(style)
                .setAnchor(Anchor.LEFT);
        addComponent(autoBrakeToolTip);

        SpaceGameEvents.getPlayerEventSystem().subscribe(
                event -> {
                    if (event instanceof AutoBrakeToggleEvent e) {
                        if (e.isEnabled()) {
                            autoBrakeToolTip.setMessage("Auto-Brake (B): On");
                        } else {
                            autoBrakeToolTip.setMessage("Auto-Brake (B): Off");
                        }
                    }
                }
        );

        // Hints
        var hintsTooltip = new TextLabel(
                "Show Hints (H)",
                new Vec2(Mayonez.getScreenWidth() - 20, 15))
                .setStyle(style)
                .setAnchor(Anchor.BOTTOM_RIGHT);
        addComponent(hintsTooltip);

        var healthShieldHint = new TextLabel(
                "Health\n\nShield",
                new Vec2(270, Mayonez.getScreenHeight() - 32))
                .setStyle(style)
                .setAnchor(Anchor.TOP_LEFT);
        addComponent(healthShieldHint);

        var hotbarHints = new TextLabel(
                "(1)   (2)   (3)   (4)", new Vec2(15, 60))
                .setStyle(style)
                .setAnchor(Anchor.BOTTOM_LEFT);
        addComponent(hotbarHints);

        var controlText = new TextLabel(
                CONTROL_HINTS_MESSAGE,
                new Vec2(Mayonez.getScreenWidth() - 20,
                        Mayonez.getScreenHeight() - 20))
                .setStyle(style)
                .setAnchor(Anchor.TOP_RIGHT);
        addComponent(controlText);

        addComponent(new ToggleHints(hintsTooltip,
                new TextLabel[]{hotbarHints, controlText, healthShieldHint}));
    }

}
