package mayonez.graphics;

import mayonez.*;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;

/**
 * Performs error checking for OpenGL.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLErrorHelper {

    private GLErrorHelper() {
    }

    /**
     * Clears all accumulated OpenGL error codes.
     */
    public static void clearGLErrors() {
        while (glGetError() != GL_NO_ERROR);
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
