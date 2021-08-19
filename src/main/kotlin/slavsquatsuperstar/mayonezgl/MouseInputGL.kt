package slavsquatsuperstar.mayonezgl

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE


object MouseInputGL {

    // Mouse Position
    private var scrollX: Double = 0.0
    private var scrollY: Double = 0.0
    private var xPos: Double = 0.0
    private var yPos: Double = 0.0
    private var lastX: Double = 0.0
    private var lastY: Double = 0.0

    // Mouse Status
    private var buttonsPressed = BooleanArray(9)
    private var buttonsHeld = BooleanArray(9)
    var buttonsDown: Int = 0 // Number of buttons pressed
        private set
    private var dragging = false

    @JvmStatic
    fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        if (action == GLFW_PRESS) {
            buttonsDown++
            if (button < buttonsHeld.size) {
                buttonsPressed[button] = true
                buttonsHeld[button] = true
            }
        } else if (action == GLFW_RELEASE) {
            buttonsDown--
            if (button < buttonsHeld.size) {
                buttonsPressed[button] = false
                buttonsHeld[button] = false
                dragging = false
            }
        }
    }

    @JvmStatic
    fun mousePosCallback(window: Long, xpos: Double, ypos: Double) {
        if (buttonsDown > 0) {
            dragging = true
        }
        lastX = xPos
        lastY = yPos
        xPos = xpos
        yPos = ypos
    }

    @JvmStatic
    fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        scrollX = xOffset
        scrollY = yOffset
    }

    @JvmStatic
    fun endFrame() {
        scrollX = 0.0
        scrollY = 0.0
        buttonsPressed.fill(element = false)
    }

    // Button Getters

    @JvmStatic
    fun isButtonPressed(button: Int): Boolean = if (button < buttonsPressed.size) buttonsPressed[button] else false

    @JvmStatic
    fun isButtonHeld(button: Int): Boolean = if (button < buttonsHeld.size) buttonsHeld[button] else false

    @JvmStatic
    fun isDragging(): Boolean = dragging

    // Position Getters

    @JvmStatic
    fun getX(): Float = xPos.toFloat()

    @JvmStatic
    fun getY(): Float = yPos.toFloat()

    @JvmStatic
    fun getDx(): Float = (xPos - lastX).toFloat()

    @JvmStatic
    fun getDy(): Float = (yPos - lastY).toFloat()

    @JvmStatic
    fun getScrollX(): Float = scrollX.toFloat()

    @JvmStatic
    fun getScrollY(): Float = scrollY.toFloat()

}