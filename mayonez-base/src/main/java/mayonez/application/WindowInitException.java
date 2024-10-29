package mayonez.application;

/**
 * An exception occurring when the program cannot properly initialize the window
 * or its frameworks. This exception type is unchecked and must be handled,
 * as it could result in a fatal crash otherwise.
 *
 * @author SlavSquatSuperstar
 */
public class WindowInitException extends Exception {
    WindowInitException(String message) {
        super(message);
    }
}
