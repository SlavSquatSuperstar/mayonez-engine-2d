package slavsquatsuperstar.mayonez

import org.apache.commons.lang3.StringUtils
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.*

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

    // Enum Definitions

    /**
     * Stores two keys intended to perform opposite actions.
     */
    enum class KeyAxis(posKey: KeyMapping, negKey: KeyMapping) {
        VERTICAL(KeyMapping.DOWN, KeyMapping.UP), HORIZONTAL(KeyMapping.RIGHT, KeyMapping.LEFT);

        private val posKey: String = posKey.name
        private val negKey: String = negKey.name

        /**
         * @return The value of this axis. 1 if the positive key is pressed. -1 if the negative key is pressed.
         * 0 if the both or neither key is pressed.
         */
        internal fun value(): Int {
            // "vector "method to make sure keys don't override each other
            val negComp = if (keyDown(negKey)) -1 else 0
            val posComp = if (keyDown(posKey)) 1 else 0
            return negComp + posComp
        }

        override fun toString(): String {
            return StringUtils.capitalize(name.lowercase())
        }

    }

    /**
     * Stores any number of virtual key codes under a common name.
     */
    enum class KeyMapping(internal vararg val keyCodes: Int) {
        // TODO read from file to assign keybinds (deserialize)
        UP(KeyEvent.VK_W, KeyEvent.VK_UP), DOWN(KeyEvent.VK_S, KeyEvent.VK_DOWN),
        LEFT(KeyEvent.VK_A, KeyEvent.VK_LEFT), RIGHT(KeyEvent.VK_D, KeyEvent.VK_RIGHT),
        Q(KeyEvent.VK_Q), E(KeyEvent.VK_E),
        SPACE(KeyEvent.VK_SPACE), SHIFT(KeyEvent.VK_SHIFT),
        EXIT(KeyEvent.VK_ESCAPE);

        override fun toString(): String {
            val str = StringBuilder("$name (")
            for (i in keyCodes.indices) {
                str.append(KeyEvent.getKeyText(keyCodes[i]))
                if (i < keyCodes.size - 1) str.append(", ")
            }
            str.append(")")
            return str.toString()
        }
    }
}