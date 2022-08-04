package slavsquatsuperstar.mayonez.event;

/**
 * Maps an object to a function which can be stored and executed later.
 *
 * @author SlavSquatSuperstar
 */
@FunctionalInterface
public interface Receivable {
    void onReceive(Object... args);
}
