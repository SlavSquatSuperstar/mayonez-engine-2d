package mayonez.event;

import mayonez.annotations.*;

/**
 * An object that receives and reacts to events.
 *
 * @param <T> the type of event to listen to
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
@FunctionalInterface
// TODO parameterize listener and nodes
public interface EventListener<T extends Event> {

    /**
     * Called when this object receives an event.
     *
     * @param event the event data
     */
    void onEvent(T event);

}
