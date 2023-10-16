package mayonez.input.mouse

import mayonez.input.*
import org.lwjgl.glfw.GLFW

/**
 * Receives mouse input events from GLFW.
 *
 * @author SlavSquatSuperstar
 */
class GLMouseManager : MouseManager() {

//    private var lastButton: Int = -1
//    private var lastAction: Int = -1

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
        if (!button.isValidIndex()) return
//        lastButton = button
//        lastAction = action
        when (action) {
            GLFW.GLFW_PRESS -> {
                setButtonDown(button, true)
                pressed = true
            }

            GLFW.GLFW_RELEASE -> {
                setButtonDown(button, false)
                pressed = false
            }
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
        if (pressed) {
            setMouseDisp(xPos - mousePosPx.x, yPos - mousePosPx.y)
        }
        setMousePos(xPos, yPos)
    }

    /**
     * The mouse scree callback method for GLFW.
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