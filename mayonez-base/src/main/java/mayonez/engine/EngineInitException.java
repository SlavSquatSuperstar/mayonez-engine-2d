package mayonez.engine;

/**
 * An exception occurring when the game engine or its frameworks cannot be
 * initialized properly. This exception type is unchecked and must be handled,
 * as it could result in a fatal crash otherwise.
 *
 * @author SlavSquatSuperstar
 */
public class EngineInitException extends Exception {
    EngineInitException(String message) {
        super(message);
    }
}
