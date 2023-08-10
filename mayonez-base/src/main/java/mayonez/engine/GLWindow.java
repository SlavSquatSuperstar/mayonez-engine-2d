package mayonez.engine;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.input.*;
import org.lwjgl.opengl.GL;

import static mayonez.engine.GLFWHelper.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * The display component for the game, using LWJGL.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
final class GLWindow implements Window {

    // Window Fields
    private long windowID;
    private final String title;
    private final int width, height;

    // Input Fields
    private KeyInput keyboard;
    private MouseInput mouse;

    GLWindow(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        try {
            initWindow();
        } catch (GLFWException e) {
            Logger.printStackTrace(e);
            Mayonez.stop(ExitCode.ERROR);
        }
    }

    // Engine methods

    @Override
    public boolean notClosedByUser() {
        return !glfwWindowShouldClose(windowID);
    }

    /**
     * Initialize the GLFW window and its features.
     * <p>
     * Source: <a href="https://www.lwjgl.org/guide">LWJGL starter guide</a>
     */
    private void initWindow() throws GLFWException {
        initGLFW();
        windowID = createGLFWWindow(width, height, title);

        // Important! Detect current context and integrate LWJGL with OpenGL bindings
        glfwMakeContextCurrent(windowID); // Make the OpenGL context current
        glfwSwapInterval(1); // Enable v-sync
        GL.createCapabilities();

        // Set renderer settings
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void start() {
        glfwShowWindow(windowID);
        glfwFocusWindow(windowID);
    }

    @Override
    public void stop() {
        glfwFreeCallbacks(windowID);
//        glfwSetWindowShouldClose(window, true);
        glfwDestroyWindow(windowID);
        glfwTerminate();
        var oldCbFun = glfwSetErrorCallback(null);
        if (oldCbFun != null) oldCbFun.free();
    }

    // Game Loop Methods

    @Override
    public void beginFrame() {
        glfwPollEvents();
    }

    @Override
    public void render(Scene scene) {
        glClearColor(1f, 1f, 1f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear screen
        scene.render(null); // Don't pass G2D
        glfwSwapBuffers(windowID); // Swap color buffers
    }

    @Override
    public void endFrame() {
        keyboard.endFrame();
        mouse.endFrame();
    }

    // Input Methods

    @Override
    public void setKeyInput(KeyInput keyboard) {
        this.keyboard = keyboard;
        glfwSetKeyCallback(windowID, keyboard::keyCallback);
    }

    @Override
    public void setMouseInput(MouseInput mouse) {
        this.mouse = mouse;
        glfwSetMouseButtonCallback(windowID, mouse::mouseButtonCallback);
        glfwSetCursorPosCallback(windowID, mouse::mousePosCallback);
        glfwSetScrollCallback(windowID, mouse::mouseScrollCallback);
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
