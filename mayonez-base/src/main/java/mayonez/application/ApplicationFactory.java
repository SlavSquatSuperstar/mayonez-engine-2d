package mayonez.application;

import mayonez.Logger;
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
        // Create window
        var window = createWindow(useGL, title, width, height);
        KeyInput.setHandler(window.getKeyInputHandler());
        MouseInput.setHandler(window.getMouseInputHandler());

        return useGL
                ? new GLApplication(window)
                : new JApplication(window);
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
        if (useGL) {
            if (OperatingSystem.getCurrent() == OperatingSystem.MAC_OS
                    && !JVMHelper.isStartedOnFirstThread()) {
                Logger.fatal("GLFW must be initialized from the main thread on macOS");
                Logger.fatal("Make sure to run Java with the \"-XstartOnFirstThread\" VM argument");
                throw new WindowInitException("Aborting GLFW initialization due to main thread not used");
            }
            return new GLWindow(title, width, height);
        } else {
            // VM args must be checked before AWT classes are used
            if (OperatingSystem.getCurrent() == OperatingSystem.MAC_OS
                    && JVMHelper.isStartedOnFirstThread()) {
                Logger.fatal("AWT cannot be used from the main thread on macOS");
                Logger.fatal("Make sure to run Java without the \"-XstartOnFirstThread\" VM argument");
                throw new WindowInitException("Aborting AWT window creation due to main thread used");
            }
            return new JWindow(title, width, height);
        }
    }

}
