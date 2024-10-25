package mayonez.engine;

import mayonez.graphics.*;

/**
 * An exception occurring when LWJGL cannot create the GLFW window.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class GLFWException extends RuntimeException {
    GLFWException(String message) {
        super(message);
    }
}
