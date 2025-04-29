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
        var fontSize = 20;
        var lineSpacing = 1;

        // Toggle Hints
        TextLabel hintsTooltip;
        addComponent(hintsTooltip = new UITextLabel(
                "Show Controls (H)",
                new Vec2(Preferences.getScreenWidth() - 100,
                        Preferences.getScreenHeight() - 30),
                font, Colors.BLACK, fontSize, lineSpacing
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
                        Preferences.getScreenHeight() - 190),
                font, Colors.BLACK, fontSize, lineSpacing
        ));
        controlHints.setAnchor(Anchor.TOP_RIGHT);
        addComponent(new ToggleHints(hintsTooltip, controlHints));

        // Gravity Text
        TextLabel gravityText;
        addComponent(gravityText = new UITextLabel(
                "Gravity: On",
                new Vec2(Preferences.getScreenWidth() - 80, 32),
                font, Colors.BLACK, fontSize, lineSpacing
        ));
        gravityText.setAnchor(Anchor.RIGHT);
        addComponent(new ToggleGravity(gravityText));
    }

}
