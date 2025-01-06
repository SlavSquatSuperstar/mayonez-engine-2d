package mayonez.input.mouse

import mayonez.event.*
import mayonez.input.*
import mayonez.input.events.*
import org.lwjgl.glfw.GLFW

/**
 * Receives mouse input events from GLFW.
 *
 * Source: [GLFW Input Guide § Mouse Input](https://www.glfw.org/docs/latest/input_guide.html#input_mouse)
 *
 * @author SlavSquatSuperstar
 */
class GLMouseManager : MouseManager(), MouseInputHandler {

    // Mouse Callbacks

    /**
     * The mouse button callback method for GLFW.
     *
     * @param window the window id
     * @param button the GLFW button code
     * @param action the event type
     * @param mods any modifier keys
     */
    override fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        when (action) {
            GLFW.GLFW_PRESS -> {
                onMouseInputEvent(MouseButtonEvent(button, true, GLFW.glfwGetTime()))
            }

            GLFW.GLFW_RELEASE -> {
                onMouseInputEvent(MouseButtonEvent(button, false))
            }
            // According to docs, GLFW_REPEAT never occurs with mouse
        }
    }

    /**
     * The mouse position callback method for GLFW.
     *
     * @param window the window id
     * @param xPos the x position of the cursor
     * @param yPos the y position of the cursor
     */
    override fun mousePosCallback(window: Long, xPos: Double, yPos: Double) {
        onMouseInputEvent(MouseMoveEvent(xPos.toFloat(), yPos.toFloat()))
    }

    /**
     * The mouse scroll callback method for GLFW.
     *
     * @param window the window id
     * @param xOffset the x offset of the scroll wheel
     * @param yOffset the y offset of the scroll wheel
     */
    override fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        onMouseInputEvent(MouseScrollEvent(xOffset.toFloat(), yOffset.toFloat()))
    }

    // Mouse Button Getters

    override fun buttonDown(button: Button?): Boolean {
        return if (button == null) false
        else buttonDown(button.glCode)
    }

    override fun buttonPressed(button: Button?): Boolean {
        return if (button == null) false
        else buttonPressed(button.glCode)
    }

    // Event Handler Overrides

    override fun getEventSystem(): EventSystem<MouseInputEvent> {
        return InputEvents.MOUSE_EVENTS
    }

    override fun getButtonCode(button: Button): Int = button.glCode

}