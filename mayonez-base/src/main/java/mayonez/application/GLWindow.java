package mayonez.application;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.*;

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
@UsesEngine(EngineType.GL)
final class GLWindow implements Window {

    // Window Fields
    private final long windowID;
    private final String title;
    private int width, height; // GLFW uses content size, unlike AWT
    private Vec2 lastPos, lastSize;
    private int frameWidth, frameHeight; // Extra padding of decorations
    private final int vidModeWidth, vidModeHeight;

    // Input Fields
    private final GLKeyManager keyboard;
    private final GLMouseManager mouse;

    /**
     * Initialize GLFW and create the GLFW window.
     * <p>
     * Source: <a href="https://www.lwjgl.org/guide">LWJGL starter guide</a>
     *
     * @param config the initialization parameters
     * @throws WindowInitException if GLFW cannot be initialized
     */
    GLWindow(WindowConfig config) throws WindowInitException {
        this.title = config.title();
        this.width = config.width();
        this.height = config.height();

        // Initialize window
        initGLFW();
        var window = createGLFWWindow(config);
        windowID = window.windowID();
        vidModeWidth = window.vidModeWidth();
        vidModeHeight = window.vidModeHeight();

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
        glfwSetWindowSizeCallback(windowID, this::onWindowResized); // Screen units
    }

    // Engine methods

    @Override
    public void start() {
        glfwShowWindow(windowID);
        glfwFocusWindow(windowID);
    }

    @Override
    public void stop() {
        glfwFreeCallbacks(windowID);
        glfwSetWindowShouldClose(windowID, true);
        glfwDestroyWindow(windowID);
        glfwTerminate();
        var oldCbFun = glfwSetErrorCallback(null);
        if (oldCbFun != null) oldCbFun.free();
        GLHelper.unloadOpenGL();
    }

    // Game Loop Methods

    @Override
    public boolean notClosedByUser() {
        return !glfwWindowShouldClose(windowID);
    }

    @Override
    public float getCurrentTimeSecs() {
        return (float) glfwGetTime();
    }

    @Override
    public void beginFrame() {
        glfwPollEvents();
    }

    @Override
    public void render() {
        GLHelper.clearScreen(1f, 1f, 1f, 1f);
        SceneManager.renderScene(null); // Don't pass G2D
        glfwSwapBuffers(windowID);
    }

    @Override
    public void endFrame() {
        KeyInput.updateKeys();
        MouseInput.updateMouse();
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

    private void onWindowResized(long windowID, int width, int height) {
        this.width = width;
        this.height = height;
        WindowEvents.WINDOW_EVENTS.broadcast(new WindowResizeEvent(width, height));
    }

    private void onFrameBufferResized(long windowID, int width, int height) {
        // Resize the viewport on Windows and Linux
        glViewport(0, 0, width, height);
    }

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
    }

    public void setFullScreen() {
        // Save previous position and size
        lastPos = getWindowPosition(windowID);
        lastSize = getWindowSize(windowID);

        glfwSetWindowMonitor(
                windowID,
                glfwGetPrimaryMonitor(), 0, 0,
                vidModeWidth, vidModeHeight, GLFW_DONT_CARE
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
        glfwSetWindowPos(windowID, (int) lastPos.x, (int) lastPos.y);
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
