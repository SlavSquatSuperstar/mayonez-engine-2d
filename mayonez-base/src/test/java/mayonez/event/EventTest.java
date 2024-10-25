package mayonez.event;

import org.junit.jupiter.api.*;

/**
 * Unit tests for the {@link mayonez.event} package.
 *
 * @author SlavSquatSuperstar
 */
class EventTest {

    @Test
    void broadcastEventTest() {
        var system = new EventSystem<>();
        EventListener<Event> l1 = e -> System.out.println("I can't believe that " + e.getMessage());
        EventListener<Event> l2 = e -> System.out.println("I just heard that " + e.getMessage());
        system.subscribe(l1);
        system.subscribe(l2);
        system.broadcast(new Event("you found 69,420 bugs in your code."));
    }

}
