package mayonez.event;

/**
 * Maps an object to an anonymous function which can be stored and executed later.
 *
 * @author SlavSquatSuperstar
 */
@FunctionalInterface
public interface Receivable {
    void onReceive(Object... args);
}
