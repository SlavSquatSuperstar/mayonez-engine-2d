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
class GLMouseManager : MouseInputHandler {

    // Mouse Callbacks

    /**
     * The mouse button callback method for GLFW.
     *
     * @param window the window id
     * @param button the GLFW button code
     * @param action the event type
     * @param mods any modifier keys
     */
    fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        val event = when (action) {
            GLFW.GLFW_PRESS -> MouseButtonEvent(button, true, GLFW.glfwGetTime())
            GLFW.GLFW_RELEASE -> MouseButtonEvent(button, false)
            // According to docs, GLFW_REPEAT never occurs with mouse
            else -> return
        }
        eventSystem.broadcast(event)
    }

    /**
     * The mouse position callback method for GLFW.
     *
     * @param window the window id
     * @param xPos the x position of the cursor
     * @param yPos the y position of the cursor
     */
    fun mousePosCallback(window: Long, xPos: Double, yPos: Double) {
        eventSystem.broadcast(MouseMoveEvent(xPos.toFloat(), yPos.toFloat()))
    }

    /**
     * The mouse scroll callback method for GLFW.
     *
     * @param window the window id
     * @param xOffset the x offset of the scroll wheel
     * @param yOffset the y offset of the scroll wheel
     */
    fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        eventSystem.broadcast(MouseScrollEvent(xOffset.toFloat(), yOffset.toFloat()))
    }

    // Event Handler Overrides

    override fun getEventSystem(): EventSystem<MouseInputEvent> = InputEvents.MOUSE_EVENTS

    override fun getButtonCode(button: Button): Int = button.glCode

}