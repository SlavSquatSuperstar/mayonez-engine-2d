package slavsquatsuperstar.demos.physics.sandbox;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.DemosAssets;

public class SandboxUI extends GameObject {

    public SandboxUI(String name) {
        super(name);
    }

    @Override
    protected void init() {
        var font = DemosAssets.getFont();
        if (font == null) return;

        // Toggle Hints
        TextLabel hintsTooltip;
        addComponent(hintsTooltip = new UITextLabel(
                "Show Controls (H)",
                new Vec2(Preferences.getScreenWidth() - 100,
                        Preferences.getScreenHeight() - 24),
                font, Colors.BLACK,
                16, 2
        ));
        hintsTooltip.setAnchor(Anchor.RIGHT);

        TextLabel controlHints;
        addComponent(controlHints = new UITextLabel(
                """
                        Spawn Object
                        - (1) Box
                        - (2) Ball
                        - (3) Triangle
                        - (4) Polygon
                        
                        (Space)
                        Toggle Gravity
                        
                        (Left Mouse)
                        Drag Object
                        
                        (Right Mouse)
                        Throw Object
                        """,
                new Vec2(Preferences.getScreenWidth() - 90,
                        Preferences.getScreenHeight() - 180),
                font, Colors.BLACK,
                16, 2
        ));
        controlHints.setAnchor(Anchor.TOP_RIGHT);
        addComponent(new ToggleHints(hintsTooltip, controlHints));

        // Gravity Text
        TextLabel gravityText;
        addComponent(gravityText = new UITextLabel(
                "Gravity On",
                new Vec2(Preferences.getScreenWidth() - 80, 32),
                font, Colors.BLACK,
                16, 2
        ));
        gravityText.setAnchor(Anchor.RIGHT);
        addComponent(new ToggleGravity(gravityText));
    }

}
