package mayonez.application;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.*;
import org.lwjgl.glfw.GLFWVidMode;

import static mayonez.application.GLFWHelper.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * A window created using LWJGL's GLFW and OpenGL libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.GL)
final class GLWindow implements Window {

    // Window Fields
    private final long windowID;
    private final String title;
    private int width, height; // Size of window content
    private int frameWidth, frameHeight; // Size of window decorations
    private Vec2 lastPos, lastSize;
    private final GLFWVidMode vidMode;

    // Input Fields
    private final GLKeyManager keyboard;
    private final GLMouseManager mouse;

    /**
     * Initialize GLFW and create the GLFW window.
     * <p>
     * Source: <a href="https://www.lwjgl.org/guide">LWJGL starter guide</a>
     *
     * @param windowConfig the window initialization parameters
     * @throws WindowInitException if GLFW cannot be initialized
     */
    GLWindow(WindowConfig windowConfig) throws WindowInitException {
        this.title = windowConfig.title();
        this.width = windowConfig.width();
        this.height = windowConfig.height();

        // Initialize window
        var window = createGLFWWindow(windowConfig);
        windowID = window.windowID();
        vidMode = window.vidMode();
        setVSyncEnabled(Preferences.useVSync());

        var frameSize = getWindowFrameSize(windowID);
        frameWidth = (int) frameSize.x;
        frameHeight = (int) frameSize.y;

        // Center window position
        centerWindowPosition(windowID);
        lastPos = getWindowPosition(windowID);
        lastSize = getWindowSize(windowID);

        // Add input handlers
        keyboard = new GLKeyManager();
        glfwSetKeyCallback(windowID, keyboard::keyCallback);

        mouse = new GLMouseManager();
        glfwSetMouseButtonCallback(windowID, mouse::mouseButtonCallback);
        glfwSetCursorPosCallback(windowID, mouse::mousePosCallback);
        glfwSetScrollCallback(windowID, mouse::mouseScrollCallback);

        // Set resize callback
        glfwSetFramebufferSizeCallback(windowID, this::onFrameBufferResized); // Pixels (larger on macOS)

        Logger.debug("Created the GLFW window");
        Logger.debug("Starting in %s mode", windowConfig.fullScreen() ? "full screen" : "windowed");
        Logger.debug("Using full screen size %dx%d", vidMode.width(), vidMode.height());
    }

    // Game Loop methods

    @Override
    public void start() {
        glfwShowWindow(windowID);
        glfwFocusWindow(windowID);
    }

    @Override
    public void render() {
        GLHelper.clearScreen(1f, 1f, 1f, 1f);
        SceneManager.renderScene(null); // Don't pass G2D
        glfwSwapBuffers(windowID);
    }

    @Override
    public void stop() {
        glfwFreeCallbacks(windowID);
        glfwSetWindowShouldClose(windowID, true);
        glfwDestroyWindow(windowID);
    }

    @Override
    public float getCurrentTimeSecs() {
        return (float) glfwGetTime();
    }

    // Event Methods

    @Override
    public void pollEvents() {
        // Update input states
        KeyInput.updateKeys();
        MouseInput.updateMouse();

        // Fetch new events
        glfwPollEvents();
    }

    @Override
    public boolean isClosedByUser() {
        return glfwWindowShouldClose(windowID);
    }

    private void onFrameBufferResized(long windowID, int width, int height) {
        // Resize the viewport on Windows and Linux
        glViewport(0, 0, width, height);

        /*
         * Detecting framebuffer resizes is more reliable than detecting window resizes
         * Sometimes framebuffer size may change while window size stays the same
         * May happen if toggling fullscreen or changing monitor DPI
         * Convert from framebuffer pixels to window screen units
         */
        var scale = getContentScale();
        this.width = width / (int) scale.x;
        this.height = height / (int) scale.y;
        WindowEvents.WINDOW_EVENTS.broadcast(new WindowResizeEvent(this.width, this.height));
    }

    // Input Methods

    @Override
    public KeyInputHandler getKeyInputHandler() {
        return keyboard;
    }

    @Override
    public MouseInputHandler getMouseInputHandler() {
        return mouse;
    }

    // Full Screen Methods

    @Override
    public boolean isFullScreen() {
        return glfwGetWindowMonitor(windowID) != NULL;
    }

    @Override
    public void setFullScreen(boolean fullScreen) {
        if (fullScreen) setFullScreen();
        else setWindowed();

        // Update frame size
        var frameSize = getWindowFrameSize(windowID);
        frameWidth = (int) frameSize.x;
        frameHeight = (int) frameSize.y;

        Logger.debug("Set window to %s mode", fullScreen ? "full screen" : "windowed");
    }

    public void setFullScreen() {
        // Save previous position and size
        lastPos = getWindowPosition(windowID);
        lastSize = getWindowSize(windowID);

        glfwSetWindowMonitor(
                windowID,
                glfwGetPrimaryMonitor(), 0, 0,
                vidMode.width(), vidMode.height(), GLFW_DONT_CARE
        );
    }

    public void setWindowed() {
        // Restore previous size and position
        glfwSetWindowMonitor(
                windowID,
                NULL, (int) lastPos.x, (int) lastPos.y,
                (int) lastSize.x, (int) lastSize.y, GLFW_DONT_CARE
        );

        // This is necessary for when the windowed size is equal to the screen resolution
        glfwRestoreWindow(windowID);
        if (platform != GLFW_PLATFORM_WAYLAND) {
            glfwSetWindowPos(windowID, (int) lastPos.x, (int) lastPos.y);
        }
        glfwSetWindowSize(windowID, (int) lastSize.x, (int) lastSize.y);
    }

    // Getters

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return width + frameWidth;
    }

    @Override
    public int getHeight() {
        return height + frameHeight;
    }

    @Override
    public int getContentWidth() {
        return width;
    }

    @Override
    public int getContentHeight() {
        return height;
    }

    @Override
    public Vec2 getContentScale() {
        return GLFWHelper.getWindowContentScale(windowID);
    }

    @Override
    public String toString() {
        return String.format("GLFW Window (%s, %dx%d, %s)",
                getTitle(), getWidth(), getHeight(), isFullScreen() ? "Full Screen" : "Windowed");
    }

}
