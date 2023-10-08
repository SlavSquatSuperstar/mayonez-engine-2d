package mayonez.input

import mayonez.util.*
import org.lwjgl.glfw.GLFW
import java.awt.event.*

/**
 * Stores mouse button codes commonly used in this program under a user-friendly button name.
 *
 * @author SlavSquatSuperstar
 */
enum class Button(internal val awtCode: Int, internal val glCode: Int) {

    UNKNOWN(MouseEvent.NOBUTTON, GLFW.GLFW_KEY_UNKNOWN),
    LEFT_MOUSE(MouseEvent.BUTTON1, GLFW.GLFW_MOUSE_BUTTON_LEFT),
    RIGHT_MOUSE(MouseEvent.BUTTON3, GLFW.GLFW_MOUSE_BUTTON_RIGHT),
    MIDDLE_MOUSE(MouseEvent.BUTTON2, GLFW.GLFW_MOUSE_BUTTON_MIDDLE),

    MOUSE_1(MouseEvent.BUTTON1, GLFW.GLFW_MOUSE_BUTTON_1),
    MOUSE_2(MouseEvent.BUTTON3, GLFW.GLFW_MOUSE_BUTTON_2),
    MOUSE_3(MouseEvent.BUTTON2, GLFW.GLFW_MOUSE_BUTTON_3);

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