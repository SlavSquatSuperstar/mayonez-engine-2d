package mayonez.application;

import mayonez.event.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.input.events.*;

import java.awt.event.*;

/**
 * Receives keyboard input events from AWT.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
class JKeyManager extends KeyAdapter implements KeyInputHandler {

    // Key Callbacks

    @Override
    public void keyPressed(KeyEvent e) {
        getEventSystem().broadcast(new KeyInputEvent(e.getKeyCode(), true));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        getEventSystem().broadcast(new KeyInputEvent(e.getKeyCode(), false));
    }

    // Input Handler Overrides

    @Override
    public EventSystem<KeyInputEvent> getEventSystem() {
        return InputEvents.KEYBOARD_EVENTS;
    }

    @Override
    public int getKeyCode(Key key) {
        return key.getAwtCode();
    }

}
