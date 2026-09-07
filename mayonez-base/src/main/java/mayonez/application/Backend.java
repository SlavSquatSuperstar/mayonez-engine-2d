package mayonez.application;

import mayonez.Logger;

/**
 * A backend for the game engine that uses a windowing library to manage the
 * window and receives input and a graphics API to render to the window. All
 * backends share the same interface, but some may lack certain features or
 * have minor differences across platforms.  Mayonez Engine currently supports
 * two backends: GLFW/OpenGL (default) and AWT, both of which are portable.
 * <p>
 * See also: <a href="https://github.com/ocornut/imgui/blob/master/docs/BACKENDS.md">ImGui Backends</a>
 *
 * @author SlavSquatSuperstar
 */
public enum Backend {

    /**
     * Java's built-in Abstract Window Toolkit and Swing packages. Runs on all
     * platforms. Does not currently support UI or text rendering.
     */
    AWT("AWT") {
        @Override
        public void init() throws WindowInitException {
            // Check that correct thread is used on macOS
            // VM args must be checked before AWT classes are used
            if (JVMHelper.isMacOS() && JVMHelper.isStartedOnFirstThread()) {
                Logger.fatal("AWT cannot be used from the main thread on macOS");
                Logger.fatal("Make sure to run Java without the \"-XstartOnFirstThread\" VM argument");
                throw new WindowInitException("Cannot create AWT window on main thread");
            }
        }

        @Override
        public Window createWindow(WindowConfig windowConfig) {
            return new JWindow(windowConfig);
        }
    },

    /**
     * LWJGL's low-level Open Graphics Library and Graphics Library Framework. Runs on most platforms.
     */
    GL("OpenGL/GLFW") {
        @Override
        public void init() throws WindowInitException {
            // Check that correct thread is used on macOS
            if (JVMHelper.isMacOS() && !JVMHelper.isStartedOnFirstThread()) {
                Logger.fatal("GLFW must be initialized from the main thread on macOS");
                Logger.fatal("Make sure to run Java with the \"-XstartOnFirstThread\" VM argument");
                throw new WindowInitException("Cannot initialize GLFW outside of main thread");
            }
            GLFWHelper.initGLFW();
        }

        @Override
        public Window createWindow(WindowConfig windowConfig)
                throws WindowInitException {
            return new GLWindow(windowConfig);
        }

        @Override
        public void free() {
            GLFWHelper.freeGLFW();
        }
    };

    private final String name;

    Backend(String name) {
        this.name = name;
    }

    /**
     * Initialize the backend library and any contexts, and check if all
     * platform prerequisites have been met.
     *
     * @throws WindowInitException if the library could not be initialized
     */
    public void init() throws WindowInitException {
    }

    /**
     * Create a window using the backend library.
     *
     * @param windowConfig the window parameters
     * @return the window
     * @throws WindowInitException if the window could not be created
     */
    public abstract Window createWindow(WindowConfig windowConfig)
            throws WindowInitException;

    /**
     * Free the backend library and any contexts.
     */
    public void free() {
    }

    @Override
    public String toString() {
        return name;
    }

}
