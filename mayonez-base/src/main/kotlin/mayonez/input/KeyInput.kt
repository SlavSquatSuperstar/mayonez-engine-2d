package mayonez.input

import mayonez.*
import mayonez.event.*
import org.lwjgl.glfw.GLFW.*
import java.awt.event.*

/**
 * The receiver for all keyboard-related input events.
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
    /** Update key inputs. */
    @JvmStatic
    fun endFrame() { // TODO rename to pollKeys?
        for (mapping in keys.values) {
            if (mapping == null) continue;

            if (mapping.down) {
                if (mapping.released) {
                    mapping.setPressed()
                } else if (mapping.pressed) { // Continued key press
                    mapping.setHeld()
                }
            } else {
                mapping.setReleased()
            }
        }
    }

    // Keyboard Callbacks

    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
//        println(glfwGetKeyName(GLFW_KEY_UNKNOWN, scancode))
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

    @JvmStatic
    fun keyDown(keyCode: Int): Boolean {
        return keys[keyCode]?.pressed == true || keys[keyCode]?.held == true
    }

    @JvmStatic
    fun keyPressed(keyCode: Int): Boolean {
        return keys[keyCode]?.pressed == true
    }

    /**
     * Returns whether the specified [Key] is continuously held down.
     *
     * @param key a member of the [Key] enum
     * @return if the specified key is down
     */
    @JvmStatic
    fun keyDown(key: Key): Boolean {
        return if (Mayonez.useGL) keyDown(key.glCode)
        else keyDown(key.awtCode)
    }

    /**
     * Returns whether the specified [Key] is pressed this frame.
     *
     * @param key a member of the [Key] enum
     * @return if the specified key is pressed
     */
    @JvmStatic
    fun keyPressed(key: Key): Boolean {
        return if (Mayonez.useGL) keyPressed(key.glCode)
        else keyPressed(key.awtCode)
    }

    /**
     * Returns whether the specified [Key] is continuously held down.
     *
     * @param keyName the name of the [Key], with spaces separating words
     * @return if the specified key is down
     */
    @JvmStatic
    fun keyDown(keyName: String): Boolean {
        for (key in Key.values()) {
            if (key.toString().equals(keyName, ignoreCase = true))
                return (keyDown(key))
        }
        return false
    }

    /**
     * Returns whether the specified [Key] is pressed this frame.
     *
     * @param keyName the name of the [Key], with spaces separating words
     * @return if the specified key is pressed
     */
    @JvmStatic
    fun keyPressed(keyName: String): Boolean {
        for (key in Key.values()) {
            if (key.toString().equals(keyName, ignoreCase = true))
                return (keyPressed(key))
        }
        return false
    }

    @JvmStatic
    fun getAxis(axisName: String): Int {
        for (a in KeyAxis.values()) {
            if (a.toString().equals(axisName, ignoreCase = true))
                return a.value()
        }
        return 0
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

}