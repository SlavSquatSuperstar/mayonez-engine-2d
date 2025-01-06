package mayonez.input.mouse

import mayonez.input.*
import mayonez.input.events.*
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

    // Event Methods

    protected fun onMouseInputEvent(event: MouseInputEvent) {
        InputEvents.MOUSE_EVENTS.broadcast(event)
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

}