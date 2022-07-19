package slavsquatsuperstar.mayonez.event;

import java.awt.event.KeyEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_UNKNOWN;
import static org.lwjgl.glfw.GLFW.glfwGetKeyName;

public class KeyboardEvent extends Event {

    private boolean keyDown;
    private int action;
    private int keyCode, mods;

    /**
     * AWT constructor for a KeyBoardEvent
     *
     * @param keyCode which key was affected
     * @param keyDown if the key is pressed or released
     * @param mods    which modifier keys are held down
     */
    public KeyboardEvent(int keyCode, boolean keyDown, int mods) {
        super(String.format("%s %s", KeyEvent.getKeyText(keyCode), keyDown ? "pressed" : "released"));
        this.keyCode = keyCode;
        this.keyDown = keyDown;
        this.mods = mods;
    }

    public KeyboardEvent(int scancode, int action, int mods) {
        super(String.format("code=%d, action=%d, mods=%s", scancode, action, mods));
        glfwGetKeyName(GLFW_KEY_UNKNOWN, scancode);
        this.keyCode = scancode;
        this.action = action;
        this.mods = mods;
    }
}
