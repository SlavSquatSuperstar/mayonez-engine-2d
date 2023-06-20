package mayonez.input

import mayonez.*
import mayonez.event.*
import org.lwjgl.glfw.GLFW.*
import java.awt.event.*

/**
 * Receives keyboard input events.
 *
 * @author SlavSquatSuperstar
 */
// TODO GLFW sticky keys?
// TODO concurrent modification sometimes happens
@Suppress("unused")
class KeyInput internal constructor() : KeyAdapter() {

    // Key Fields
    private val keys: MutableMap<Int, MappingStatus?> = HashMap()

    // Game Loop Methods
    /** Poll key events from the window. */
    fun endFrame() { // TODO rename to pollKeys?
        for (key in keys.values) {
            when {
                key == null -> continue
                key.down -> key.updateIfDown()
                else -> key.setReleased()
            }
        }
    }

    // Keyboard Callbacks

    /**
     * The keyboard callback method for GLFW.
     *
     * @param window the window id
     * @param key the GLFW key code
     * @param scancode the platform-dependent key code
     * @param action the event type
     * @param mods any modifier keys
     */
    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        when (action) {
            // TODO GL double pressing still occurs
            GLFW_PRESS -> setKeyDown(key, true)
            GLFW_REPEAT -> setKeyDown(key, true)
            GLFW_RELEASE -> setKeyDown(key, false)
        }
        EventSystem.broadcast(KeyboardEvent(key, scancode, action, mods))
        endFrame()
    }

    override fun keyPressed(e: KeyEvent) { // Activates when ever a key is down
        setKeyDown(e.keyCode, true)
        EventSystem.broadcast(KeyboardEvent(e.keyCode, true, e.modifiersEx))
//        println("${KeyEvent.getModifiersExText(e.modifiersEx)}${e.keyLocation}")
    }

    override fun keyReleased(e: KeyEvent) {
        setKeyDown(e.keyCode, false)
        EventSystem.broadcast(KeyboardEvent(e.keyCode, false, e.modifiersEx))
    }

    // Keyboard Getters

    /**
     * Whether the user is continuously holding down the specified
     * [mayonez.input.Key].
     *
     * @param key a key enum constant
     * @return if the specified key is pressed
     */
    internal fun keyDown(key: Key?): Boolean {
        return when {
            key == null -> false
            Mayonez.useGL -> keyDown(key.glCode)
            else -> keyDown(key.awtCode)
        }
    }

    /**
     * Whether the user has started pressing the specified [mayonez.input.Key]
     * this frame.
     *
     * @param key a key enum constant
     * @return if the specified key is pressed
     */
    internal fun keyPressed(key: Key?): Boolean {
        return when {
            key == null -> false
            Mayonez.useGL -> keyPressed(key.glCode)
            else -> keyPressed(key.awtCode)
        }
    }

    // Helper Methods

    private fun setKeyDown(keyCode: Int, keyDown: Boolean) {
        val status = keys[keyCode]
        if (status == null) { // Track new key
            val newStatus = MappingStatus()
            newStatus.down = keyDown
            keys[keyCode] = newStatus
        } else {
            status.down = keyDown
        }
    }

    private fun keyDown(keyCode: Int): Boolean {
        return keys[keyCode]?.pressed == true || keys[keyCode]?.held == true
    }

    private fun keyPressed(keyCode: Int): Boolean {
        return keys[keyCode]?.pressed == true
    }

}