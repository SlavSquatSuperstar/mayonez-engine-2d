package mayonez.application;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.math.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.createPrint;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Assists in GLFW window creation for the OpenGL engine.
 * <p>
 * Sources:
 * <li><a href="https://www.glfw.org/docs/latest/window_guide.html">
 * GLFW Window Guide</a></li>
 * <li><a href="https://www.glfw.org/docs/latest/group__monitor.html">
 * GLFW Monitor Reference</a></li>
 * <li><a href="https://github.com/LWJGL/lwjgl3-wiki/wiki/1.3.-Memory-FAQ">
 * LWJGL Memory FAQ</a></li>
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
final class GLFWHelper {

    private GLFWHelper() {
    }

    /**
     * Initializes the GLFW library.
     */
    static void initGLFW() throws WindowInitException {
        createPrint(System.err).set(); // Setup error callback
        if (!glfwInit()) {
            throw new WindowInitException("Unable to initialize GLFW");
        }
    }

    /**
     * Create a new window and return its GLFW pointer.
     *
     * @param config the initialization parameters
     * @return the window id
     */
    static long createGLFWWindow(WindowConfig config) throws WindowInitException {
        // Create window
        configureWindowHints();
        var monitor = config.fullScreen() ? glfwGetPrimaryMonitor() : NULL;
        var windowID = glfwCreateWindow(
                config.width(), config.height(), config.title(), monitor, NULL
        );
        if (windowID == NULL) {
            throw new WindowInitException("Could not create the GLFW window");
        }

        // Very important!
        glfwMakeContextCurrent(windowID); // Make the OpenGL context current
        glfwSwapInterval(1); // Enable v-sync
        GLHelper.loadOpenGL(); // Integrate LWJGL with OpenGL bindings
        GLHelper.enableBlending();
        return windowID;
    }

    /**
     * Set the GLFW window hints for the application window.
     */
    private static void configureWindowHints() {
        glfwDefaultWindowHints(); // Reset window settings
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Stay hidden until after creation
        glfwWindowHint(GLFW_DECORATED, GLFW_TRUE); // Enable title bar
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Allow user and OS resizing
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE); // Scale screen properly for Windows

        // Set GLFW context profile to core (forward compatible)
        // macOS only supports OpenGL versions 3.2-4.1, inclusive
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        if (GLHelper.isUseOldGlVersion()) {
            // Fallback version 3.3
            Logger.debug("Creating OpenGL 3.3 context");
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        } else {
            // Default version 4.0
            Logger.debug("Creating OpenGL 4.0 context");
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        }

    }

    /**
     * Set the application's window scale parameter.
     *
     * @param windowID the GLFW window pointer
     */
    static void setWindowScale(long windowID) {
        // Source: https://github.com/glfw/glfw/issues/845
        var contentScale = getWindowContentScale(windowID);
        var framebufferRatio = getWindowFramebufferRatio(windowID);
        var windowScale = contentScale.mul(framebufferRatio);
        WindowProperties.setWindowScale(windowScale);
        Logger.debug("Window scale has been set to %s", windowScale);
    }

    /**
     * How much the display has been scaled by.
     *
     * @param windowID the GLFW window pointer
     * @return the content scale
     */
    private static Vec2 getWindowContentScale(long windowID) {
        try (var stack = stackPush()) {
            var xScale = stack.mallocFloat(1);
            var yScale = stack.mallocFloat(1);
            glfwGetWindowContentScale(windowID, xScale, yScale);
            return new Vec2(xScale.get(0), yScale.get(0));
        }
    }

    /**
     * The ratio between the window size and the framebuffer (rendered image) size.
     * On Windows, the ratio should equal 1:1, and on Unix, the ratio should be
     * the reciprocal of the content scale.
     *
     * @param windowID the GLFW window pointer
     * @return the ratio
     */
    private static Vec2 getWindowFramebufferRatio(long windowID) {
        var windowSize = getWindowSize(windowID);
        var framebufferSize = getFramebufferSize(windowID);
        return windowSize.div(framebufferSize);
    }

    /**
     * Center the window inside the current display.
     *
     * @param windowID the GLFW window pointer
     */
    static void centerWindowPosition(long windowID) {
        try (var stack = stackPush()) {
            var xPos = stack.mallocInt(1);
            var yPos = stack.mallocInt(1);
            var width = stack.mallocInt(1);
            var height = stack.mallocInt(1);
            // Use this over glfwGetVideoMode to account for menu bars
            glfwGetMonitorWorkarea(glfwGetPrimaryMonitor(), xPos, yPos, width, height);

            var windowSize = getWindowSize(windowID);
            var xCenterPos = xPos.get(0) + (width.get(0) - (int) windowSize.x) / 2;
            var yCenterPos = yPos.get(0) + (height.get(0) - (int) windowSize.y) / 2;
            glfwSetWindowPos(windowID, xCenterPos, yCenterPos);
        }
    }

    /**
     * The dimensions of the top-left corner of the window content area in
     * screen units.
     *
     * @param windowID the GLFW window pointer
     * @return the window size
     */
    static Vec2 getWindowPos(long windowID) {
        try (var stack = stackPush()) {
            var xSize = stack.mallocInt(1);
            var ySize = stack.mallocInt(1);
            glfwGetWindowPos(windowID, xSize, ySize);
            return new Vec2(xSize.get(0), ySize.get(0));
        }
    }

    /**
     * The dimensions of the application window content area in screen units.
     * On a macOS device with a Retina display, this is different from the
     * framebuffer size.
     *
     * @param windowID the GLFW window pointer
     * @return the window size
     */
    static Vec2 getWindowSize(long windowID) {
        try (var stack = stackPush()) {
            var xSize = stack.mallocInt(1);
            var ySize = stack.mallocInt(1);
            glfwGetWindowSize(windowID, xSize, ySize);
            return new Vec2(xSize.get(0), ySize.get(0));
        }
    }

    /**
     * The dimensions of the rendered framebuffer in pixels. On a macOS device
     * with a Retina display, this is different from the window size.
     *
     * @param windowID the GLFW window pointer
     * @return the framebuffer size
     */
    static Vec2 getFramebufferSize(long windowID) {
        try (var stack = stackPush()) {
            var xSize = stack.mallocInt(1);
            var ySize = stack.mallocInt(1);
            glfwGetFramebufferSize(windowID, xSize, ySize);
            return new Vec2(xSize.get(0), ySize.get(0));
        }
    }

}
