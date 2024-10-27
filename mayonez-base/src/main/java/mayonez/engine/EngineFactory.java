package mayonez.engine;

import mayonez.*;
import mayonez.io.*;

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
     * @throws mayonez.engine.EngineInitException if the wrong thread is used on macOS
     */
    public static GameEngine createGameEngine(boolean useGL) throws EngineInitException {
        var window = createWindow(useGL);
        return useGL ? new GLGameEngine(window) : new JGameEngine(window);
    }

    // Window Methods

    /**
     * Creates a new {@link mayonez.engine.Window} object with the given engine
     * type.
     *
     * @param useGL whether to use OpenGL instead of Java's AWT library
     * @return the window
     * @throws mayonez.engine.EngineInitException if the wrong thread is used on macOS
     */
    private static Window createWindow(boolean useGL) throws EngineInitException {
        var title = String.format("%s (%s) %s",
                Preferences.getTitle(), (useGL ? "GL" : "AWT"), Preferences.getVersion()
        );
        var width = Preferences.getScreenWidth();
        var height = Preferences.getScreenHeight();

        // Check that correct thread is used on macOS
        if (useGL) {
            if (OperatingSystem.getCurrentOS() == OperatingSystem.MAC_OS
                    && !JVMHelper.isStartedOnFirstThread()) {
                Logger.error("GLFW must be initialized from the main thread on macOS");
                Logger.error("Make sure to run Java with the \"-XstartOnFirstThread\" VM argument");
                throw new EngineInitException("Aborting GLFW initialization due to main thread not used");
            }
            return new GLWindow(title, width, height);
        } else {
            // VM args must be checked before AWT classes are used
            if (OperatingSystem.getCurrentOS() == OperatingSystem.MAC_OS
                    && JVMHelper.isStartedOnFirstThread()) {
                Logger.error("AWT cannot be used from the main thread on macOS");
                Logger.error("Make sure to run Java without the \"-XstartOnFirstThread\" VM argument");
                throw new EngineInitException("Aborting AWT window creation due to main thread used");
            }
            return new JWindow(title, width, height);
        }
    }

}
