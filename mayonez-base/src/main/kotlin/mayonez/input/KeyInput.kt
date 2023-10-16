package mayonez.input

import mayonez.input.keyboard.*

/**
 * Receives keyboard input events.
 *
 * Usage: To query if a key is held, call [KeyInput.keyDown]. To query if a
 * key was just pressed this frame, call [KeyInput.keyPressed]. To query a
 * key axis, call [KeyInput.getAxis].
 *
 * @author SlavSquatSuperstar
 */
object KeyInput {

    // Key Fields
    private lateinit var instance: KeyManager

    @JvmStatic
    fun getInstance(): KeyManager = instance

    // Game Methods
    fun setUseGL(useGL: Boolean) {
        instance = if (useGL) GLKeyManager() else JKeyManager()
    }

    // Key Getters

    /**
     * Whether the user is continuously holding down the specified
     * [mayonez.input.Key].
     *
     * @param key a key enum constant
     * @return if the specified key is held
     */
    @JvmStatic
    fun keyDown(key: Key?): Boolean = instance.keyDown(key)

    /**
     * Whether the user has started pressing the specified [mayonez.input.Key]
     * this frame.
     *
     * @param key a key enum constant
     * @return if the specified key is pressed
     */
    @JvmStatic
    fun keyPressed(key: Key?): Boolean = instance.keyPressed(key)

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