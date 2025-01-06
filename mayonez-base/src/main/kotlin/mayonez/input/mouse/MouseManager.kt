package mayonez.input.mouse

import mayonez.input.*
import mayonez.input.events.*
import mayonez.math.*
import java.awt.event.*

private const val NUM_BUTTONS: Int = 8 // GLFW supports eight mouse buttons
private const val DOUBLE_CLICK_TIME_SECS: Float = 0.40f

/**
 * Receives and stores mouse input events from the window.
 *
 * @author SlavSquatSuperstar
 */
sealed class MouseManager : MouseAdapter() {

    // Mouse Button Fields
    private val buttons: MutableMap<Int, InputState> = HashMap(NUM_BUTTONS) // All button states
    private val buttonsDown: MutableSet<Int> = HashSet(NUM_BUTTONS) // Buttons down this frame

    internal var anyButtonDown: Boolean = false
        private set
    internal var doubleClick: Boolean = false
        private set
    private var lastClickTimeSecs: Double = 0.0

    // Mouse Movement Fields
    internal var mousePosPx = Vec2()
        private set
    internal var mouseDispPx = Vec2() // drag displacement
        private set

    // Mouse Scroll Fields
    internal var scroll = Vec2()
        private set

    // Event Methods

    /** Poll mouse events from the window. */
    fun updateMouse() {
        // Update mouse input
        updateButtons()

        // Reset double click
        doubleClick = false

        // Reset motion
        setMouseDisp(0f, 0f)
        setScrollPos(0f, 0f)
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

    protected fun onMouseInputEvent(event: MouseInputEvent) {
        when (event) {
            is MouseButtonEvent -> onMouseButtonEvent(event)
            is MouseMoveEvent -> onMouseMoveEvent(event)
            is MouseScrollEvent -> setScrollPos(event.xOffset, event.yOffset)
        }
    }

    private fun onMouseButtonEvent(event: MouseButtonEvent) {
        // Set button down
        val button = event.button
        if (event.isButtonDown) {
            buttonsDown.add(button)
            // Track new button
            if (!buttons.containsKey(button)) buttons[button] = InputState.NONE
        } else {
            buttonsDown.remove(button)
        }
        updateButtons()

        // Set click time
        if (event.isButtonDown) {
            // Detect double click
            // Source: https://www.youtube.com/watch?v=k3rVEIr0Z7w
            if (event.eventTime - lastClickTimeSecs <= DOUBLE_CLICK_TIME_SECS)
                doubleClick = true
            lastClickTimeSecs = event.eventTime
        } else {
            setMouseDisp(0f, 0f)
        }
    }

    private fun onMouseMoveEvent(event: MouseMoveEvent) {
        if (anyButtonDown) {
            // Set drag displacement
            setMouseDisp(event.mouseX - mousePosPx.x, event.mouseY - mousePosPx.y)
        }
        mousePosPx.set(event.mouseX, event.mouseY)
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

    // Mouse Getters and Setters

    protected fun buttonDown(button: Int): Boolean {
        return button in buttonsDown
    }

    protected fun buttonPressed(button: Int): Boolean {
        return buttons[button] == InputState.PRESSED
    }

    private fun setMouseDisp(dx: Float, dy: Float) {
        mouseDispPx.set(dx, dy)
    }

    private fun setScrollPos(scrollX: Float, scrollY: Float) {
        scroll.set(scrollX, scrollY)
    }

}