package mayonez.event;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.event} package.
 *
 * @author SlavSquatSuperstar
 */
class EventTest {

    @Test
    void broadcastEventIsReceived() {
        var system = new EventSystem<>();
        var list1 = new TestEventListener();
        var list2 = new TestEventListener();

        system.subscribe(list1);
        system.subscribe(list2);
        system.broadcast(new Event());

        assertTrue(list1.hasReceivedEvent());
        assertTrue(list2.hasReceivedEvent());
    }

    private static class TestEventListener implements EventListener<Event> {
        private boolean receivedEvent = false;

        @Override
        public void onEvent(Event event) {
            receivedEvent = true;
        }

        public boolean hasReceivedEvent() {
            return receivedEvent;
        }
    }

}
