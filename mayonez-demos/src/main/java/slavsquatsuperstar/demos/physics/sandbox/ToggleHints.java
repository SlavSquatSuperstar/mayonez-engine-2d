package slavsquatsuperstar.demos.physics.sandbox;

import mayonez.*;
import mayonez.graphics.font.*;
import mayonez.input.*;

/**
 * Toggles displaying control hints in the UI.
 *
 * @author SlavSquatSupertar
 */
class ToggleHints extends Script {

    private final TextLabel hintsTooltip, controlHints;
    private boolean hintsShown;

    ToggleHints(TextLabel hintsTooltip, TextLabel controlHints) {
        this.hintsTooltip = hintsTooltip;
        this.controlHints = controlHints;
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
        hintsTooltip.setMessage(hintsShown ? "Hide Controls (H)" : "Show Controls (H)");
        controlHints.setEnabled(hintsShown);
    }

}
