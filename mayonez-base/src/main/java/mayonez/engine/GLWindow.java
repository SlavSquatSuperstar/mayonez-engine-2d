package mayonez.engine;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.input.*;
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
// TODO gl window exception
@UsesEngine(EngineType.GL)
final class GLWindow implements Window {

    private long window; // The window pointer
    private final String title;
    private final int width, height;

    GLWindow(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        initWindow();
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
    private void initWindow() {
        try {
            initGLFW();
            createGLFWWindow();
            setWindowProperties();
        } catch (RuntimeException e) {
            Logger.printStackTrace(e);
            Mayonez.stop(ExitCode.ERROR);
        }

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    private static void initGLFW() throws RuntimeException {
        GLFWErrorCallback.createPrint(System.err).set(); // Setup error callback
        try {
            if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        } catch (IllegalStateException | ExceptionInInitializerError e) {
            throw new RuntimeException(
                    "Unable to initialize GLFW (make sure to run Java with \"-XstartOnFirstThread\" on macOS)");
        }
    }

    private void createGLFWWindow() throws RuntimeException {
        configureWindowSettings();
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Could not create the GLFW window");
        }
    }

    private static void configureWindowSettings() {
        glfwDefaultWindowHints(); // Window settings
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Don't stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // Don't allow resizing
        // For macOS
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
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

    // Make the window visible
    public void start() {
        glfwShowWindow(window);
        glfwFocusWindow(window);
    }

    // Free GLFW resources and destroy the window
    public void stop() {
        glfwFreeCallbacks(window);
//        glfwSetWindowShouldClose(window, true);
        glfwDestroyWindow(window);
        glfwTerminate();
        var oldCbFun = glfwSetErrorCallback(null);
        if (oldCbFun != null) oldCbFun.free();
    }

    // Game Loop Methods

    public void beginFrame() {
        glfwPollEvents();
        if (KeyInput.keyDown(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window, true); // Exit program by pressing escape
        }
    }

    public void render(Scene scene) {
        glClearColor(1f, 1f, 1f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear screen
        scene.render(null); // Don't pass G2D
        glfwSwapBuffers(window); // Swap color buffers
    }

    public void endFrame() {
        KeyInput.endFrame(); // Don't need, use key callback instead
        MouseInput.endFrame();
    }

    // Input Methods

    @Override
    public void setKeyInput(KeyInput keyboard) {
        glfwSetKeyCallback(window, keyboard::keyCallback);
    }

    @Override
    public void setMouseInput(MouseInput mouse) {
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
