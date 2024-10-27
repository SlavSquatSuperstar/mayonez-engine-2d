package mayonez.input

import mayonez.util.*
import org.lwjgl.glfw.GLFW.*
import java.awt.event.MouseEvent.*

/**
 * Stores mouse button codes commonly used in this program under a user-friendly button name.
 *
 * @author SlavSquatSuperstar
 */
enum class Button(val awtCode: Int, val glCode: Int) {

    UNKNOWN(NOBUTTON, GLFW_KEY_UNKNOWN),
    LEFT_MOUSE(BUTTON1, GLFW_MOUSE_BUTTON_LEFT),
    RIGHT_MOUSE(BUTTON3, GLFW_MOUSE_BUTTON_RIGHT),
    MIDDLE_MOUSE(BUTTON2, GLFW_MOUSE_BUTTON_MIDDLE),

    MOUSE_1(BUTTON1, GLFW_MOUSE_BUTTON_1),
    MOUSE_2(BUTTON3, GLFW_MOUSE_BUTTON_2),
    MOUSE_3(BUTTON2, GLFW_MOUSE_BUTTON_3);

    // Replace underscores with spaces and convert to title case
    override fun toString(): String {
        return StringUtils.capitalizeAllWords(name.replace('_', ' '))
    }

    companion object {
        internal fun findWithName(buttonName: String): Button? {
            return StringUtils.findWithName(entries, buttonName)
        }
    }

}