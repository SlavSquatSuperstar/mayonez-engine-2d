package slavsquatsuperstar.mayonez.input

import org.lwjgl.glfw.GLFW.*
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Mayonez
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
    private val buttons = HashMap<Int, InputState>()
//    private var buttons = BooleanArray(9)
//    private var buttonsLast = BooleanArray(9)

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
//        for (b in buttons.keys) {
//            if (buttons[b] == InputState.PRESSED) buttons[b] = InputState.HELD
//        }
    }

    /* Mouse Button Callbacks */

    @JvmStatic
    fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        when (action) {
            GLFW_PRESS -> setButton(button, InputState.PRESSED)
            GLFW_REPEAT -> setButton(button, InputState.HELD)
            GLFW_RELEASE -> setButton(button, InputState.RELEASED)
        }
        pressed = buttons[button] != InputState.RELEASED
    }

    override fun mousePressed(e: MouseEvent) {
        val button = e.button
        if (isButtonReleased(button)) setButton(button, InputState.PRESSED) // New button press -> set as pressed
        else setButton(button, InputState.HELD) // Continuous button press -> set as held
        pressed = true
        clicks = e.clickCount
    }

    override fun mouseReleased(e: MouseEvent) {
        val button = e.button
        setButton(button, InputState.RELEASED)
        setMouseDisp(0, 0)
        clicks = 0
    }

    private fun setButton(button: Int, state: InputState) {
        buttons[button] = state
    }

    /* Mouse Movement Callbacks */

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

    /* Mouse Scroll Callbacks */

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

    /* Mouse Button Getters */

    private fun isButtonReleased(button: Int): Boolean =
        (buttons[button] == InputState.RELEASED || buttons[button] == null)

    private fun isButtonPressed(button: Int): Boolean = buttons[button] == InputState.PRESSED

    private fun isButtonHeld(button: Int): Boolean = buttons[button] == InputState.HELD

    @JvmStatic
    fun buttonDown(button: Int): Boolean = isButtonHeld(button) || isButtonPressed(button)

    @JvmStatic
    fun buttonPressed(button: Int): Boolean = isButtonPressed(button)

    @JvmStatic
    fun buttonDown(buttonName: String): Boolean {
        for (b in MouseMapping.values())
            if (b.toString().equals(buttonName, ignoreCase = true))
                return buttonDown(b.button)
        return false
    }

    @JvmStatic
    fun buttonPressed(buttonName: String): Boolean {
        for (b in MouseMapping.values())
            if (b.toString().equals(buttonName, ignoreCase = true))
                return buttonPressed(b.button)
        return false
    }

    /* Mouse Position Getters */

    @JvmStatic
    fun getScreenPos(): Vec2 = mousePos

    @JvmStatic
    fun getScreenX(): Float = mousePos.x

    @JvmStatic
    fun getScreenY(): Float = mousePos.y

    @JvmStatic
    fun getWorldPos(): Vec2 {
        val flipped = Vec2(mousePos.x, Mayonez.windowHeight - mousePos.y) // Mirror y
        return flipped.toWorld()
    }

    @JvmStatic
    fun getWorldX(): Float = getWorldPos().x

    @JvmStatic
    fun getWorldY(): Float = getWorldPos().y

    /* Mouse Displacement Getters */

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
        return this / Mayonez.scene.cellSize
    }

}