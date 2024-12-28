package mayonez.event;

import mayonez.annotations.*;

/**
 * A collection of global event systems usable by the application.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
// TODO subscribe to event types and check parameter before onEvent
public final class Events {

    public static final EventSystem<KeyboardEvent> KEYBOARD_EVENTS;

    static {
        KEYBOARD_EVENTS = new EventSystem<>();
    }

    private Events() {
    }

}
