package mayonez.input.keyboard

import mayonez.input.*
import java.awt.event.*

private const val INITIAL_NUM_KEYS = 64

/**
 * Receives and stores keyboard input events.
 *
 * @author SlavSquatSuperstar
 */
sealed class KeyManager : KeyAdapter() {

    // Key Fields
    private val keys: MutableMap<Int, InputState> = HashMap(INITIAL_NUM_KEYS) // All key states
    private val keysDown: MutableSet<Int> = HashSet(INITIAL_NUM_KEYS) // Keys down this frame

    internal var anyKeyDown: Boolean = false
        private set

    // Game Loop Methods

    /** Poll key events from the window. */
    fun updateKeys() {
        for ((key, value) in keys) {
            // Update key state
            val keyDown = key in keysDown
            val newState = value.getNextState(keyDown)
            keys[key] = newState
        }
        anyKeyDown = keysDown.isNotEmpty()
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

    // Key Getters and Setters

    protected fun setKeyDown(keyCode: Int, keyDown: Boolean) {
        if (keyDown) {
            keysDown.add(keyCode)
            // Track new key
            if (!keys.containsKey(keyCode)) keys[keyCode] = InputState.NONE
        } else {
            keysDown.remove(keyCode)
        }
    }

    protected fun keyDown(keyCode: Int): Boolean {
        return keyCode in keysDown
    }

    protected fun keyPressed(keyCode: Int): Boolean {
        return keys[keyCode] == InputState.PRESSED
    }

}