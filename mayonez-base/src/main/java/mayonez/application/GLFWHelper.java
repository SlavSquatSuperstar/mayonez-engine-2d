package mayonez.application;

import mayonez.*;
import mayonez.config.RunConfig;
import mayonez.graphics.*;
import mayonez.math.*;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.Comparator;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.getDescription;
import static org.lwjgl.system.APIUtil.apiClassTokens;
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

    private static final Map<Integer, String> ERROR_CODES = apiClassTokens(
            (field, value) -> 0x10000 < value && value < 0x20000, null, GLFW.class
    );
    private static int platform = GLFW_ANY_PLATFORM;


    private GLFWHelper() {
    }

    // Initialization Methods

    /**
     * Initializes the GLFW library.
     */
    static void initGLFW() throws WindowInitException {
        // noinspection resource
        glfwSetErrorCallback(GLFWHelper::logGLFWError); // Use logger for error callback
        if (!glfwInit()) {
            throw new WindowInitException("Unable to initialize GLFW");
        }
        Logger.debug("Initialized the GLFW library");
    }

    // From GLFWErrorCallback.createPrint
    private static void logGLFWError(int code, long description) {
        var trace = Thread.currentThread().getStackTrace();
        var msg = new StringBuilder("GLFW: ")
                .append(ERROR_CODES.get(code))
                .append("\n\t")
                .append(getDescription(description));
        for (int i = 4; i < trace.length; i++) {
            msg.append("\n\t\t");
            msg.append(trace[i]);
        }
        Logger.error(msg.toString());
    }

    /**
     * Create a new window and return its GLFW pointer.
     *
     * @param windowConfig the initialization parameters
     * @param runConfig    the backend initialization parameters
     * @return the window id
     */
    static GLFWWindow createGLFWWindow(WindowConfig windowConfig, RunConfig runConfig) throws WindowInitException {
        // Create window
        configureWindowHints(windowConfig);
        configureContextHints(runConfig);

        // Get closest video mode
        var vidMode = getNearestVideoMode(windowConfig);
        if (vidMode == null) {
            throw new WindowInitException("Could not set the GLFW video mode");
        }

        int width, height;
        if (windowConfig.fullScreen()) {
            // Use video mode dimensions if fullscreen
            width = vidMode.width();
            height = vidMode.height();
        } else {
            // Otherwise use provided dimensions
            width = windowConfig.width();
            height = windowConfig.height();
        }

        var monitor = windowConfig.fullScreen() ? glfwGetPrimaryMonitor() : NULL;
        var windowID = glfwCreateWindow(
               width, height, windowConfig.title(), monitor, NULL
        );
        if (windowID == NULL) {
            throw new WindowInitException("Could not create the GLFW window");
        }

        // Very important!
        glfwMakeContextCurrent(windowID); // Make the OpenGL context current
        glfwSwapInterval(1); // Enable v-sync
        GLHelper.loadOpenGL(); // Integrate LWJGL with OpenGL bindings

        return new GLFWWindow(windowID, vidMode);
    }

    /**
     * The nearest available full screen resolution to the preferred one, or otherwise the current
     * resolution.
     *
     * @return the best video mode, or null on error
     */
    static @Nullable GLFWVidMode getNearestVideoMode(WindowConfig config) {
        var monitor = glfwGetPrimaryMonitor();
        var currentVidMode = glfwGetVideoMode(monitor);
        var vidModes = glfwGetVideoModes(monitor);

        if (vidModes == null) return currentVidMode;
        return vidModes.stream()
                .min(Comparator.comparingInt(mode -> getDistanceSquared(mode, config)))
                .orElse(currentVidMode);
    }

    private static int getDistanceSquared(GLFWVidMode mode, WindowConfig config) {
        var xDiff = mode.width() - config.width();
        var yDiff = mode.height() - config.height();
        return (xDiff * xDiff) + (yDiff * yDiff);
    }

    /**
     * Set the GLFW window hints for the application window.
     *
     * @param config the initialization parameters
     */
    private static void configureWindowHints(WindowConfig config) {
        glfwDefaultWindowHints(); // Reset window settings
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Stay hidden until after creation
        glfwWindowHint(GLFW_DECORATED, GLFW_TRUE); // Enable title bar
        glfwWindowHint(GLFW_RESIZABLE, config.resizable() ? GLFW_TRUE : GLFW_FALSE); // Allow user and OS resizing
        /*
         * Note: If GLFW_SCALE_TO_MONITOR is set to true (false by default),
         * then the window size (in screen coordinates) is scaled with the OS
         * settings on Windows and Linux. Then, all mouse coordinates need to
         * be scaled by contentScale / (framebufferSize / windowSize)
         *
         * Source: https://github.com/glfw/glfw/issues/845
         */
    }

    /**
     * Set the OpenGL context hints for the application window.
     *
     * @param runConfig the context configuration
     */
    private static void configureContextHints(RunConfig runConfig) {
        // Set GLFW context profile to core (forward compatible)
        // macOS only supports OpenGL versions 3.2-4.1, inclusive
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        if (runConfig.glFallback()) {
            // Fallback version 3.3
            Logger.debug("Using OpenGL 3.3 context");
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        } else {
            // Default version 4.0
            Logger.debug("Using OpenGL 4.0 context");
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        }
        GLHelper.setUseOldGLVersion(runConfig.glFallback());
    }

    // Window Position Methods

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
            // Raises GLFW_FEATURE_UNAVAILABLE on Wayland
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
    static Vec2 getWindowPosition(long windowID) {
        try (var stack = stackPush()) {
            var xSize = stack.mallocInt(1);
            var ySize = stack.mallocInt(1);
            // Raises GLFW_FEATURE_UNAVAILABLE on Wayland
            glfwGetWindowPos(windowID, xSize, ySize);
            return new Vec2(xSize.get(0), ySize.get(0));
        }
    }

    // Window Size Methods

    /**
     * The dimensions of the window content area, excluding the title bar,
     * in screen units. On a macOS device with a Retina display, this is
     * different from the framebuffer size. Also see {@link #getFramebufferSize}
     * and {@link #getWindowFrameSize}.
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
     * with a Retina display, this is different from the window size. Also see
     * {@link #getWindowSize} and {@link #getFramebufferWindowRatio}.
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

    /**
     * The additional distances of the window frame, including the title bar,
     * in screen units. Also see {@link #getWindowSize}.
     *
     * @param windowID the GLFW window pointer
     * @return the window size
     */
    static Vec2 getWindowFrameSize(long windowID) {
        try (var stack = stackPush()) {
            var left = stack.mallocInt(1);
            var top = stack.mallocInt(1);
            var right = stack.mallocInt(1);
            var bottom = stack.mallocInt(1);
            glfwGetWindowFrameSize(windowID, left, top, right, bottom);
            return new Vec2(left.get(0) + right.get(0), top.get(0) + bottom.get(0));
        }
    }

    /**
     * How much the window's contents should be scaled by. The content scale
     * primarily affects the size and position scaling of UI and text elements
     * on high-DPI screens. On Windows and Linux, changing the scaling in the
     * OS settings affects the content scale, but fractional scaling may not be
     * supported (scale rounds up). On macOS Retina devices, the content scale
     * is typically 2x2 unless the resolution is set very high or very low.
     * In full screen mode, then content scale is usually 1x1.
     *
     * @param windowID the GLFW window pointer
     * @return the content scale
     */
    static Vec2 getWindowContentScale(long windowID) {
        try (var stack = stackPush()) {
            var xScale = stack.mallocFloat(1);
            var yScale = stack.mallocFloat(1);
            glfwGetWindowContentScale(windowID, xScale, yScale);
            return new Vec2(xScale.get(0), yScale.get(0));
        }
    }

    /**
     * The ratio between the framebuffer size and window size, measured in pixels
     * to screen units. On most devices, the ratio equals 1:1, but on high-DPI
     * monitors such as macOS Retina displays, the ratio may be higher when in
     * windowed mode. On macOS with Retina, the ratio also equals the content
     * scale. Also see {@link #getFramebufferSize} and {@link #getWindowSize}
     *
     * @param windowID the GLFW window pointer
     * @return the ratio
     */
    static Vec2 getFramebufferWindowRatio(long windowID) {
        var framebufferSize = getFramebufferSize(windowID);
        var windowSize = getWindowSize(windowID);
        return framebufferSize.div(windowSize);
    }

    // Termination Methods

    /**
     * Terminates the GLFW library.
     */
    static void freeGLFW() {
        glfwTerminate();
        Logger.debug("Terminated the GLFW library");
        var oldCbFun = glfwSetErrorCallback(null);
        if (oldCbFun != null) oldCbFun.free(); // Free callback function
        GLHelper.unloadOpenGL();
    }

    // Helper Class

    record GLFWWindow(long windowID, GLFWVidMode vidMode) {
    }

}
