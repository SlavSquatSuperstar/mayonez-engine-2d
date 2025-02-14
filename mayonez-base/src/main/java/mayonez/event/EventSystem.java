package mayonez.event;

import java.util.*;

/**
 * A common node that links multiple event listeners and event generators.
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
    public void subscribe(EventListener<T> l) {
        listeners.add(l);
    }

    /**
     * Unsubscribes a listener to all events passed through this event system.
     *
     * @param l the event observer
     */
    public void unsubscribe(EventListener<T> l) {
        listeners.remove(l);
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
    public void broadcast(T e) {
        listeners.forEach(l -> l.onEvent(e));
    }

}
