package mayonez.input.keyboard

import mayonez.event.*
import mayonez.graphics.*
import mayonez.input.*
import mayonez.input.events.*
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
internal class GLKeyManager : KeyManager(), KeyInputHandler {

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
        val keyDown = when (action) {
            GLFW.GLFW_PRESS -> true
            GLFW.GLFW_RELEASE -> false
            // According to docs, GLFW_REPEAT should not be used if multiple keys are held
            else -> return
        }
        eventSystem.broadcast(KeyInputEvent(key, keyDown))
    }

    // Event Handler Overrides

    override fun getEventSystem(): EventSystem<KeyInputEvent> {
        return InputEvents.KEYBOARD_EVENTS
    }

    override fun getKeyCode(key: Key): Int = key.glCode

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