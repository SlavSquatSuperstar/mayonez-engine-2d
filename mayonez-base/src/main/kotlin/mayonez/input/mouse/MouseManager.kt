package mayonez.input.mouse

import mayonez.input.*
import mayonez.math.*
import java.awt.event.*

private const val NUM_BUTTONS: Int = 8

/**
 * Receives mouse input events.
 *
 * @author SlavSquatSuperstar
 */
sealed class MouseManager : MouseAdapter() {

    // Mouse Button Fields
    // Use arrays instead of hashmaps because very few buttons, GLFW uses max 8
    private val buttons: Array<MappingStatus> = Array(NUM_BUTTONS) { MappingStatus() } // All button states
    private val buttonsDown: MutableSet<Int> = HashSet(NUM_BUTTONS) // Buttons down this frame

    // Mouse State Fields
    internal var anyButtonDown: Boolean = false
        private set
    internal var clicks: Int = 1

    // Mouse Movement Fields
    internal var mousePosPx = Vec2()
        private set
    internal var mouseDispPx = Vec2() // drag displacement
        private set

    // Mouse Scroll Fields
    internal var scroll = Vec2()
        private set

    /** Poll mouse events from the window. */
    fun updateMouse() {
        // Update mouse input
        updateButtons()

        // Reset motion
        setMouseDisp(0, 0)
        setScrollPos(0, 0)
    }

    protected fun updateButtons() {
        for (buttonState in buttons) {
            if (buttonState.down) buttonState.updateIfDown()
            else buttonState.setReleased()
        }
        anyButtonDown = buttonsDown.isNotEmpty()
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
    open fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
    }

    // Mouse Movement Callbacks

    /**
     * The mouse position callback method for GLFW.
     *
     * @param window the window id
     * @param xPos the x position of the cursor
     * @param yPos the y position of the cursor
     */
    open fun mousePosCallback(window: Long, xPos: Double, yPos: Double) {
    }

    // Mouse Scroll Callbacks

    /**
     * The mouse scroll callback method for GLFW.
     *
     * @param window the window id
     * @param xOffset the x offset of the scroll wheel
     * @param yOffset the y offset of the scroll wheel
     */
    open fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
    }

    // Mouse Button Getters

    /**
     * Whether the user is continuously holding down the specified
     * [mayonez.input.Button].
     *
     * @param button a button enum constant
     * @return if the specified button is held
     */
    internal abstract fun buttonDown(button: Button?): Boolean

    /**
     * Whether the user has started pressing the specified
     * [mayonez.input.Button] this frame.
     *
     * @param button a button enum constant
     * @return if the specified button is pressed
     */
    internal abstract fun buttonPressed(button: Button?): Boolean

    // Mouse Button Helper Methods

    protected fun buttonDown(button: Int): Boolean {
        return button.isValidIndex() && !buttons[button].released
    }

    protected fun buttonPressed(button: Int): Boolean {
        return button.isValidIndex() && buttons[button].pressed
    }

    protected fun setButtonDown(button: Int, down: Boolean) {
        buttons[button].down = down
        if (down) buttonsDown.add(button)
        else buttonsDown.remove(button)
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

    protected fun Int.isValidIndex(): Boolean = this in 0..<NUM_BUTTONS

}