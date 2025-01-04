package mayonez.input.events;

import mayonez.event.*;

/**
 * Tracks input events produced by the window.
 *
 * @author SlavSquatSuperstar
 */
public final class InputEvents {

    public static final EventSystem<KeyInputEvent> KEYBOARD_EVENTS;

    static {
        KEYBOARD_EVENTS = new EventSystem<>();
    }

    private InputEvents() {
    }

}
