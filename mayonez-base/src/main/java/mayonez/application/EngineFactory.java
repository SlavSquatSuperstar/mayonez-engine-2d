package mayonez.application;

import mayonez.*;
import mayonez.input.keyboard.KeyManager;
import mayonez.input.mouse.MouseManager;
import mayonez.io.*;

/**
 * A factory class that constructs {@link GameEngine} and
 * {@link Window} objects depending on the run configuration.
 *
 * @author SlavSquatSuperstar
 */
public final class EngineFactory {

    private EngineFactory() {
    }

    // Game Engine Methods

    /**
     * Creates a new {@link GameEngine} object with the given
     * engine type.
     *
     * @param useGL      whether to use OpenGL instead of Java's AWT library
     * @param keyInput   the key input instance
     * @param mouseInput the mouse input instance
     * @return the game engine
     * @throws EngineInitException if the wrong thread is used on macOS
     */
    public static GameEngine createGameEngine(
            boolean useGL, KeyManager keyInput, MouseManager mouseInput
    ) throws EngineInitException {
        // Create window
        var title = String.format("%s (%s) %s",
                Preferences.getTitle(), (useGL ? "GL" : "AWT"), Preferences.getVersion()
        );
        var width = Preferences.getScreenWidth();
        var height = Preferences.getScreenHeight();
        var window = createWindow(useGL, title, width, height);

        return useGL
                ? new GLGameEngine(window, keyInput, mouseInput)
                : new JGameEngine(window, keyInput, mouseInput);
    }

    // Window Methods

    /**
     * Creates a new {@link Window} object with the given engine
     * type.
     *
     * @param useGL  whether to use OpenGL instead of Java's AWT library
     * @param title  the window title
     * @param width  the window width
     * @param height the window height
     * @return the window
     * @throws EngineInitException if the wrong thread is used on macOS
     */
    private static Window createWindow(
            boolean useGL, String title, int width, int height
    ) throws EngineInitException {
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
