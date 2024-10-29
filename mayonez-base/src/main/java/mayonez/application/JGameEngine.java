package mayonez.application;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.keyboard.*;
import mayonez.input.mouse.*;

/**
 * An instance of this game using Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
final class JGameEngine extends GameEngine {

    public JGameEngine(Window window, KeyManager keyInput, MouseManager mouse) {
        super(window, keyInput, mouse);
    }

    // Game Engine Methods

    @Override
    public float getCurrentTimeSecs() {
        return Time.getTotalProgramSeconds();
    }

    @Override
    public String toString() {
        return String.format("AWT Game (%s)", getRunningString());
    }

}
