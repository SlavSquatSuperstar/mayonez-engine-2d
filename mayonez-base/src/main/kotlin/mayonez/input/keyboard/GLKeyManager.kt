package mayonez.input.keyboard

import mayonez.event.*
import mayonez.graphics.*
import mayonez.input.*
import org.lwjgl.glfw.GLFW

/**
 * Receives keyboard input events from GLFW.
 *
 * Source: [GLFW Input Guide § Key Input](https://www.glfw.org/docs/latest/input_guide.html#input_key)
 *
 * @author SlavSquatSuperstar
 */
// TODO GLFW sticky keys?
@UsesEngine(EngineType.GL)
internal class GLKeyManager : KeyManager() {

    // Key Callbacks

    /**
     * The keyboard callback method for GLFW.
     *
     * @param window the window id
     * @param key the GLFW key code
     * @param scancode the platform-dependent key code
     * @param action the event type
     * @param mods any modifier keys
     */
    override fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        when (action) {
            GLFW.GLFW_PRESS, GLFW.GLFW_REPEAT -> setKeyDown(key, true)
            // According to docs, GLFW_REPEAT should not be relied upon
            GLFW.GLFW_RELEASE -> setKeyDown(key, false)
        }
        Events.KEYBOARD_EVENTS.broadcast(KeyboardEvent(key, scancode, action, mods))
    }

    // Key Getters

    override fun keyDown(key: Key?): Boolean {
        return if (key == null) false
        else keyDown(key.glCode)
    }

    override fun keyPressed(key: Key?): Boolean {
        return if (key == null) false
        else keyPressed(key.glCode)
    }

}