package slavsquatsuperstar.mayonez.input

import org.lwjgl.glfw.GLFW
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Game
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent

/**
 * The receiver for all mouse-related input events.
 *
 * @author SlavSquatSuperstar
 */
object MouseInput : MouseAdapter() {

    /*
     * Fields and Properties
     */

    // Mouse Pointer Fields (in pixels)
    private var mouseX: Float = 0.0f
    private var mouseY: Float = 0.0f
    private var mouseDx: Float = 0.0f // drag displacement
    private var mouseDy: Float = 0.0f

    // Mouse Scroll Fields
    private var scrollX: Float = 0.0f
    private var scrollY: Float = 0.0f

    // Mouse Button Fields

    private var buttons = BooleanArray(9)
    private var buttonsLast = BooleanArray(9)

    @JvmStatic
    // TODO isDragging() method
    var pressed: Boolean = false // assuming only one button clicked at a time
        @JvmName("isPressed") get
        private set

    @JvmStatic
    var clicks: Int = 0 // doesn't work with GL yet
        private set

    // Game Loop Methods

    @JvmStatic
    fun endFrame() {
        setMouseDisp(0, 0)
        setScrollPos(0, 0)
        buttonsLast.fill(false)
    }

    /*
     * Callback Methods
     */

    // Mouse Button Callbacks

    @JvmStatic
    @Suppress("unused")
    fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        if (action == GLFW.GLFW_PRESS) {
            setButton(button, true)
        } else if (action == GLFW.GLFW_RELEASE) {
            setButton(button, false)
            pressed = false
        }
    }

    override fun mousePressed(e: MouseEvent) {
        val button = e.button
        setButton(button, true)
        clicks = e.clickCount
    }

    override fun mouseReleased(e: MouseEvent) {
        val button = e.button
        setButton(button, false)
        setMouseDisp(0, 0)
        clicks = 0
    }

    private fun setButton(button: Int, isDown: Boolean) {
        if (button < buttons.size) {
            buttons[button] = isDown
            buttonsLast[button] = isDown
            pressed = isDown
        }
    }

    // Mouse Movement Callbacks

    @JvmStatic
    @SuppressWarnings("unused")
    fun mousePosCallback(window: Long, xPos: Double, yPos: Double) {
        if (pressed) setMouseDisp(xPos - mouseX, yPos - mouseY)
        setMousePos(xPos, yPos)
    }

    override fun mouseMoved(e: MouseEvent) {
        setMousePos(e.x, e.y)
    }

    override fun mouseDragged(e: MouseEvent) {
        pressed = true
        setMouseDisp(e.x - mouseX, e.y - mouseY)
        setMousePos(e.x, e.y)
    }

    private fun setMousePos(x: Number, y: Number) {
        mouseX = x.toFloat()
        mouseY = y.toFloat()
    }

    private fun setMouseDisp(dx: Number, dy: Number) {
        mouseDx = dx.toFloat()
        mouseDy = dy.toFloat()
    }

    // Mouse Scroll Callbacks

    @JvmStatic
    @SuppressWarnings("unused")
    fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        setScrollPos(xOffset, yOffset)
    }

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        setScrollPos(e.x, e.y)
    }

    private fun setScrollPos(scrollX: Number, scrollY: Number) {
        this.scrollX = scrollX.toFloat()
        this.scrollY = scrollY.toFloat()
    }

    // Mouse Button Getters

    @JvmStatic
    fun buttonDown(button: Int): Boolean = buttons[button]

    @JvmStatic
    fun buttonPressed(button: Int): Boolean = buttonsLast[button]

    @JvmStatic
    fun buttonDown(buttonName: String): Boolean {
        for (b in MouseMapping.values())
            if (b.toString().equals(buttonName, ignoreCase = true))
                return buttons[b.button]
        return false
    }

    @JvmStatic
    fun buttonPressed(buttonName: String): Boolean {
        for (b in MouseMapping.values())
            if (b.toString().equals(buttonName, ignoreCase = true))
                return buttonsLast[b.button]
        return false
    }

    // Mouse Position Getters

    @JvmStatic
    fun getScreenX(): Float = mouseX

    @JvmStatic
    fun getScreenY(): Float = mouseY

    @JvmStatic
    fun getScreenPos(): Vec2 = Vec2(mouseX, mouseY)

    @JvmStatic
    fun getWorldX(): Float = mouseX.toWorld()

    @JvmStatic
    fun getWorldY(): Float = mouseY.toWorld()

    @JvmStatic
    fun getWorldPos(): Vec2 = Vec2(getWorldX(), getWorldY())

    // Mouse Displacement Getters

    @JvmStatic
    fun getScreenDx(): Float = mouseDx

    @JvmStatic
    fun getScreenDy(): Float = mouseDy

    @JvmStatic
    fun getScreenDisp(): Vec2 = Vec2(mouseDx, mouseDy)

    @JvmStatic
    fun getWorldDx(): Float = mouseDx.toWorld()

    @JvmStatic
    fun getWorldDy(): Float = mouseDy.toWorld()

    @JvmStatic
    fun getWorldDisp(): Vec2 = Vec2(getWorldDx(), getWorldDy())

    private fun Float.toWorld(): Float = this / (Game.currentScene()?.cellSize?.toFloat() ?: 1.0f)

}