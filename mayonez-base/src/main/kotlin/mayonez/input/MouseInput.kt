package mayonez.input

import mayonez.*
import mayonez.math.*
import org.lwjgl.glfw.GLFW
import java.awt.event.*

/**
 * Receives mouse input events.
 *
 * @author SlavSquatSuperstar
 */
@Suppress("unused")
object MouseInput : MouseAdapter() {

    // Mouse Button Fields
    // Use arrays instead of hashmaps because very few buttons, GLFW uses max 8
    private const val NUM_BUTTONS: Int = 8
    private val buttons: Array<MappingStatus> = Array(NUM_BUTTONS) { MappingStatus() }
    private var lastButton: Int = -1
    private var lastAction: Int = -1

    // Mouse Movement Fields
    private var mousePosPx = Vec2()
        private set
    private var mouseDispPx = Vec2() // drag displacement

    // Mouse Scroll Fields
    private var scroll = Vec2()

    // Mouse State Properties

    @JvmStatic
    var pressed: Boolean = false
        @JvmName("isPressed") get
        private set

    @JvmStatic
    val released: Boolean
        @JvmName("isReleased") get() = !pressed

    @JvmStatic
    var clicks: Int = 1
        private set

    // Game Loop Methods

    fun endFrame() {
        // Update mouse input
        pollMouseButtons()

        /// Reset motion
        setMouseDisp(0, 0)
        setScrollPos(0, 0)
    }

    private fun pollMouseButtons() {
        for (button in buttons) {
            if (button.down) {
                button.updateIfDown()
            } else {
                button.setReleased()
            }
        }
    }

    // Mouse Button Callbacks

    /**
     * The mouse button callback method for GLFW.
     *
     * @param window the window id
     * @param button the GLFW button code
     * @param action the event type
     * @param mods any modifier keys
     */
    fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        if (!button.isValidIndex()) return
        lastButton = button
        lastAction = action
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
        pollMouseButtons()
    }

    override fun mousePressed(e: MouseEvent) {
        setButtonDown(e.button, true)
        pressed = true
        clicks = e.clickCount
    }

    override fun mouseReleased(e: MouseEvent) {
        setButtonDown(e.button, false)
        pressed = false
        clicks = e.clickCount
        setMouseDisp(0, 0)
    }

    // Mouse Movement Callbacks

    /**
     * The mouse position callback method for GLFW.
     *
     * @param window the window id
     * @param xPos the x position of the cursor
     * @param yPos the y position of the cursor
     */
    fun mousePosCallback(window: Long, xPos: Double, yPos: Double) {
        if (pressed) {
            setMouseDisp(xPos - mousePosPx.x, yPos - mousePosPx.y)
        }
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

    // Mouse Scroll Callbacks

    /**
     * The mouse scree callback method for GLFW.
     *
     * @param window the window id
     * @param xOffset the x offset of the scroll wheel
     * @param yOffset the y offset of the scroll wheel
     */
    fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        setScrollPos(xOffset, yOffset)
    }

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        setScrollPos(e.x, e.y)
    }

    // Mouse Button Getters

    /**
     * Whether the user is continuously holding down the specified
     * [mayonez.input.Button].
     *
     * @param button a button enum constant
     * @return if the specified button is held
     */
    @JvmStatic
    fun buttonDown(button: Button?): Boolean {
        return when {
            button == null -> false
            Mayonez.useGL -> buttonDown(button.glCode)
            else -> buttonDown(button.awtCode)
        }
    }

    /**
     * Whether the user has started pressing the specified
     * [mayonez.input.Button] this frame.
     *
     * @param button a button enum constant
     * @return if the specified button is pressed
     */
    @JvmStatic
    fun buttonPressed(button: Button?): Boolean {
        return when {
            button == null -> false
            Mayonez.useGL -> buttonPressed(button.glCode)
            else -> buttonPressed(button.awtCode)
        }
    }

    /**
     * Whether the user is continuously holding down the [mayonez.input.Button]
     * with the specified name.
     *
     * @param buttonName the name of the button
     * @return if the specified button is held
     */
    @JvmStatic
    fun buttonDown(buttonName: String): Boolean {
        return buttonDown(Button.findWithName(buttonName))
    }

    /**
     * Whether the user is started pressing the [mayonez.input.Button] with the
     * specified name this frame.
     *
     * @param buttonName the name of the button
     * @return if the specified button is pressed
     */
    @JvmStatic
    fun buttonPressed(buttonName: String): Boolean {
        return buttonPressed(Button.findWithName(buttonName))
    }

    // Mouse Movement Getters

    /** Get the position of the cursor in the screen, in pixels */
    @JvmStatic
    val screenPosition: Vec2
        get() = mousePosPx

    /** Get the position of the cursor in the world, in world units. */
    @JvmStatic
    val position: Vec2
        get() = SceneManager.currentScene.camera.toWorld(mousePosPx)

    /** Get the drag displacement of the cursor in the world, in world units. */
    @JvmStatic
    val screenDisplacement: Vec2
        get() = mouseDispPx

    /** Get the drag displacement of the cursor in the screen, in pixels */
    @JvmStatic
    val displacement: Vec2
        get() = mouseDispPx.invertY().toWorld()

    private fun Vec2.invertY(): Vec2 = this * Vec2(1f, -1f)
    private fun Vec2.toWorld(): Vec2 = this / SceneManager.currentScene.scale

    // TODO Mouse Scroll Getters

    // Mouse Button Helper Methods

    private fun buttonDown(button: Int): Boolean {
        return button.isValidIndex() && !buttons[button].released
    }

    private fun buttonPressed(button: Int): Boolean {
        return button.isValidIndex() && buttons[button].pressed
    }

    private fun setButtonDown(button: Int, down: Boolean) {
        buttons[button].down = down
    }

    // Mouse Movement Helper Methods

    private fun setMousePos(x: Number, y: Number) {
        mousePosPx.set(x.toFloat(), y.toFloat())
    }

    private fun setMouseDisp(dx: Number, dy: Number) {
        mouseDispPx.set(dx.toFloat(), dy.toFloat())
    }

    // Mouse Scroll Helper Methods

    private fun setScrollPos(scrollX: Number, scrollY: Number) {
        scroll.set(scrollX.toFloat(), scrollY.toFloat())
    }

    private fun Int.isValidIndex(): Boolean = this in 0 until NUM_BUTTONS

}