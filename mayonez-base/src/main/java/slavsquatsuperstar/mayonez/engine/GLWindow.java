package slavsquatsuperstar.mayonez.engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.graphics.renderer.Renderable;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class GLWindow implements Window {

    private long window; // The window pointer
    private final String title;
    private final int width, height;

//    private KeyInput keyboard;
//    private MouseInput mouse;

    public GLWindow(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        init();
    }

    // Engine methods

    @Override
    public boolean notClosedByUser() {
        return !glfwWindowShouldClose(window);
    }

    /**
     * Initialize GLFW window and features.
     * <br>
     * Source: https://www.lwjgl.org/guide
     */
    private void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        try {
            if (!glfwInit()) throw new IllegalStateException("Engine: Unable to initialize GLFW");
        } catch (IllegalStateException | ExceptionInInitializerError e) {
            Logger.warn("Window: LWJGL must be run with \"-XstartOnFirstThread\" on macOS");
            Mayonez.stop(1);
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // window settings
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
        // for mac
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);

        // Create the window
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) throw new RuntimeException("Engine: Failed to create the GLFW window");

        // Add input listeners

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        glfwMakeContextCurrent(window); // Make the OpenGL context current
        glfwSwapInterval(1); // Enable v-sync

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
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
        glfwSetErrorCallback(null).free();
    }

    // Game Loop Methods

    public void beginFrame() {
        glfwPollEvents();
        if (KeyInput.keyDown(GLFW_KEY_ESCAPE))
            glfwSetWindowShouldClose(window, true); // Exit program by pressing escape
    }

    public void render(Renderable r) {
        glClearColor(1f, 1f, 1f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the screen
        r.draw(null); // don't pass G2D
        glfwSwapBuffers(window); // swap the color buffers
    }

    public void endFrame() {
        KeyInput.endFrame(); // don't need, use key callback instead
        MouseInput.endFrame();
    }

    // Input Methods

    @Override
    public void setKeyInput(KeyInput keyboard) {
//        this.keyboard = keyboard;
        glfwSetKeyCallback(window, KeyInput::keyCallback);
    }

    @Override
    public void setMouseInput(MouseInput mouse) {
//        this.mouse = mouse;
        glfwSetMouseButtonCallback(window, MouseInput::mouseButtonCallback);
        glfwSetCursorPosCallback(window, MouseInput::mousePosCallback);
        glfwSetScrollCallback(window, MouseInput::mouseScrollCallback);
    }

    // Properties

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
}
