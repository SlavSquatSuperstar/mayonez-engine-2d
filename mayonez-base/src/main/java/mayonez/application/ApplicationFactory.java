package mayonez.application;

import mayonez.*;
import mayonez.config.RunConfig;
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
        KeyInput.setHandler(window.getKeyInputHandler());
        MouseInput.setHandler(window.getMouseInputHandler());
        return new Application(window);
    }

    /**
     * Creates a new {@link Window} with the given engine type.
     *
     * @param runConfig    which windowing framework to use
     * @param windowConfig the window's initialization parameters
     * @return the window
     * @throws WindowInitException if the wrong thread is used on macOS
     */
    public static Window createWindow(RunConfig runConfig, WindowConfig windowConfig)
            throws WindowInitException {
        if (runConfig.useGL()) {
            WindowLibrary.GLFW.check();
            WindowLibrary.GLFW.init();
            return new GLWindow(windowConfig, runConfig);
        } else {
            WindowLibrary.AWT.check();
            WindowLibrary.AWT.init();
            return new JWindow(windowConfig);
        }
    }

}
