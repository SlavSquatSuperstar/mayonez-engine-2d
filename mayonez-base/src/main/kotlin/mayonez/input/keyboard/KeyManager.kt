package mayonez.input.keyboard

import mayonez.input.*
import java.awt.event.*

private const val INITIAL_NUM_KEYS = 64

/**
 * Receives and stores keyboard input events.
 *
 * @author SlavSquatSuperstar
 */
// TODO GLFW sticky keys?
// TODO concurrent modification sometimes happens
sealed class KeyManager : KeyAdapter() {

    // Key Fields
    private val keys: MutableMap<Int, MappingStatus?> = HashMap(INITIAL_NUM_KEYS)

    // Game Loop Methods

    /** Poll key events from the window. */
    fun updateKeys() {
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
    open fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
    }

    // Keyboard Getters

    /**
     * Whether the user is continuously holding down the specified
     * [mayonez.input.Key].
     *
     * @param key a key enum constant
     * @return if the specified key is pressed
     */
    internal abstract fun keyDown(key: Key?): Boolean

    /**
     * Whether the user has started pressing the specified [mayonez.input.Key]
     * this frame.
     *
     * @param key a key enum constant
     * @return if the specified key is pressed
     */
    internal abstract fun keyPressed(key: Key?): Boolean

    // Helper Methods

    protected fun setKeyDown(keyCode: Int, keyDown: Boolean) {
        val status = keys[keyCode]
        if (status == null) { // Track new key
            val newStatus = MappingStatus()
            newStatus.down = keyDown
            keys[keyCode] = newStatus
        } else {
            status.down = keyDown
        }
    }

    protected fun keyDown(keyCode: Int): Boolean {
        return keys[keyCode]?.pressed == true || keys[keyCode]?.held == true
    }

    protected fun keyPressed(keyCode: Int): Boolean {
        return keys[keyCode]?.pressed == true
    }

}