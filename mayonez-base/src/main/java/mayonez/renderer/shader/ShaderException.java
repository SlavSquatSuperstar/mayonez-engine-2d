package mayonez.renderer.shader;

import mayonez.application.*;

/**
 * An exception occurring when the program cannot successfully parse or
 * compile a shader file.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.GL)
class ShaderException extends RuntimeException {
    ShaderException(String message) {
        super(message);
    }

    ShaderException(Throwable cause) {
        super(cause);
    }
}
