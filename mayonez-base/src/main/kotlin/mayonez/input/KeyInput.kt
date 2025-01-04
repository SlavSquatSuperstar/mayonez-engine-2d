package mayonez.input

import mayonez.input.keyboard.*

/**
 * Receives and stores keyboard input events from the window.
 *
 * Usage: To query if a key is held, call [KeyInput.keyDown]. To query if a
 * key was just pressed this frame, call [KeyInput.keyPressed]. To query a
 * key axis, call [KeyInput.getAxis].
 *
 * @author SlavSquatSuperstar
 */
object KeyInput {

    // Singleton Properties
    private lateinit var instance: KeyManager

    // Constants
    // Around 80 keys on a US ANSI keyboard, round down to 2^6
    private const val INITIAL_NUM_KEYS = 64

    // Key Fields
    private val keys: MutableMap<Int, InputState> = HashMap(INITIAL_NUM_KEYS) // All key states
    private val keysDown: MutableSet<Int> = HashSet(INITIAL_NUM_KEYS) // Keys down this frame
    private var anyKeyDown: Boolean = false

    // Event Fields
    private var handler: KeyInputHandler? = null

    /**
     * Create the [KeyManager] instance if it does not exist.
     *
     * @param useGL whether to use GLFW instead of AWT.
     * @return the key input instance
     */
    internal fun createInstance(useGL: Boolean): KeyManager {
        if (this::instance.isInitialized) return instance
        instance = if (useGL) GLKeyManager() else JKeyManager()
        return instance
    }

    /**
     * Set the key event generator instance for the application.
     *
     * @param handler the key input handler
     */
    @JvmStatic
    fun setHandler(handler: KeyInputHandler) {
        // Replace event generator
        this.handler?.eventSystem?.unsubscribeAll()
        this.handler = handler
        handler.eventSystem?.subscribe { event ->
            onKeyInputEvent(event.keyCode, event.isKeyDown)
        }
    }

    private fun onKeyInputEvent(keyCode: Int, keyDown: Boolean) {
        if (keyDown) {
            keysDown.add(keyCode)
            // Track new key
            if (!keys.containsKey(keyCode)) keys[keyCode] = InputState.NONE
        } else {
            keysDown.remove(keyCode)
        }
    }

    /**
     * Update input states for all keys.
     */
    @JvmStatic
    fun updateKeys() {
        for ((key, value) in keys) {
            // Update key state
            val keyDown = key in keysDown
            val newState = value.getNextState(keyDown)
            keys[key] = newState
        }
        anyKeyDown = keysDown.isNotEmpty()
    }

    /**
     * Whether the user is continuously holding down the specified
     * [mayonez.input.Key].
     *
     * @param key a key enum constant
     * @return if the specified key is held
     */
    @JvmStatic
    fun keyDown(key: Key?): Boolean {
        return if (key == null) false
        else if (handler == null) false
        else keyDown(handler!!.getKeyCode(key))
    }

    /**
     * Whether the user has started pressing the specified [mayonez.input.Key]
     * this frame.
     *
     * @param key a key enum constant
     * @return if the specified key is pressed
     */
    @JvmStatic
    fun keyPressed(key: Key?): Boolean {
        return if (key == null) false
        else if (handler == null) false
        else keyPressed(handler!!.getKeyCode(key))
    }

    /**
     * Whether the user is continuously holding down the [mayonez.input.Key]
     * with the specified name.
     *
     * @param keyName the name of the key
     * @return if the specified key is held
     */
    @JvmStatic
    fun keyDown(keyName: String): Boolean = keyDown(Key.findWithName(keyName))

    /**
     * Whether the user has started pressing the [mayonez.input.Key] with the
     * specified name this frame.
     *
     * @param keyName the name of the key
     * @return if the specified key is pressed
     */
    @JvmStatic
    fun keyPressed(keyName: String): Boolean = keyPressed(Key.findWithName(keyName))

    @JvmStatic
    fun keyDown(keyCode: Int): Boolean = keyCode in keysDown

    @JvmStatic
    fun keyPressed(keyCode: Int): Boolean = (keys[keyCode] == InputState.PRESSED)

    /**
     * If any keyboard keys are held down.
     *
     * @return if the keyboard is pressed
     */
    @JvmStatic
    fun isAnyDown(): Boolean = anyKeyDown

    // Key Axis Methods

    /**
     * Get the value of the specified [mayonez.input.InputAxis].
     *
     * @param axis an axis enum constant
     * @return the axis value, either -1, 0, or 1
     */
    @JvmStatic
    fun getAxis(axis: InputAxis?): Int = axis?.value() ?: 0

    /**
     * Get the value of the [mayonez.input.DefaultKeyAxis] with the specified
     * name.
     *
     * @param axisName the name of the axis
     * @return the axis value, either -1, 0, or 1
     */
    @JvmStatic
    fun getAxis(axisName: String): Int = getAxis(DefaultKeyAxis.findWithName(axisName))

}