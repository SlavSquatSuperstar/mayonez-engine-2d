package mayonez.application;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.*;
import org.lwjgl.BufferUtils;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * The display component for the game, using LWJGL.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
final class GLWindow implements Window {

    // Window Fields
    private final long windowID;
    private final String title;
    private final int width, height;
    private Vec2 lastPos, lastSize;

    // Input Fields
    private final GLKeyManager keyboard;
    private final GLMouseManager mouse;

    /**
     * Initialize GLFW and create the GLFW window.
     * <p>
     * Source: <a href="https://www.lwjgl.org/guide">LWJGL starter guide</a>
     *
     * @param title  the window title
     * @param width  the window width
     * @param height the window height
     * @throws WindowInitException if GLFW cannot be initialized
     */
    GLWindow(String title, int width, int height) throws WindowInitException {
        this.title = title;
        this.width = width;
        this.height = height;

        // Initialize window
        GLFWHelper.initGLFW();
        windowID = GLFWHelper.createGLFWWindow(width, height, title);

        // Important! Detect current context and integrate LWJGL with OpenGL bindings
        glfwMakeContextCurrent(windowID); // Make the OpenGL context current
        glfwSwapInterval(1); // Enable v-sync
        GLHelper.loadOpenGL();

        // Add input handlers
        keyboard = new GLKeyManager();
        glfwSetKeyCallback(windowID, keyboard::keyCallback);

        mouse = new GLMouseManager();
        glfwSetMouseButtonCallback(windowID, mouse::mouseButtonCallback);
        glfwSetCursorPosCallback(windowID, mouse::mousePosCallback);
        glfwSetScrollCallback(windowID, mouse::mouseScrollCallback);

        // Set resize callback
        glfwSetFramebufferSizeCallback(windowID, this::onFrameBufferResized); // Pixels (larger on macOS)
        glfwSetWindowSizeCallback(windowID, this::onWindowResized); // Screen coords
    }

    // Engine methods

    @Override
    public boolean notClosedByUser() {
        return !glfwWindowShouldClose(windowID);
    }

    @Override
    public void start() {
        glfwShowWindow(windowID);
        glfwFocusWindow(windowID);
        lastPos = new Vec2();
        lastSize = WindowProperties.getScreenSize();
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
        System.out.println("window resized");
        System.out.printf("size = %dx%d\n", width, height);
    }

    private void onFrameBufferResized(long windowID, int width, int height) {
        System.out.println("frame buffer resized");
        System.out.printf("size = %dx%d\n", width, height);
    }

    @Override
    public boolean isFullScreen() {
        return glfwGetWindowMonitor(windowID) != NULL;
    }

    @Override
    public void setFullScreen(boolean fullScreen) {
        if (fullScreen) setFullScreen();
        else setWindowed();
    }

    public void setFullScreen() {
        // Save previous position and size
        var posX = BufferUtils.createIntBuffer(1);
        var posY = BufferUtils.createIntBuffer(1);
        glfwGetWindowPos(windowID, posX, posY);
        lastPos = new Vec2(posX.get(), posY.get());
        System.out.println("last pos = " + lastPos);

        var sizeX = BufferUtils.createIntBuffer(1);
        var sizeY = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(windowID, sizeX, sizeY);
        lastSize = new Vec2(sizeX.get(), sizeY.get());
        System.out.println("last size = " + lastSize);

        glfwSetWindowMonitor(
                windowID,
                glfwGetPrimaryMonitor(), 0, 0,
                Preferences.getScreenWidth(), Preferences.getScreenHeight(),
                GLFW_DONT_CARE
        );
        // TODO use full resolution
    }

    public void setWindowed() {
        // Restore previous size and position
        glfwSetWindowMonitor(
                windowID,
                NULL, (int) lastPos.x, (int) lastPos.y,
                (int) lastSize.x, (int) lastSize.y,
                GLFW_DONT_CARE
        );
        // Do this until projection is updated
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
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return String.format("GL Window (%s, %dx%d)", getTitle(), getWidth(), getHeight());
    }

}
