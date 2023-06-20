package mayonez.input

import mayonez.*
import mayonez.math.*
import java.awt.event.*

private const val NUM_BUTTONS: Int = 8

/**
 * Receives mouse input events.
 *
 * @author SlavSquatSuperstar
 */
@Suppress("unused")
sealed class MouseManager : MouseAdapter() {

    // Mouse Button Fields
    // Use arrays instead of hashmaps because very few buttons, GLFW uses max 8
    private val buttons: Array<MappingStatus> = Array(NUM_BUTTONS) { MappingStatus() }

    // Mouse Movement Fields
    protected var mousePosPx = Vec2()
        private set
    private var mouseDispPx = Vec2() // drag displacement

    // Mouse Scroll Fields
    private var scroll = Vec2()

    // Mouse State Properties

    internal var pressed: Boolean = false
        private set
    internal var clicks: Int = 1
        private set

    // Game Loop Methods

    protected fun pollMouseButtons() {
        for (button in buttons) {
            if (button.down) {
                button.updateIfDown()
            } else {
                button.setReleased()
            }
        }
    }

    fun endFrame() {
        // Update mouse input
        pollMouseButtons()

        /// Reset motion
        setMouseDisp(0, 0)
        setScrollPos(0, 0)
    }

    // Mouse Button Callbacks

    open fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
    }

    override fun mousePressed(e: MouseEvent) {
    }

    override fun mouseReleased(e: MouseEvent) {
    }

    // Mouse Movement Callbacks

    open fun mousePosCallback(window: Long, xPos: Double, yPos: Double) {
    }

    override fun mouseMoved(e: MouseEvent) {
    }

    override fun mouseDragged(e: MouseEvent) {
    }

    // Mouse Scroll Callbacks

    open fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
    }

    override fun mouseWheelMoved(e: MouseWheelEvent) {
    }

    // Mouse Button Getters

    /**
     * Whether the user is continuously holding down the specified
     * [mayonez.input.Button].
     *
     * @param button a button enum constant
     * @return if the specified button is pressed
     */
    internal fun buttonDown(button: Button?): Boolean {
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
    internal fun buttonPressed(button: Button?): Boolean {
        return when {
            button == null -> false
            Mayonez.useGL -> buttonPressed(button.glCode)
            else -> buttonPressed(button.awtCode)
        }
    }

    // Mouse Movement Getters

    internal val screenPosition: Vec2
        get() = mousePosPx

    internal val position: Vec2
        get() = SceneManager.currentScene.camera.toWorld(mousePosPx)

    internal val screenDisplacement: Vec2
        get() = mouseDispPx

    internal val displacement: Vec2
        get() = mouseDispPx.invertY().toWorld()

    // Mouse Button Helper Methods

    private fun buttonDown(button: Int): Boolean {
        return button.isValidIndex() && !buttons[button].released
    }

    private fun buttonPressed(button: Int): Boolean {
        return button.isValidIndex() && buttons[button].pressed
    }

    protected fun setButtonDown(button: Int, down: Boolean) {
        buttons[button].down = down
    }

    protected fun setPressed(pressed: Boolean) {
        this.pressed = pressed
    }

    protected fun setClicks(clicks: Int){
        this.clicks = clicks
    }

    // Mouse Movement Helper Methods

    protected fun setMousePos(x: Number, y: Number) {
        mousePosPx.set(x.toFloat(), y.toFloat())
    }

    protected fun setMouseDisp(dx: Number, dy: Number) {
        mouseDispPx.set(dx.toFloat(), dy.toFloat())
    }

    // Mouse Scroll Helper Methods

    protected fun setScrollPos(scrollX: Number, scrollY: Number) {
        scroll.set(scrollX.toFloat(), scrollY.toFloat())
    }

    protected fun Int.isValidIndex(): Boolean = this in 0 until NUM_BUTTONS

}

// Helper Extensions
private fun Vec2.invertY(): Vec2 = this * Vec2(1f, -1f)
private fun Vec2.toWorld(): Vec2 = this / SceneManager.currentScene.scale