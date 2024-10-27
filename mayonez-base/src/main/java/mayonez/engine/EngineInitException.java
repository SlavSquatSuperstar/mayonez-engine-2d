package mayonez.engine;

/**
 * An exception occurring when the game engine cannot be initialized properly.
 *
 * @author SlavSquatSuperstar
 */
public class EngineInitException extends RuntimeException {
    EngineInitException(String message) {
        super(message);
    }
}
