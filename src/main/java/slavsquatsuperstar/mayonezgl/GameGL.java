package slavsquatsuperstar.mayonezgl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Time;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public class GameGL { // can't implement runnable otherwise GLFW will crash

    // Singleton Fields
//    public static GameGL game;

    // Thread Fields
    private boolean running = false;

    // Window Parameters
    private long window; // The window handle
    private String title;
    private int width, height;

    public GameGL(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        init();
    }

    // Game Loop Methods

    public static void main(String[] args) {
        new GameGL("Mayonez + LWJGL", 300, 300).start();
    }

//    public static GameGL getInstance() {
//        return game;
//    }

    /**
     * Initialize GLFW window and features.
     * <p>
     * Source: https://www.lwjgl.org/guide
     */
    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Engine: Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // window settings
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Engine: Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

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
    }

    public void start() {
        if (running) // Don't start the game if already running
            return;

        Logger.log("Engine: Starting");
        Logger.log("Welcome to %s %s", Preferences.TITLE, Preferences.VERSION);
        running = true;

        glfwShowWindow(window); // Make the window visible
        run();
    }

    public void stop(int status) {
        if (!running)
            return;

        Logger.log("Engine: Stopping with exit code %d", status);

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        Logger.printExitMessage();
        System.exit(status);
    }

    public void run() {
        // All time values are in seconds
        float lastTime = 0; // Last time the game loop iterated
        float deltaTime = 0; // Time since last frame

        // For rendering
        boolean ticked = false; // Has engine actually updated?

        // For debugging
        float timer = 0;
        int frames = 0;

        try {
            // Render to the screen until the user closes the window or pressed the ESCAPE key
            while (!glfwWindowShouldClose(window)) {
                float currentFrameTime = Time.getTime(); // Time for current frame
                float passedTime = currentFrameTime - lastTime;
                deltaTime += passedTime;
                timer += passedTime;
                lastTime = currentFrameTime; // Reset lastTime

                glClearColor(1.0f, 0.0f, 0.0f, 0.0f); // Set the clear color

                while (deltaTime >= Time.TIME_STEP) {
                    glfwPollEvents(); // Reset and poll window events
                    update(deltaTime);
                    deltaTime -= Time.TIME_STEP;
                    ticked = true;
                }
                // Only render if the game has updated to save resources
                if (ticked) {
                    render();
                    frames++;
                    ticked = false;
                }
                // Print ticks and frames each second
                if (timer >= 1) {
                    Logger.log("Frames per Second: %d", frames);
                    frames = 0;
                    timer = 0;
                }
            }
        } catch (Exception e) {
            Logger.log(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
            stop(1);
        }

        stop(0);
    }

    public void update(float dt) {
        // update scene
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glfwSwapBuffers(window); // swap the color buffers
    }
}