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
     * Creates a new {@link Application} with the given window.
     *
     * @param window the application window
     * @return the application
     */
    public static Application createApplication(Window window) {
        // TODO In AWT, this doesn't update camera screen size
        KeyInput.setHandler(window.getKeyInputHandler());
        MouseInput.setHandler(window.getMouseInputHandler());
        return new Application(window);
    }

    /**
     * Creates a new {@link Window} with the given engine type.
     *
     * @param useGL  whether to use OpenGL instead of Java's AWT library
     * @param config the window's initialization parameters
     * @return the window
     * @throws WindowInitException if the wrong thread is used on macOS
     */
    public static Window createWindow(boolean useGL, WindowConfig config)
            throws WindowInitException {
        // Check that correct thread is used on macOS
        var isMacOS = OperatingSystem.getCurrent() == OperatingSystem.MAC_OS;
        if (useGL) {
            if (isMacOS && !JVMHelper.isStartedOnFirstThread()) {
                Logger.fatal("GLFW must be initialized from the main thread on macOS");
                Logger.fatal("Make sure to run Java with the \"-XstartOnFirstThread\" VM argument");
                throw new WindowInitException("Aborting GLFW initialization due to main thread not used");
            }
            return new GLWindow(config);
        } else {
            // VM args must be checked before AWT classes are used
            if (isMacOS && JVMHelper.isStartedOnFirstThread()) {
                Logger.fatal("AWT cannot be used from the main thread on macOS");
                Logger.fatal("Make sure to run Java without the \"-XstartOnFirstThread\" VM argument");
                throw new WindowInitException("Aborting AWT window creation due to main thread used");
            }
            return new JWindow(config);
        }
    }

}
