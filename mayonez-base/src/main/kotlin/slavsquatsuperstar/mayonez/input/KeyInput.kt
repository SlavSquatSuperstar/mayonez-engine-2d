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
// isKeyPressed() works in window begin frame, but not game update -> query too slow?
object KeyInput : KeyAdapter() {

    // Key Fields
    private val keys = BooleanArray(350) // tapped once
    private val keysLast = BooleanArray(350) // continuously held

    // Game Loop Methods

    @JvmStatic
    fun endFrame() {
        keysLast.fill(false)
    }

    // Keyboard Callbacks

    @JvmStatic
    @Suppress("unused")
    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        when (action) {
            GLFW_PRESS -> setKey(key, true)
            GLFW_RELEASE -> setKey(key, false);
        }
    }

    override fun keyPressed(e: KeyEvent) {
        if (!keyDown(e.keyCode)) setKey(e.keyCode, true) // only register press if not down
    }

    override fun keyReleased(e: KeyEvent) {
        setKey(e.keyCode, false)
    }

    private fun setKey(keyCode: Int, isDown: Boolean) {
        if (keyCode < keys.size) {
            keysLast[keyCode] = isDown
            keys[keyCode] = isDown
        }
    }

    // Keyboard Getters

    @JvmStatic
    fun keyDown(keyCode: Int): Boolean = keys[keyCode]

    @JvmStatic
    fun keyPressed(keyCode: Int): Boolean = keysLast[keyCode]

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