package mayonez.application;

import mayonez.event.*;

/**
 * Tracks window events produced by the window.
 *
 * @author SlavSquatSuperstar
 */
public final class WindowEvents {

    public static final EventSystem<WindowResizeEvent> WINDOW_EVENTS;

    static {
        WINDOW_EVENTS = new EventSystem<>();
    }

    private WindowEvents() {
    }

}
