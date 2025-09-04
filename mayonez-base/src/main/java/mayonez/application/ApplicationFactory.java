package mayonez.application;

import mayonez.*;
import mayonez.input.*;
import mayonez.util.OperatingSystem;

/**
 * A factory class that constructs {@link Application} and
 * {@link Window} objects depending on the run configuration.
 *
 * @author SlavSquatSuperstar
 */
public final class ApplicationFactory {

    private ApplicationFactory() {
    }

    // Factory Methods

    /**
     * Creates a new {@link Application} object with the given
     * engine type.
     *
     * @param useGL  whether to use OpenGL instead of AWT
     * @param title  the window title
     * @param width  the window width
     * @param height the window height
     * @return the game engine
     * @throws WindowInitException if the wrong thread is used on macOS
     */
    public static Application createApplication(
            boolean useGL, String title, int width, int height
    ) throws WindowInitException {
        // TODO Pass fullscreen parameter
        // Create window
        var window = createWindow(useGL, title, width, height);
        // TODO In AWT, this doesn't update camera screen size
        window.setFullScreen(Preferences.isFullscreen());
        KeyInput.setHandler(window.getKeyInputHandler());
        MouseInput.setHandler(window.getMouseInputHandler());

        return new Application(window);
    }

    /**
     * Creates a new {@link Window} object with the given engine
     * type.
     *
     * @param useGL  whether to use OpenGL instead of Java's AWT library
     * @param title  the window title
     * @param width  the window width
     * @param height the window height
     * @return the window
     * @throws WindowInitException if the wrong thread is used on macOS
     */
    private static Window createWindow(
            boolean useGL, String title, int width, int height
    ) throws WindowInitException {
        // Check that correct thread is used on macOS
        var isMacOS = OperatingSystem.getCurrent() == OperatingSystem.MAC_OS;
        if (useGL) {
            if (isMacOS && !JVMHelper.isStartedOnFirstThread()) {
                Logger.fatal("GLFW must be initialized from the main thread on macOS");
                Logger.fatal("Make sure to run Java with the \"-XstartOnFirstThread\" VM argument");
                throw new WindowInitException("Aborting GLFW initialization due to main thread not used");
            }
            return new GLWindow(title, width, height);
        } else {
            // VM args must be checked before AWT classes are used
            if (isMacOS && JVMHelper.isStartedOnFirstThread()) {
                Logger.fatal("AWT cannot be used from the main thread on macOS");
                Logger.fatal("Make sure to run Java without the \"-XstartOnFirstThread\" VM argument");
                throw new WindowInitException("Aborting AWT window creation due to main thread used");
            }
            return new JWindow(title, width, height);
        }
    }

}
