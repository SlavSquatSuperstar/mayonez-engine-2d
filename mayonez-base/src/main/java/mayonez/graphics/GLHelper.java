package mayonez.graphics;

import mayonez.*;
import mayonez.application.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

/**
 * Manages OpenGL capabilities and performs error checking and debugging.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.GL)
public final class GLHelper {

    private GLHelper() {
    }

    // Capabilities

    /**
     * Loads the OpenGL library and creates the capabilities in the current thread.
     * Requires an active GLFW window.
     *
     * @throws IllegalStateException if no GLFW window is active
     */
    public static void loadOpenGL() throws IllegalStateException {
        Logger.debug("Creating OpenGL capabilities");
        GL.createCapabilities();
        printGLInfo();
        enableBlending();
    }

    /**
     * Checks if the OpenGL capabilities have been created in the current thread.
     *
     * @return if the GL library is loaded
     */
    public static boolean isGLInitialized() {
        try {
            GL.getCapabilities();
            return true;
        } catch (IllegalStateException e) {
            return false; // GL not initialized
        }
    }

    /**
     * Unloads the OpenGL library and destroys the capabilities in the current thread.
     */
    public static void unloadOpenGL() {
        Logger.debug("Destroying OpenGL capabilities");
        GL.destroy();
    }

    // Draw Methods

    public static void clearScreen(float red, float green, float blue, float alpha) {
        glClearColor(red, green, blue, alpha);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear screen
    }

    public static void enableBlending() {
        // Note: complex transparent shapes don't work well, better to use textures
        // Can be solved with on-demand triangle fans
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        // Blending computes a weighted average of two colors
    }

    // Version Methods

    // TODO allow specify log level

    /**
     * Prints the OpenGL/GLSL version number and graphics card manufacturer of
     * the current device.
     */
    public static void printGLInfo() {
        Logger.debug("OpenGL Version: " + glGetString(GL_VERSION));
        Logger.debug("GLSL Version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));
        Logger.debug("OpenGL Renderer: " + glGetString(GL_RENDERER));
        Logger.debug("OpenGL Vendor: " + glGetString(GL_VENDOR));
    }

    // Error Logging Methods

    /**
     * Clears all accumulated OpenGL error codes.
     */
    public static void clearGLErrors() {
        while (glGetError() != GL_NO_ERROR) ;
    }

    /**
     * Prints all accumulated OpenGL error codes to the console.
     */
    public static void printGLErrors() {
        int error;
        do {
            error = glGetError();
            if (error == GL_NO_ERROR) Logger.log("OpenGL Error: (none)");
            else Logger.error("OpenGL Error: 0x%x", error);
        } while (error != GL_NO_ERROR);
    }

    /**
     * Runs a OpenGL function with error logging and prints the source.
     *
     * @param glFunction the function to run
     */
    public static void runWithErrorLogging(Runnable glFunction) {
        var location = Thread.currentThread().getStackTrace()[2];
        Logger.log("Started OpenGL error logging at: %s", location);
        clearGLErrors();
        glFunction.run();
        printGLErrors();
        Logger.log("Finished OpenGL error logging");
    }

}
