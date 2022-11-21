package mayonez.event;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link mayonez.event} package.
 *
 * @author SlavSquatSuperstar
 */
public class EventTests {

    @Test
    public void broadcastEventTest() {
        EventListener l1 = e -> System.out.println("I can't believe that " + e.getMessage());
        EventListener l2 = e -> System.out.println("I just heard that " + e.getMessage());
        EventSystem.subscribe(l1);
        EventSystem.subscribe(l2);
        EventSystem.broadcast(new Event("you found 69,420 bugs in your code."));
    }

}
