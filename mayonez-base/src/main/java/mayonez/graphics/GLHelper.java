package mayonez.graphics;

import mayonez.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL.destroy;
import static org.lwjgl.opengl.GL11.*;

/**
 * Manages OpenGL capabilities and performs error checking and debugging.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLHelper {

    private GLHelper() {
    }

    // Capabilities

    /**
     * Loads the OpenGL library and creates the capabilities in the current thread.
     */
    public static void loadOpenGL() {
        Logger.debug("Creating OpenGL capabilities");
        createCapabilities();
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
        destroy();
    }

    // Draw Methods

    public static void clearScreen(float red, float green, float blue, float alpha) {
        glClearColor(red, green, blue, alpha);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear screen
    }

    public static void enableBlending() {
        // Note: complex transparent shapes don't work well, better to use textures
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    // Debug Methods

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
//        var location = Thread.currentThread().getStackTrace()[2];
//        Logger.log("Started error logging at: %s", location);
        clearGLErrors();
        glFunction.run();
        printGLErrors();
//        Logger.log("Finished error logging");
    }

}
