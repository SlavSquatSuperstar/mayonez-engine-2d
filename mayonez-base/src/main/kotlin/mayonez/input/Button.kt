package mayonez.input

import org.lwjgl.glfw.GLFW
import mayonez.util.StringUtils
import java.awt.event.MouseEvent

/**
 * Stores mouse button codes commonly used in this program under a user-friendly button name.
 *
 * @author SlavSquatSuperstar
 */
enum class Button(internal val awtCode: Int, internal val glCode: Int) {

    UNKNOWN(MouseEvent.NOBUTTON, -1),
    MOUSE_1(MouseEvent.BUTTON1, GLFW.GLFW_MOUSE_BUTTON_1),
    MOUSE_2(MouseEvent.BUTTON3, GLFW.GLFW_MOUSE_BUTTON_2),
    MOUSE_3(MouseEvent.BUTTON2, GLFW.GLFW_MOUSE_BUTTON_3),

    LEFT_MOUSE(MouseEvent.BUTTON1, GLFW.GLFW_MOUSE_BUTTON_1),
    RIGHT_MOUSE(MouseEvent.BUTTON3, GLFW.GLFW_MOUSE_BUTTON_2),
    MIDDLE_MOUSE(MouseEvent.BUTTON2, GLFW.GLFW_MOUSE_BUTTON_3);

    // Replace underscores with spaces and convert to title case
    override fun toString(): String {
        return StringUtils.capitalizeWords(name.replace('_', ' '))
    }

}