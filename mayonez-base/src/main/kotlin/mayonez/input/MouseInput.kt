package mayonez.input

import mayonez.*
import mayonez.math.*
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import java.awt.event.*

/**
 * The receiver for all mouse-related input events.
 *
 * @author SlavSquatSuperstar
 */
@Suppress("unused")
object MouseInput : MouseAdapter() {

    // Mouse Pointer Fields
    private var mousePosPx = Vec2()
    private var mouseDispPx = Vec2() // drag displacement

    // Mouse Scroll Fields
    private var scroll = Vec2()

    // Mouse Button Fields
    private var lastButton: Int = -1
    private var lastAction: Int = -1

    // Use arrays instead of hashmaps because very few buttons, GLFW uses max 8
    private const val NUM_BUTTONS: Int = 8
    private val buttons: Array<MappingStatus> = Array(NUM_BUTTONS) { MappingStatus() }

    /** If any of the mouse buttons are pressed or held. */
    @JvmStatic
    // TODO isDragging() method
    var pressed: Boolean = false
        @JvmName("isPressed") get
        private set

    /** If none of the mouse buttons are pressed of held. */
    @JvmStatic
    val released: Boolean
        @JvmName("isReleased") get() = !pressed

    @JvmStatic
    var clicks: Int = 0 // doesn't work with GL yet
        private set

    // Game Loop Methods

    private fun pollMouseButtons() {
        for (button in buttons) {
            if (button.down) {
                button.updateIfDown()
            } else {
                button.setReleased()
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

    fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        if (!button.isValidIndex()) return
        lastButton = button
        lastAction = action
        when (action) {
            GLFW_PRESS -> {
                setButtonDown(button, true)
                pressed = true
            }

            GLFW_RELEASE -> {
                setButtonDown(button, false)
                pressed = false
            }
        }
        pollMouseButtons()
    }

    override fun mousePressed(e: MouseEvent) {
        if (e.button.isValidIndex()) {
            setButtonDown(e.button, true)
            pressed = true
            clicks = e.clickCount
        }
    }

    override fun mouseReleased(e: MouseEvent) {
        if (e.button.isValidIndex()) {
            setButtonDown(e.button, false)
            setMouseDisp(0, 0)
            pressed = false
            clicks = 0
        }
    }

    // Mouse Movement Callbacks

    fun mousePosCallback(window: Long, xPos: Double, yPos: Double) {
        if (pressed) setMouseDisp(xPos - mousePosPx.x, yPos - mousePosPx.y)
        setMousePos(xPos, yPos)
    }

    override fun mouseMoved(e: MouseEvent) {
        setMousePos(e.x, e.y)
    }

    override fun mouseDragged(e: MouseEvent) {
        pressed = true
        setMouseDisp(e.x - mousePosPx.x, e.y - mousePosPx.y)
        setMousePos(e.x, e.y)
    }

    private fun setMousePos(x: Number, y: Number) {
        mousePosPx.set(x.toFloat(), y.toFloat())
    }

    private fun setMouseDisp(dx: Number, dy: Number) {
        mouseDispPx.set(dx.toFloat(), dy.toFloat())
    }

    // Mouse Scroll Callbacks

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

    private fun buttonDown(button: Int): Boolean {
        return button.isValidIndex() && !buttons[button].released
    }

    private fun buttonPressed(button: Int): Boolean {
        return button.isValidIndex() && buttons[button].pressed
    }

    @JvmStatic
    fun buttonDown(button: Button?): Boolean {
        return when {
            button == null -> false
            Mayonez.useGL -> buttonDown(button.glCode)
            else -> buttonDown(button.awtCode)
        }
    }

    @JvmStatic
    fun buttonPressed(button: Button?): Boolean {
        return when {
            button == null -> false
            Mayonez.useGL -> buttonPressed(button.glCode)
            else -> buttonPressed(button.awtCode)
        }
    }

    @JvmStatic
    fun buttonDown(buttonName: String): Boolean {
        val buttonWithName = Button.values()
            .find { it.toString().equals(buttonName, ignoreCase = true) }
        return buttonDown(buttonWithName)
    }

    @JvmStatic
    fun buttonPressed(buttonName: String): Boolean {
        val buttonWithName = Button.values()
            .find { it.toString().equals(buttonName, ignoreCase = true) }
        return buttonPressed(buttonWithName)
    }

    // Mouse Position Getters

    @JvmStatic
    val screenPos: Vec2
        get() = mousePosPx

    @JvmStatic
    val position: Vec2
        get() = SceneManager.currentScene.camera.toWorld(mousePosPx)

    // Mouse Displacement Getters

    @JvmStatic
    val screenDisp: Vec2
        get() = mouseDispPx

    @JvmStatic
    val displacement: Vec2
        get() = mouseDispPx.invertY().toWorld()


    // Helper Methods

    private fun Vec2.invertY(): Vec2 = this * Vec2(1f, -1f)

    private fun Vec2.toWorld(): Vec2 = this / SceneManager.currentScene.scale

    private fun Int.isValidIndex(): Boolean = this in 0 until NUM_BUTTONS

    private fun setButtonDown(button: Int, down: Boolean) {
        buttons[button].down = down
    }

}