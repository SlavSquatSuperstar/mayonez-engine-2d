package mayonez.application;

import mayonez.input.*;

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
     * @param backend      the engine backend
     * @param windowConfig the window initialization parameters
     * @return the window
     * @throws WindowInitException if the wrong thread is used on macOS
     */
    public static Window createWindow(Backend backend, WindowConfig windowConfig)
            throws WindowInitException {
        var windowLibrary = backend.window();
        windowLibrary.check();
        windowLibrary.init();
        return windowLibrary.createWindow(windowConfig);
    }

}
