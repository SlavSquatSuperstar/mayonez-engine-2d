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
//        EventListener l1 = e -> System.out.println("I can't believe that " + e.getMessage());
//        EventListener l2 = e -> System.out.println("I just heard that " + e.getMessage());
//        EventSystem.subscribe(l1);
//        EventSystem.subscribe(l2);
//        EventSystem.broadcast(new Event("you found 69,420 bugs in your code."));

//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("hello");
//        strings.add("there");
//        strings.add(null);
//        strings.add("general");
//        strings.add("kenobi");
//        strings.add(null);
//        strings.forEach(System.out::println);

        int size = 5;
        int i = 3;
        System.out.println("i = " + i);
        int next = (i + 1) % size;
        System.out.println("next = " + next);

        i = 4;
        System.out.println("i = " + i);
        int prev = (i - 1 + size) % size;
        System.out.println("prev = " +  prev);
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

    interface EventListener<T extends Event> {
        void onEvent(T event);
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
