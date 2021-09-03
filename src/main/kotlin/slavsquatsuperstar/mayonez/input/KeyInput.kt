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
object KeyInput : KeyAdapter() {

    // Key Fields

    private val keysLast = BooleanArray(350) // continuously held
    private val keysDown = BooleanArray(350) // tapped once

    // Game Loop Methods (should not be called statically)

    fun endFrame() {
        keysLast.fill(element = false)
    }

    @SuppressWarnings("unused")
    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        if (action == GLFW_PRESS) {
            keysDown[key] = true
            keysLast[key] = true
        } else if (action == GLFW_RELEASE) {
            keysDown[key] = false
            keysLast[key] = false
        }
    }

    // KeyListener Overrides

    override fun keyPressed(e: KeyEvent) {
        val code = e.keyCode
        keysLast[code] = true
        keysDown[code] = true
    }

    override fun keyReleased(e: KeyEvent) {
        val code = e.keyCode
        keysLast[code] = false
        keysDown[code] = false
    }

    // Keyboard Getters

    @JvmStatic
    fun keyDown(keyCode: Int): Boolean = keysDown[keyCode]

    @JvmStatic
    fun keyPressed(keyCode: Int): Boolean = keysLast[keyCode]

    /**
     * Whether any of the keys associated with the specified [KeyMapping] are pressed.
     *
     * @param keyName The name of the [KeyMapping].
     * @return Whether the specified key is pressed.
     */
    @JvmStatic
    fun keyDown(keyName: String): Boolean {
        for (m in KeyMapping.values()) {
            if (m.name.equals(keyName, ignoreCase = true)) { // if the desired mapping exists
                for (code in m.keyCodes) if (keysDown[code]) return true
            }
        }
        return false
    }

    @JvmStatic
    fun keyPressed(keyName: String): Boolean {
        for (m in KeyMapping.values()) {
            if (m.name.equals(keyName, ignoreCase = true)) { // if the desired mapping exists
                for (code in m.keyCodes) if (keysLast[code]) return true
            }
        }
        return false
    }

    @JvmStatic
    fun getAxis(axisName: String?): Int {
        for (a in KeyAxis.values())
            if (a.toString().equals(axisName, ignoreCase = true))
                return a.value()
        return 0
    }

}