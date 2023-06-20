package mayonez.input

import mayonez.*
import mayonez.event.*
import mayonez.util.*
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
object KeyInput : KeyAdapter() {

    // Key Fields
    private val keys: MutableMap<Int, MappingStatus?> = HashMap()

    // Game Loop Methods
    /** Poll key events from the window. */
    @JvmStatic
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
     * Whether the user is continuously holding down the specified [Key].
     *
     * @param key a Key enum constant
     * @return if the specified key is pressed
     */
    @JvmStatic
    fun keyDown(key: Key?): Boolean {
        return when {
            key == null -> false
            Mayonez.useGL -> keyDown(key.glCode)
            else -> keyDown(key.awtCode)
        }
    }

    /**
     * Whether the user has started pressing the specified [Key] this frame.
     *
     * @param key a Key enum constant
     * @return if the specified key is pressed
     */
    @JvmStatic
    fun keyPressed(key: Key?): Boolean {
        return when {
            key == null -> false
            Mayonez.useGL -> keyPressed(key.glCode)
            else -> keyPressed(key.awtCode)
        }
    }

    /**
     * Whether the user has started pressing the [Key] with the specified name
     * this frame.
     *
     * @param keyName the name of the key
     * @return if the specified key is pressed
     */
    @JvmStatic
    fun keyDown(keyName: String): Boolean {
        return keyDown(Key.findWithName(keyName))
    }

    /**
     * Whether the user is continuously holding down the [Key] with the
     * specified name.
     *
     * @param keyName the name of the key
     * @return if the specified key is pressed
     */
    @JvmStatic
    fun keyPressed(keyName: String): Boolean {
        return keyPressed(Key.findWithName(keyName))
    }

    /**
     * Get the value of the [mayonez.input.KeyAxis] with the specified name, an
     * integer between -1 and 1.
     *
     * @param axisName the name of the axis
     * @return the axis value
     */
    @JvmStatic
    fun getAxis(axisName: String): Int {
        return StringUtils.findConstantWithName(KeyAxis.values(), axisName)
            ?.value() ?: 0
    }

    // Helper Methods

    private fun setKeyDown(keyCode: Int, keyDown: Boolean) {
        val status = keys[keyCode]
        if (status == null) { // Track new key
            val newStatus = MappingStatus();
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