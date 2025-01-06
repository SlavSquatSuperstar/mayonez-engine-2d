package mayonez.application;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.input.keyboard.*;
import mayonez.input.mouse.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

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

    // Input Fields
    private final GLKeyManager keyboard;
    private final GLMouseManager mouse;

    /**
     * Initialize GLFW and create the GLFW window.
     * <p>
     * Source: <a href="https://www.lwjgl.org/guide">LWJGL starter guide</a>
     *
     * @param title the window title
     * @param width the window width
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
