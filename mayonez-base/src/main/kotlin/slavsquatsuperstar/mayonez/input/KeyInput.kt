package slavsquatsuperstar.mayonez.input

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

/**
 * The receiver for all keyboard-related input events.
 *
 * @author SlavSquatSuperstar
 */
// TODO game loop / callback methods shouldn't be accessible from API (called statically)
object KeyInput : KeyAdapter() {

    // Key Fields
    private val keys = HashMap<Int, InputState>()

    // Game Loop Methods
    @JvmStatic
    fun endFrame() {
//        keysHeld.fill(false)
    }

    /* Keyboard Callbacks */

    @JvmStatic
    @Suppress("unused")
    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        if (action == GLFW_PRESS) {
            if (isKeyReleased(key)) setKey(key, InputState.PRESSED)
            else setKey(key, InputState.HELD)
        } else if (action == GLFW_RELEASE) {
            setKey(key, InputState.RELEASED)
        }
    }

    override fun keyPressed(e: KeyEvent) { // Activates when ever a key is down
        val key = e.keyCode
        if (isKeyReleased(key)) setKey(key, InputState.PRESSED) // New key press -> set as pressed
        else setKey(key, InputState.HELD) // Continuous key press -> set as held
    }

    override fun keyReleased(e: KeyEvent) {
        setKey(e.keyCode, InputState.RELEASED) // Set as released
    }

    private fun setKey(keyCode: Int, state: InputState) {
        keys[keyCode] = state
    }

    /* Keyboard Getters */

    private fun isKeyReleased(keyCode: Int): Boolean = (keys[keyCode] == InputState.RELEASED || keys[keyCode] == null)

    private fun isKeyPressed(keyCode: Int): Boolean = keys[keyCode] == InputState.PRESSED

    private fun isKeyHeld(keyCode: Int): Boolean = keys[keyCode] == InputState.HELD

    @JvmStatic
    fun keyDown(keyCode: Int): Boolean = isKeyHeld(keyCode) || isKeyPressed(keyCode)

    @JvmStatic
    fun keyPressed(keyCode: Int): Boolean = isKeyPressed(keyCode) && !isKeyHeld(keyCode)

    /**
     * Returns whether any of the keys associated with the specified [KeyMapping] is continuously held down.
     *
     * @param keyName the name of the [KeyMapping]
     * @return if the specified key is down
     */
    @JvmStatic
    fun keyDown(keyName: String): Boolean {
        for (m in KeyMapping.values())
            if (m.name.equals(keyName, ignoreCase = true)) // if the desired mapping exists
                for (code in m.keyCodes) if (keyDown(code)) return true
        return false
    }

    /**
     * Returns whether any of the keys associated with the specified [KeyMapping] has been pressed.
     *
     * @param keyName the name of the [KeyMapping]
     * @return if the specified key was pressed
     */
    @JvmStatic
    fun keyPressed(keyName: String): Boolean {
        for (m in KeyMapping.values())
            if (m.name.equals(keyName, ignoreCase = true))
                for (code in m.keyCodes) if (keyPressed(code)) return true
        return false
    }

    @JvmStatic
    fun getAxis(axisName: String): Int {
        for (a in KeyAxis.values())
            if (a.toString().equals(axisName, ignoreCase = true))
                return a.value()
        return 0
    }

}