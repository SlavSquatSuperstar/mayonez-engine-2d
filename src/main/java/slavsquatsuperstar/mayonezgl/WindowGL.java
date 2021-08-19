package slavsquatsuperstar.mayonezgl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import slavsquatsuperstar.mayonez.Logger;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowGL {

    private long window; // The window handle
    private String title;
    private int width, height;

    public WindowGL(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        init();
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(window);
    }

    // Game Loop methods

    /**
     * Initialize GLFW window and features.
     * <p>
     * Source: https://www.lwjgl.org/guide
     */
    private void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
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
        glfwSetKeyCallback(window, KeyInputGL::keyCallback);
//        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
//            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
//                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
//        });

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

    // Make the window visible
    public void start() {
        glfwShowWindow(window);
        glfwFocusWindow(window);
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f); // Set the clear color
    }

    // Free GLFW resources and destroy the window
    public void stop() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    // Reset and poll window events
    public void update() {
        glfwPollEvents();
        if (KeyInputGL.isKeyHeld(GLFW_KEY_ESCAPE))
            glfwSetWindowShouldClose(window, true); // Exit program by pressing escape
        if (KeyInputGL.isKeyPressed(GLFW_KEY_W))
            Logger.log("Begin W");
        if (KeyInputGL.isKeyHeld(GLFW_KEY_W))
            Logger.log("W");
        KeyInputGL.endFrame();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glfwSwapBuffers(window); // swap the color buffers
    }

}
