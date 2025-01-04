package mayonez.input.mouse

import mayonez.input.*
import org.lwjgl.glfw.GLFW

private const val DOUBLE_CLICK_TIME_SECS: Float = 0.20f

/**
 * Receives mouse input events from GLFW.
 *
 * Source: [GLFW Input Guide § Mouse Input](https://www.glfw.org/docs/latest/input_guide.html#input_mouse)
 *
 * @author SlavSquatSuperstar
 */
class GLMouseManager : MouseManager() {

    private var lastClickTimeSecs: Double = 0.0

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
                setButtonDown(button, true)
                // Detect double click
                // Source: https://www.youtube.com/watch?v=k3rVEIr0Z7w
                val currentClickTimeSecs = GLFW.glfwGetTime()
                if (currentClickTimeSecs - lastClickTimeSecs <= DOUBLE_CLICK_TIME_SECS)
                    setDoubleClick(true)
                lastClickTimeSecs = currentClickTimeSecs
            }
            // According to docs, GLFW_REPEAT never occurs with mouse
            GLFW.GLFW_RELEASE -> setButtonDown(button, false)
        }
        updateButtons()
    }

    /**
     * The mouse position callback method for GLFW.
     *
     * @param window the window id
     * @param xPos the x position of the cursor
     * @param yPos the y position of the cursor
     */
    override fun mousePosCallback(window: Long, xPos: Double, yPos: Double) {
        if (anyButtonDown) {
            setMouseDisp(xPos - mousePosPx.x, yPos - mousePosPx.y)
        }
        setMousePos(xPos, yPos)
    }

    /**
     * The mouse scroll callback method for GLFW.
     *
     * @param window the window id
     * @param xOffset the x offset of the scroll wheel
     * @param yOffset the y offset of the scroll wheel
     */
    override fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        setScrollPos(xOffset, yOffset)
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

}