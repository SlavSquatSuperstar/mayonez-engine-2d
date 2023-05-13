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
        super(new JWindow(String.format("%s %s (AWT)", Preferences.getTitle(), Preferences.getVersion()),
                Preferences.getScreenWidth(), Preferences.getScreenHeight()));
    }

//    public boolean isFullScreen() {
//        var env = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        var device = env.getDefaultScreenDevice();
//        return device.getFullScreenWindow() != null;
//    }

    // Game Loop Methods

    @Override
    public float getCurrentTimeSecs() {
        return Mayonez.getSeconds();
    }

    @Override
    public String toString() {
        return String.format("AWT Game (%s)", getRunningString());
    }
}
