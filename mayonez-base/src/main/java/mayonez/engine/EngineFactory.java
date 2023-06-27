package mayonez.engine;

import mayonez.*;

/**
 * A factory class that constructs {@link mayonez.engine.GameEngine} and
 * {@link mayonez.engine.Window} objects depending on the run configuration.
 *
 * @author SlavSquatSuperstar
 */
public final class EngineFactory {

    private EngineFactory() {
    }

    // Game Engine Methods

    /**
     * Creates a new {@link mayonez.engine.GameEngine} object with the given
     * engine type.
     *
     * @param useGL whether to use OpenGL instead of Java's AWT library
     * @return the game engine
     */
    public static GameEngine createGameEngine(boolean useGL) {
        return useGL ? new GLGameEngine() : new JGameEngine();
    }

    // Window Methods

    /**
     * Creates a new {@link mayonez.engine.Window} object with the given engine
     * type.
     *
     * @param useGL whether to use OpenGL instead of Java's AWT library
     * @return the game engine
     */
    static Window createWindow(boolean useGL) {
        var title = String.format("%s %s (%s)",
                (useGL ? "GL" : "AWT"),
                Preferences.getTitle(), Preferences.getVersion()
        );
        var width = Preferences.getScreenWidth();
        var height = Preferences.getScreenHeight();

        if (useGL) return new GLWindow(title, width, height);
        else return new JWindow(title, width, height);
    }

}
