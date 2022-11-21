package mayonez.input

import org.lwjgl.glfw.GLFW.*
import mayonez.Mayonez
import mayonez.event.EventSystem
import mayonez.event.KeyboardEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

/**
 * The receiver for all keyboard-related input events.
 *
 * @author SlavSquatSuperstar
 */
// TODO GLFW sticky keys?
@Suppress("unused")
object KeyInput : KeyAdapter() {

    // Key Fields
    // represent a bit field with held, pressed, and down bits
    private val keysDown = HashMap<Int, Boolean?>() // if key listener detects this key
    private val keysPressed = HashMap<Int, Boolean?>() // if key was just pressed in this frame
    private val keysHeld = HashMap<Int, Boolean?>() // if key is continuously held down

    // Game Loop Methods
    @JvmStatic
    fun endFrame() { // TODO rename to pollKeys?
        // Update key input
        for (k in keysDown.keys) {
            if (keysDown[k] == true) {
                if (keysPressed[k] != true && keysHeld[k] != true) { // New key press
                    keysPressed[k] = true
                } else if (keysPressed[k] == true) { // Continued key press
                    keysPressed[k] = false
                    keysHeld[k] = true
                }
            } else { // Released key
                keysPressed[k] = false
                keysHeld[k] = false
            }
        }
    }

    /* Keyboard Callbacks */

    @JvmStatic
    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
//        println(glfwGetKeyName(GLFW_KEY_UNKNOWN, scancode))
        when (action) {
            // TODO GL double pressing still occurs
            GLFW_PRESS -> {
                keysDown[key] = true
//                keysPressed[key] = true
//                keysHeld[key] = false
            }

            GLFW_REPEAT -> {
                keysDown[key] = true
//                keysPressed[key] = false
//                keysHeld[key] = true
            }

            GLFW_RELEASE -> {
                keysDown[key] = false
//                keysPressed[key] = false
//                keysHeld[key] = false
            }
        }
        EventSystem.broadcast(KeyboardEvent(key, scancode, action, mods))
        endFrame()
    }

    override fun keyPressed(e: KeyEvent) { // Activates when ever a key is down
        keysDown[e.keyCode] = true
        EventSystem.broadcast(KeyboardEvent(e.keyCode, true, e.modifiersEx))
//        println("${KeyEvent.getModifiersExText(e.modifiersEx)}${e.keyLocation}")
    }

    override fun keyReleased(e: KeyEvent) {
        keysDown[e.keyCode] = false
        EventSystem.broadcast(KeyboardEvent(e.keyCode, false, e.modifiersEx))
    }

    /* Keyboard Getters */

    @JvmStatic
    fun keyDown(keyCode: Int): Boolean = keysHeld[keyCode] == true || keysPressed[keyCode] == true

    @JvmStatic
    fun keyPressed(keyCode: Int): Boolean = keysPressed[keyCode] == true

    /**
     * Returns whether the specified [Key] is continuously held down.
     *
     * @param key a member of the [Key] enum
     * @return if the specified key is down
     */
    @JvmStatic
    fun keyDown(key: Key): Boolean {
        return if (Mayonez.useGL!!) keyDown(key.glCode)
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
        return if (Mayonez.useGL!!) keyPressed(key.glCode)
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

}