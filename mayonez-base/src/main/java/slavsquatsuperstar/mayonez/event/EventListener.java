package slavsquatsuperstar.mayonez.event;

/**
 * An object that receives and reacts to events.
 *
 * @author SlavSquatSuperstar
 */
@FunctionalInterface
public interface EventListener<T extends Event> {
    void onEvent(T event);
}
