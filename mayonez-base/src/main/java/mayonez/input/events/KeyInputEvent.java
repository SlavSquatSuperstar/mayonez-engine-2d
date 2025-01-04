package mayonez.input.events;

import mayonez.event.*;

/**
 * Indicates a key has been pressed or released.
 *
 * @author SlavSquatSuperstar
 */
public class KeyInputEvent extends Event {

    private final int keyCode;
    private final boolean keyDown;

    public KeyInputEvent(int keyCode, boolean keyDown) {
        super("Key %d is down: %s".formatted(keyCode, keyDown));
        this.keyCode = keyCode;
        this.keyDown = keyDown;
    }

    /**
     * The internal key code associated with this event.
     *
     * @return the key code
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Whether the key was pressed or released in this event.
     *
     * @return if the key is down
     */
    public boolean isKeyDown() {
        return keyDown;
    }

}
