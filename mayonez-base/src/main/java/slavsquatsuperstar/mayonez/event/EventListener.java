package slavsquatsuperstar.mayonez.event;

import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature;

/**
 * An object that receives and reacts to events.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
@FunctionalInterface
public interface EventListener<T extends Event> {
    void onEvent(T event);
}
