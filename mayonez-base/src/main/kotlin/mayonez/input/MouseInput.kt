package mayonez.input

import mayonez.Mayonez
import mayonez.SceneManager
import mayonez.math.Vec2
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
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
    private const val NUM_BUTTONS: Int = 8
    private val buttonsDown: Array<Boolean> = Array(8) { false } // if mouse listener detects this button
    private val buttonsPressed: Array<Boolean> = Array(8) { false } // if button is pressed this frame
    private val buttonsHeld: Array<Boolean> = Array(8) { false } // if button is continuously held down

    @JvmStatic
    // TODO isDragging() method
    var pressed: Boolean = false // assuming only one button clicked at a time
        @JvmName("isPressed") get
        private set

    @JvmStatic
    var released: Boolean = false
        @JvmName("isReleased") get
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

    // Mouse Button Callbacks

    @JvmStatic
    fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        if (!button.isValid()) return
        lastButton = button
        lastAction = action
        when (action) {
            GLFW_PRESS -> {
                buttonsDown[button] = true
                pressed = true
                released = false
            }

            GLFW_RELEASE -> {
                buttonsDown[button] = false
                pressed = false
                released = true
            }
        }
        pollMouseButtons()
    }

    override fun mousePressed(e: MouseEvent) {
        if (!e.button.isValid()) return
//        println("pressed")
        buttonsDown[e.button] = true
        pressed = true
        released = false
        clicks = e.clickCount
    }

    override fun mouseReleased(e: MouseEvent) {
        if (!e.button.isValid()) return
//        println("released")
        buttonsDown[e.button] = false
        setMouseDisp(0, 0)
        pressed = false
        released = true
        clicks = 0
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
    fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        setScrollPos(xOffset, yOffset)
    }

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        setScrollPos(e.x, e.y)
    }

    private fun setScrollPos(scrollX: Number, scrollY: Number) {
        scroll.set(scrollX.toFloat(), scrollY.toFloat())
    }

    // Mouse Button Getters

    @JvmStatic
    fun buttonDown(button: Int): Boolean = button.isValid() && (buttonsHeld[button] || buttonsPressed[button])

    @JvmStatic
    fun buttonPressed(button: Int): Boolean = button.isValid() && buttonsPressed[button]

    @JvmStatic
    fun buttonDown(button: Button): Boolean {
        return if (Mayonez.useGL) buttonDown(button.glCode)
        else buttonDown(button.awtCode)
    }

    @JvmStatic
    fun buttonPressed(button: Button): Boolean {
        return if (Mayonez.useGL) buttonPressed(button.glCode)
        else buttonPressed(button.awtCode)
    }

    @JvmStatic
    fun buttonDown(buttonName: String): Boolean {
        for (button in Button.values()) {
            if (button.toString().equals(buttonName, ignoreCase = true))
                return buttonDown(button)
        }
        return false
    }

    @JvmStatic
    fun buttonPressed(buttonName: String): Boolean {
        for (button in Button.values()) {
            if (button.toString().equals(buttonName, ignoreCase = true))
                return buttonPressed(button)
        }
        return false
    }

    // Mouse Position Getters

    @JvmStatic
    val screenPos: Vec2
        get() = mousePos

    @JvmStatic
    val position: Vec2
        get() = SceneManager.currentScene.camera.toWorld(mousePos)

    // Mouse Displacement Getters

    @JvmStatic
    val screenDisp: Vec2
        get() = mouseDisp

    @JvmStatic
    val displacement: Vec2
        get() = (mouseDisp * Vec2(1f, -1f)).toWorld() // Invert y


    // Helper Methods

    // Converts screen pixels to world units
    private fun Vec2.toWorld(): Vec2 = this / SceneManager.currentScene.scale

    // If a button is a valid index
    private fun Int.isValid(): Boolean = this in 0 until NUM_BUTTONS

}