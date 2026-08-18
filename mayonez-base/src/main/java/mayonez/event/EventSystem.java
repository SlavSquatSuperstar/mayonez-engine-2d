package mayonez.event;

import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * A network of objects that can send or receive {@link Event}s. Events are
 * broadcasted to multiple subscribed {@link EventListener}s that execute
 * asynchronous and uncoupled behavior.
 *
 * @param <T> the type of event to send
 * @author SlavSquatSuperstar
 */
public class EventSystem<T extends Event> {

    private final List<EventListener<T>> listeners = new ArrayList<>();

    /**
     * Subscribes a listener to all events passed through this event system.
     *
     * @param l the event observer
     */
    public void subscribe(@Nullable EventListener<T> l) {
        if (l != null) listeners.add(l);
    }

    /**
     * Unsubscribes a listener to all events passed through this event system.
     *
     * @param l the event observer
     */
    public void unsubscribe(@Nullable EventListener<T> l) {
        if (l != null) listeners.remove(l);
    }

    /**
     * Unsubscribes all listeners subscribed to this event system.
     */
    public void unsubscribeAll() {
        listeners.clear();
    }

    /**
     * Notifies all subscribed listeners of an event.
     *
     * @param e the event data
     */
    public void broadcast(@Nullable T e) {
        if (e != null) listeners.forEach(l -> l.onEvent(e));
    }

}
