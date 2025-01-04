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
    private val buttons: MutableMap<Int, InputState> = HashMap(NUM_BUTTONS) // All button states
    private val buttonsDown: MutableSet<Int> = HashSet(NUM_BUTTONS) // Buttons down this frame

    // Mouse State Fields
    internal var anyButtonDown: Boolean = false
        private set
    internal var doubleClick: Boolean = false
        private set

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

        // Reset double click
        doubleClick = false

        // Reset motion
        setMouseDisp(0, 0)
        setScrollPos(0, 0)
    }

    protected fun updateButtons() {
        for ((button, value) in buttons) {
            // Update button state
            val buttonDown = button in buttonsDown
            val newState = value.getNextState(buttonDown)
            buttons[button] = newState
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

    // Mouse Button Getters and Setters

    protected fun setButtonDown(button: Int, buttonDown: Boolean) {
        if (buttonDown) {
            buttonsDown.add(button)
            // Track new button
            if (!buttons.containsKey(button)) buttons[button] = InputState.NONE
        } else {
            buttonsDown.remove(button)
        }
    }

    protected fun buttonDown(button: Int): Boolean {
        return button in buttonsDown
    }

    protected fun buttonPressed(button: Int): Boolean {
        return buttons[button] == InputState.PRESSED
    }

    protected fun setDoubleClick(doubleClick: Boolean) {
        this.doubleClick = doubleClick
    }

    // Mouse Movement Setters

    protected fun setMousePos(x: Number, y: Number) {
        mousePosPx.set(x.toFloat(), y.toFloat())
    }

    protected fun setMouseDisp(dx: Number, dy: Number) {
        mouseDispPx.set(dx.toFloat(), dy.toFloat())
    }

    // Mouse Scroll Setters

    protected fun setScrollPos(scrollX: Number, scrollY: Number) {
        scroll.set(scrollX.toFloat(), scrollY.toFloat())
    }

}