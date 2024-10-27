package mayonez.engine;

import mayonez.*;
import mayonez.graphics.*;

/**
 * An instance of this game using Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
final class JGameEngine extends GameEngine {

    JGameEngine(Window window) {
        super(window);
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
