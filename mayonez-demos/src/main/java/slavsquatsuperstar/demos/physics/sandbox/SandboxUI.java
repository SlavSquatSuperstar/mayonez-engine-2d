package slavsquatsuperstar.demos.physics.sandbox;

import mayonez.*;
import mayonez.graphics.font.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;

public class SandboxUI extends GameObject {

    private static final int FONT_SIZE = 20;
    private static final String CONTROL_HINTS_MESSAGE = """
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
            """;

    public SandboxUI(String name) {
        super(name);
    }

    @Override
    protected void init() {

        // Toggle Hints
        TextLabel hintsTooltip = new TextLabel(
                "Show Controls (H)",
                new Vec2(Preferences.getScreenWidth() - 20,
                        Preferences.getScreenHeight() - 20)
        );
        hintsTooltip.setAnchor(Anchor.TOP_RIGHT);
        hintsTooltip.setFontSize(FONT_SIZE);
        addComponent(hintsTooltip);

        TextLabel controlHints = new TextLabel(
                CONTROL_HINTS_MESSAGE,
                new Vec2(Preferences.getScreenWidth() - 30,
                        Preferences.getScreenHeight() - 70)
        );
        controlHints.setAnchor(Anchor.TOP_RIGHT);
        controlHints.setFontSize(FONT_SIZE);
        addComponent(controlHints);
        addComponent(new ToggleHints(hintsTooltip, controlHints));

        // Gravity Text
        TextLabel gravityText = new TextLabel(
                "Gravity: On",
                new Vec2(Preferences.getScreenWidth() - 20, 20)
        );
        gravityText.setAnchor(Anchor.BOTTOM_RIGHT);
        gravityText.setFontSize(FONT_SIZE);
        addComponent(gravityText);
        addComponent(new ToggleGravity(gravityText));
    }

}
