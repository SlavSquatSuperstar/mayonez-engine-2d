package mayonez.engine;

import mayonez.ExitCode;
import mayonez.Logger;
import mayonez.Mayonez;
import mayonez.Scene;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;
import mayonez.math.Vec2;
import mayonez.util.OperatingSystem;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * The display component for the game, using LWJGL.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
final class GLWindow implements Window {

    // Window Fields
    private long window; // The window pointer
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
        return !glfwWindowShouldClose(window);
    }

    /**
     * Initialize the GLFW window and its features.
     * <p>
     * Source: <a href="https://www.lwjgl.org/guide">LWJGL starter guide</a>
     */
    private void initWindow() throws GLFWException {
        initGLFW();
        createGLFWWindow();
        setWindowProperties();

        // Very important!
        // Detect current context and integrate LWJGL with OpenGL bindings
        GL.createCapabilities();

        setWindowSettings();
    }

    private void initGLFW() throws GLFWException {
        GLFWErrorCallback.createPrint(System.err).set(); // Setup error callback
        if (!glfwInit()) {
            if (OperatingSystem.getCurrentOS() == OperatingSystem.MAC_OS) {
                Logger.error("GLFW must run with the \"-XstartOnFirstThread\" VM argument on macOS");
            }
            throw new GLFWException("Unable to initialize GLFW");
        }
    }

    private void createGLFWWindow() throws GLFWException {
        configureWindowSettings();
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new GLFWException("Could not create the GLFW window");
        }
        setWindowScale();
    }

    private void configureWindowSettings() {
        glfwDefaultWindowHints(); // Reset window settings
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Don't stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // Don't allow resizing
        // Set proper version for macOS
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        // Scale properly for Windows
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
    }

    private void setWindowScale() {
        var xScaleBuff = BufferUtils.createFloatBuffer(1);
        var yScaleBuff = BufferUtils.createFloatBuffer(2);
        glfwGetWindowContentScale(window, xScaleBuff, yScaleBuff);

        var xScale = xScaleBuff.get(0);
        var yScale = yScaleBuff.get(0);
        Mayonez.setWindowScale(new Vec2(xScale, yScale));
        Logger.log("Window scaling is %.2fx%.2f", xScale, yScale);
    }

    private void setWindowProperties() {
        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            // Store window size in two pointers
            IntBuffer windowWidth = stack.mallocInt(1);
            IntBuffer windowHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, windowWidth, windowHeight);

            // Center the window
            GLFWVidMode screenResolution = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(
                    window,
                    (screenResolution.width() - windowWidth.get(0)) / 2,
                    (screenResolution.height() - windowHeight.get(0)) / 2
            );
        } // The stack frame is popped automatically

        glfwMakeContextCurrent(window); // Make the OpenGL context current
        glfwSwapInterval(1); // Enable v-sync
    }

    private void setWindowSettings() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void start() {
        glfwShowWindow(window);
        glfwFocusWindow(window);
    }

    @Override
    public void stop() {
        glfwFreeCallbacks(window);
//        glfwSetWindowShouldClose(window, true);
        glfwDestroyWindow(window);
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
        glfwSwapBuffers(window); // Swap color buffers
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
        glfwSetKeyCallback(window, keyboard::keyCallback);
    }

    @Override
    public void setMouseInput(MouseInput mouse) {
        this.mouse = mouse;
        glfwSetMouseButtonCallback(window, mouse::mouseButtonCallback);
        glfwSetCursorPosCallback(window, mouse::mousePosCallback);
        glfwSetScrollCallback(window, mouse::mouseScrollCallback);
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
