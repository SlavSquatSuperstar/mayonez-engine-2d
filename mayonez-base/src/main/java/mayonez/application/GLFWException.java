package mayonez.application;

import mayonez.graphics.*;

/**
 * An exception occurring when LWJGL cannot create the GLFW window. This type of
 * exception is checked and must be handled.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class GLFWException extends Exception {
    GLFWException(String message) {
        super(message);
    }
}
