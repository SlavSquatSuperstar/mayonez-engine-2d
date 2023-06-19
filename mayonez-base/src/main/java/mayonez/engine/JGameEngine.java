package mayonez.engine;

import mayonez.*;
import mayonez.annotations.*;

/**
 * An instance of this game using Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
final class JGameEngine extends GameEngine {

    JGameEngine() {
        super(WindowFactory.createWindow(false));
    }

    // Game Engine Methods

    @Override
    public float getCurrentTimeSecs() {
        return Mayonez.getSeconds();
    }

    @Override
    public String toString() {
        return String.format("AWT Game (%s)", getRunningString());
    }

}
