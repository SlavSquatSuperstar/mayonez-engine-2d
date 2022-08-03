package slavsquatsuperstar.mayonez.event;

import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * The central node that links event listeners and event generators.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
// TODO multiple event nodes
public final class EventSystem {

    private final static List<EventListener> listeners = new ArrayList<>();

    private EventSystem() {
    }

    public static void subscribe(EventListener l) {
        listeners.add(l);
    }

    public static void broadcast(Event e) {
        listeners.forEach(l -> l.onEvent(e));
    }

}
