package slavsquatsuperstar.mayonez.input

import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

/**
 * The receiver for all keyboard-related input events.
 *
 * @author SlavSquatSuperstar
 */
object KeyInput : KeyAdapter() {

    // Key Fields

    private val keys = BooleanArray(256)

    // KeyListener Method Overrides

    override fun keyPressed(e: KeyEvent) {
        val code = e.keyCode
        keys[code] = true
    }

    override fun keyReleased(e: KeyEvent) {
        val code = e.keyCode
        keys[code] = false
    }

    // Keyboard Getters

    @JvmStatic
    fun keyDown(keyCode: Int): Boolean = keys[keyCode]

    /**
     * Whether any of the keys associated with the specified [KeyMapping] are pressed.
     *
     * @param keyName The name of the [KeyMapping].
     * @return Whether the specified key is pressed.
     */
    @JvmStatic
    fun keyDown(keyName: String?): Boolean {
        for (m in KeyMapping.values()) {
            if (m.name.equals(keyName, ignoreCase = true)) { // if the desired mapping exists
                for (code in m.keyCodes) if (keys[code]) return true
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