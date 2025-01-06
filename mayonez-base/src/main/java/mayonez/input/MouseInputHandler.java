package mayonez.input;

import mayonez.event.*;
import mayonez.input.events.*;

/**
 * Receives mouse input events from the window and passes them to
 * {@link mayonez.input.MouseInput}.
 *
 * @author SlavSquatSuperstar
 */
public interface MouseInputHandler {

    /**
     * The event system that this handler forwards keyboard events to.
     *
     * @return the event system
     */
    EventSystem<MouseInputEvent> getEventSystem();

    /**
     * Maps a physical button to its correct button code.
     *
     * @param button the button
     * @return the button code
     */
    int getButtonCode(Button button);

}
