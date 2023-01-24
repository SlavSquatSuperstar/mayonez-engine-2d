package mayonez.engine;

import mayonez.Mayonez;
import mayonez.Preferences;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;

/**
 * An instance of this game using Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JGame extends GameEngine {

    public JGame() {
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
    public float getCurrentTime() {
        return Mayonez.getSeconds();
    }

    @Override
    public String toString() {
        return String.format("AWT Game (%s)", getRunningString());
    }
}
