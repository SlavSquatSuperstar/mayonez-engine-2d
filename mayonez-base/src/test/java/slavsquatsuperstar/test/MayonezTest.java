package slavsquatsuperstar.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Driver code for new features in the engine.
 *
 * @author SlavSquatSuperstar
 */
public class MayonezTest {

    public static void main(String[] args) throws Exception {
        EventListener l1 = e -> System.out.println("I can't believe that " + e.getMessage());
        EventListener l2 = e -> System.out.println("I just heard that " + e.getMessage());
        EventSystem.subscribe(l1);
        EventSystem.subscribe(l2);
        EventSystem.broadcast(new Event("you found 69,420 bugs in your code."));
    }

    static class EventSystem {

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

    interface EventListener {
        void onEvent(Event event);
    }

    static class Event {
        private final String msg;

        public Event(String msg) {
            this.msg = msg;
        }

        public String getMessage() {
            return msg;
        }

        @Override
        public String toString() {
            return String.format("%s (%s)",
                    getClass().isAnonymousClass() ? "Event" : getClass().getSimpleName(), msg);
        }
    }


}
