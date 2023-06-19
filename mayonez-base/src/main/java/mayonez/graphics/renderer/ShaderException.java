package mayonez.graphics.renderer;

import mayonez.annotations.*;

/**
 * An exception occurring when the program cannot successfully parse or
 * compile a shader file.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class ShaderException extends RuntimeException {
    ShaderException(String message) {
        super(message);
    }

    ShaderException(Throwable cause) {
        super(cause);
    }
}
