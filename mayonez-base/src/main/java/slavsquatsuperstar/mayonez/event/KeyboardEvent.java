package slavsquatsuperstar.mayonez.event;

import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature;

import java.awt.event.KeyEvent;

@ExperimentalFeature
public class KeyboardEvent extends Event {

    private boolean keyDown;
    private int action;
    private int keyCode, scancode, mods;

    /**
     * AWT constructor for a KeyboardEvent.
     *
     * @param keyCode the AWT code of the affected key
     * @param keyDown if the key is pressed or released
     * @param mods    which modifier keys are held down
     */
    public KeyboardEvent(int keyCode, boolean keyDown, int mods) {
        super(String.format("%s %s", KeyEvent.getKeyText(keyCode), keyDown ? "pressed" : "released"));
        this.keyCode = keyCode;
        this.keyDown = keyDown;
        this.mods = mods;
    }

    /**
     * GL constructor for a KeyboardEvent.
     *
     * @param key      the GLFW code of the affected key
     * @param scancode the platform-specific scancode
     * @param action   if the key is pressed, held, or released
     * @param mods     which modifier keys are held down
     */
    public KeyboardEvent(int key, int scancode, int action, int mods) {
        super(String.format("code=%d, action=%d, mods=%s", key, action, mods));
//        glfwGetKeyName(key, scancode); // key name from key code
        this.keyCode = key;
        this.scancode = scancode;
        this.action = action;
        this.mods = mods;
    }
}
