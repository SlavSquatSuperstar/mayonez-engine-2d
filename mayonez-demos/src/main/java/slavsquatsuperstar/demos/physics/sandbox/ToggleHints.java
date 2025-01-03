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

    private final TextLabel hintsText, spawnHints;
    private boolean hintsShown;

    ToggleHints(TextLabel hintsText, TextLabel spawnHints) {
        this.hintsText = hintsText;
        this.spawnHints = spawnHints;
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
        hintsText.setMessage(hintsShown ? "Hide Controls (H)" : "Show Controls (H)");
        spawnHints.setEnabled(hintsShown);
    }

}
