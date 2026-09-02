package mayonez.application;

import mayonez.Logger;

/**
 * A windowing library or framework that handles window creation and events.
 *
 * @author SlavSquatSuperstar
 */
public enum WindowLibrary {

    /**
     * Java's Abstract Window Toolkit and Swing packages.
     */
    AWT {
        @Override
        public void check() throws WindowInitException {
            // Check that correct thread is used on macOS
            // VM args must be checked before AWT classes are used
            if (JVMHelper.isMacOS() && JVMHelper.isStartedOnFirstThread()) {
                Logger.fatal("AWT cannot be used from the main thread on macOS");
                Logger.fatal("Make sure to run Java without the \"-XstartOnFirstThread\" VM argument");
                throw new WindowInitException("Cannot create AWT window on main thread");
            }
        }
    },

    /**
     * LWJGL's Graphics Library Framework.
     */
    GLFW {
        @Override
        public void check() throws WindowInitException {
            // Check that correct thread is used on macOS
            if (JVMHelper.isMacOS() && !JVMHelper.isStartedOnFirstThread()) {
                Logger.fatal("GLFW must be initialized from the main thread on macOS");
                Logger.fatal("Make sure to run Java with the \"-XstartOnFirstThread\" VM argument");
                throw new WindowInitException("Cannot initialize GLFW outside of main thread");
            }
        }

        @Override
        public void init() throws WindowInitException {
            GLFWHelper.initGLFW();
        }

        @Override
        public void free() {
            GLFWHelper.freeGLFW();
        }
    };

    /**
     * Check if all platform prerequisites have been met.
     *
     * @throws WindowInitException if the prerequisites are not met
     */
    public abstract void check() throws WindowInitException;

    /**
     * Initialize the windowing library.
     *
     * @throws WindowInitException if the library could not be initialized
     */
    public void init() throws WindowInitException {
    }

    /**
     * Free the windowing library.
     */
    public void free() {
    }

}
