package mayonez.input

import mayonez.input.events.*
import mayonez.math.*

/**
 * Receives and stores mouse input events from the window.
 *
 * Usage: To query if a button is held, call [MouseInput.buttonDown]. To query if a
 * button was just pressed this frame, call [MouseInput.buttonPressed].
 *
 * @author SlavSquatSuperstar
 */
object MouseInput {

    // Constants
    private const val NUM_BUTTONS: Int = 8 // GLFW supports eight mouse buttons
    private const val DOUBLE_CLICK_TIME_SECS: Float = 0.40f

    // Mouse Button Fields
    private val buttons: MutableMap<Int, InputState> = HashMap(NUM_BUTTONS) // All button states
    private val buttonsDown: MutableSet<Int> = HashSet(NUM_BUTTONS) // Buttons down this frame
    private val anyButtonDown: Boolean
        get() = buttonsDown.isNotEmpty()
    private var doubleClick: Boolean = false // TODO double click with same button
    private var lastClickTimeSecs: Float = 0f

    // Mouse Movement Fields
    private var mousePosPx = Vec2()
    private var mouseDispPx = Vec2() // drag displacement

    // Mouse Scroll Fields
    private var scroll = Vec2()

    // Event Fields
    private lateinit var handler: MouseInputHandler

    // Transform Properties

    private lateinit var pointXf: PointTransformer

    fun setPointTransformer(pointXf: PointTransformer) {
        this.pointXf = pointXf
    }

    // Event Methods

    /**
     * Set the mouse event generator instance for the application.
     *
     * @param handler the key input handler
     */
    @JvmStatic
    fun setHandler(handler: MouseInputHandler) {
        // Replace event generator
        if (this::handler.isInitialized) this.handler.eventSystem.unsubscribeAll()
        this.handler = handler
        handler.eventSystem.subscribe { event -> onMouseInputEvent(event) }
    }

    private fun onMouseInputEvent(event: MouseInputEvent) {
        when (event) {
            is MouseButtonEvent -> onMouseButtonEvent(event)
            is MouseMoveEvent -> onMouseMoveEvent(event)
            is MouseScrollEvent -> scroll.set(event.xOffset, event.yOffset)
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

        // Set click time
        if (event.isButtonDown) {
            // Detect double click
            // Source: https://www.youtube.com/watch?v=k3rVEIr0Z7w
            if (event.eventTime - lastClickTimeSecs <= DOUBLE_CLICK_TIME_SECS)
                doubleClick = true
            lastClickTimeSecs = event.eventTime
        } else {
            mouseDispPx.set(0f, 0f)
        }
    }

    private fun onMouseMoveEvent(event: MouseMoveEvent) {
        if (anyButtonDown) {
            // Set drag displacement
            mouseDispPx.set(event.mouseX - mousePosPx.x, event.mouseY - mousePosPx.y)
        }
        mousePosPx.set(event.mouseX, event.mouseY)
    }

    // Update Methods

    /**
     * Update input states for all buttons and reset click and motion states.
     */
    @JvmStatic
    fun updateMouse() {
        // Update mouse buttons
        for ((button, value) in buttons) {
            // Update button state
            val buttonDown = button in buttonsDown
            val newState = value.getNextState(buttonDown)
            buttons[button] = newState
        }

        // Reset other states
        doubleClick = false
        mouseDispPx.set(0f, 0f)
        scroll.set(0f, 0f)
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
        return if (button == null) false
        else buttonDown(handler.getButtonCode(button))
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
        return if (button == null) false
        else buttonPressed(handler.getButtonCode(button))
    }

    /**
     * Whether the user is continuously holding down the [mayonez.input.Button]
     * with the specified name.
     *
     * @param buttonName the name of the button
     * @return if the specified button is held
     */
    @JvmStatic
    fun buttonDown(buttonName: String): Boolean = buttonDown(Button.findWithName(buttonName))

    /**
     * Whether the user is started pressing the [mayonez.input.Button] with the
     * specified name this frame.
     *
     * @param buttonName the name of the button
     * @return if the specified button is pressed
     */
    @JvmStatic
    fun buttonPressed(buttonName: String): Boolean = buttonPressed(Button.findWithName(buttonName))

    @JvmStatic
    fun buttonDown(button: Int): Boolean = button in buttonsDown

    @JvmStatic
    fun buttonPressed(button: Int): Boolean = (buttons[button] == InputState.PRESSED)

    /**
     * If any mouse buttons are held down.
     *
     * @return if the mouse is pressed
     */
    @JvmStatic
    fun isAnyDown(): Boolean = anyButtonDown

    /**
     * If any two (or more) mouse button pressed were registered in rapid succession.
     *
     * @return if the mouse is double-clicked
     */
    @JvmStatic
    fun isDoubleClick(): Boolean = doubleClick

    // Mouse Movement Getters

    /**
     * Get the position of the cursor on the screen, in pixels.
     *
     * @return the screen position
     */
    @JvmStatic
    fun getScreenPosition(): Vec2 = mousePosPx

    /**
     * Get the position of the cursor in the scene, in world units.
     *
     * @return the world position
     */
    @JvmStatic
    fun getPosition(): Vec2 = pointXf.toWorldPosition(getScreenPosition())

    /**
     * Get the drag displacement of the cursor on the screen, in pixels.
     *
     * @return the screen displacement
     */
    @JvmStatic
    fun getScreenDisplacement(): Vec2 = mouseDispPx

    /**
     * Get the drag displacement of the cursor in the scene, in world units.
     *
     * @return the world displacement
     */
    @JvmStatic
    fun getDisplacement(): Vec2 = pointXf.toWorldDisplacement(getScreenDisplacement())

    // Mouse Scroll Getters

    /**
     * Get the scroll displacement of the mouse.
     *
     * @return the scroll
     */
    @JvmStatic
    fun getScroll(): Vec2 = scroll

}