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
        // Player Status
        var playerStatus = new PlayerStatus(new Vec2(32, Mayonez.getScreenHeight() - 32),
                new Vec2(32, 32), new Vec2(192, 32), 12);
        addComponent(playerStatus);

        // Weapon Hotbar
        var weaponHotbar = new WeaponHotbar(new Vec2(32, 32), new Vec2(32, 32), 16,
                ProjectilePrefabs.PROJECTILE_TYPES);
        addComponent(weaponHotbar);

        addComponent(new PlayerUIController(playerStatus, weaponHotbar));

        // Tet Elements
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
        // TODO text in containers
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
