package mayonez.application;

import mayonez.event.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.input.events.*;
import mayonez.input.keyboard.*;
import org.lwjgl.glfw.GLFW;

/**
 * Receives keyboard input events from GLFW.
 * <p>
 * Source: <a href="https://www.glfw.org/docs/latest/input_guide.html#input_key">
 * GLFW Input Guide § Key Input</a>
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class GLKeyManager implements KeyInputHandler {

    // Key Callbacks

    /**
     * The keyboard callback method for GLFW.
     *
     * @param window   the window id
     * @param key      the GLFW key code
     * @param scancode the platform-dependent key code
     * @param action   the event type
     * @param mods     any modifier keys
     */
    @SuppressWarnings("unused")
    void keyCallback(long window, int key, int scancode, int action, int mods) {
        switch (action) {
            case GLFW.GLFW_PRESS -> getEventSystem().broadcast(new KeyInputEvent(key, true));
            case GLFW.GLFW_RELEASE -> getEventSystem().broadcast(new KeyInputEvent(key, false));
            // According to docs, GLFW_REPEAT should not be used if multiple keys are held
        }
    }

    // Input Handler Overrides

    @Override
    public EventSystem<KeyInputEvent> getEventSystem() {
        return InputEvents.KEYBOARD_EVENTS;
    }

    @Override
    public int getKeyCode(Key key) {
        return key.getGlCode();
    }

}
