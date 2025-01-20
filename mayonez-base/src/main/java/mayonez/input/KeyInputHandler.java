package mayonez.input;

import mayonez.event.*;
import mayonez.input.events.*;

/**
 * Receives keyboard input events from the window and passes them to
 * {@link mayonez.input.KeyInput}.
 *
 * @author SlavSquatSuperstar
 */
public interface KeyInputHandler {

    /**
     * The event system that this handler forwards keyboard events to.
     *
     * @return the event system
     */
    EventSystem<KeyInputEvent> getEventSystem();

    /**
     * Maps a physical key to its correct key code.
     *
     * @param key the key
     * @return the key code
     */
    int getKeyCode(Key key);

}
