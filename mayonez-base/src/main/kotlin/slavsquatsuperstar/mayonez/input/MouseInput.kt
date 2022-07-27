package slavsquatsuperstar.mayonez.input

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
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
    // Store mouse buttons here
    private var lastButton: Int = -1
    private var lastAction: Int = -1

    // Don't need hashmaps because very few buttons, max 8 buttons used by GLFW
    private val buttonsDown: Array<Boolean> = Array(8) { false } // if mouse listener detects this button
    private val buttonsPressed: Array<Boolean> = Array(8) { false } // if button is pressed this frame
    private val buttonsHeld: Array<Boolean> = Array(8) { false } // if button is continuously held down

    @JvmStatic
    // TODO isDragging() method
    var pressed: Boolean = false // assuming only one button clicked at a time
        @JvmName("isPressed") get
        private set

    @JvmStatic
    var clicks: Int = 0 // doesn't work with GL yet
        private set

    // Game Loop Methods
    private fun pollMouseButtons() {
        for (b in buttonsDown.indices) {
            if (buttonsDown[b]) {
                if (!buttonsPressed[b] && !buttonsHeld[b]) { // New button press
                    buttonsPressed[b] = true
                } else if (buttonsPressed[b]) { // Continued button press
                    buttonsPressed[b] = false
                    buttonsHeld[b] = true
                }
            } else { // Released button
                buttonsPressed[b] = false
                buttonsHeld[b] = false
            }

        }
    }

    @JvmStatic
    fun endFrame() {
        // Update mouse input
        pollMouseButtons()

        /// Reset motion
        setMouseDisp(0, 0)
        setScrollPos(0, 0)
    }

    /* Mouse Button Callbacks */

    @JvmStatic
    fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        lastButton = button
        lastAction = action
        when (action) {
            GLFW_PRESS -> buttonsDown[button] = true
            GLFW_RELEASE -> buttonsDown[button] = false
        }
        pressed = buttonsDown[button] == true
        pollMouseButtons()
    }

    override fun mousePressed(e: MouseEvent) {
        println("pressed")
        buttonsDown[e.button] = true
        pressed = true
        clicks = e.clickCount
    }

    override fun mouseReleased(e: MouseEvent) {
        println("released")
        buttonsDown[e.button] = false
        setMouseDisp(0, 0)
        pressed = false
        clicks = 0
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

    @JvmStatic
    fun buttonDown(button: Int): Boolean = buttonsHeld[button] || buttonsPressed[button]

    @JvmStatic
    fun buttonPressed(button: Int): Boolean = buttonsPressed[button]

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