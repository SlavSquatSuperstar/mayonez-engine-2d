package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.font.*;
import mayonez.input.*;

/**
 * Toggles displaying control hints in the UI.
 *
 * @author SlavSquatSupertar
 */
public class ToggleHints extends Script {

    private final TextLabel hintsTooltip;
    private final TextLabel[] hintLabels;
    private boolean hintsShown;

    public ToggleHints(TextLabel hintsTooltip, TextLabel[] hintLabels) {
        this.hintsTooltip = hintsTooltip;
        this.hintLabels = hintLabels;
    }

    @Override
    protected void start() {
        toggleHints(false);
    }

    @Override
    protected void update(float dt) {
        if (KeyInput.keyPressed("h")) {
            toggleHints(!hintsShown);
        }
    }

    private void toggleHints(boolean hintsShown) {
        this.hintsShown = hintsShown;
        hintsTooltip.setMessage(hintsShown ? "Hide Hints (H)" : "Show Hints (H)");
        for (var text : hintLabels) text.setEnabled(hintsShown);
    }

}
