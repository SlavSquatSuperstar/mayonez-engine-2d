package slavsquatsuperstar.mayonez.input

import java.awt.event.KeyEvent

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