package slavsquatsuperstar.mayonez.input

import org.lwjgl.glfw.GLFW
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Game
import slavsquatsuperstar.mayonez.Logger
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent

/**
 * The receiver for all mouse-related input events.
 *
 * @author SlavSquatSuperstar
 */
@Suppress("unused")
object MouseInput : MouseAdapter() {

    /* Fields and Properties */

    // Mouse Pointer Fields (in pixels)
    private var mousePos = Vec2()
    private var mouseDisp = Vec2() // drag displacement

    // Mouse Scroll Fields
    private var scroll = Vec2()

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

    /* Callback Methods */

    // Mouse Button Callbacks

    @JvmStatic
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
    fun mousePosCallback(window: Long, xPos: Double, yPos: Double) {
        if (pressed) setMouseDisp(xPos - mousePos.x, yPos - mousePos.y)
        setMousePos(xPos, yPos)
    }

    override fun mouseMoved(e: MouseEvent) {
        setMousePos(e.x, e.y)
    }

    override fun mouseDragged(e: MouseEvent) {
        pressed = true
        setMouseDisp(e.x - mousePos.x, e.y - mousePos.y)
        setMousePos(e.x, e.y)
    }

    private fun setMousePos(x: Number, y: Number) {
        mousePos.set(x.toFloat(), y.toFloat())
    }

    private fun setMouseDisp(dx: Number, dy: Number) {
        mouseDisp.set(dx.toFloat(), dy.toFloat())
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
        scroll.set(scrollX.toFloat(), scrollY.toFloat())
    }

    /* Getter Methods */

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
    fun getScreenPos(): Vec2 = mousePos

    @JvmStatic
    fun getScreenX(): Float = mousePos.x

    @JvmStatic
    fun getScreenY(): Float = mousePos.y

    @JvmStatic
    fun getWorldPos(): Vec2 {
        val flipped = Vec2(mousePos.x, Game.getWindowHeight() - mousePos.y) // Mirror y
        return flipped.toWorld()
    }

    @JvmStatic
    fun getWorldX(): Float = getWorldPos().x

    @JvmStatic
    fun getWorldY(): Float = getWorldPos().y

    // Mouse Displacement Getters

    @JvmStatic
    fun getScreenDisp(): Vec2 = mouseDisp

    @JvmStatic
    fun getScreenDx(): Float = mouseDisp.x

    @JvmStatic
    fun getScreenDy(): Float = mouseDisp.y

    @JvmStatic
    fun getWorldDisp(): Vec2 = (mouseDisp * Vec2(1f, -1f)).toWorld() // Invert y

    @JvmStatic
    fun getWorldDx(): Float = getWorldDisp().x

    @JvmStatic
    fun getWorldDy(): Float = getWorldDisp().y

    // Helper Methods

    private fun Vec2.toWorld(): Vec2 {
        return this / (Game.currentScene()?.cellSize ?: 1.0f)
    }

}