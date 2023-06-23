package mayonez.input

import mayonez.math.*

/**
 * Allows the user to query input events from any class using the window's
 * input managers.
 *
 * @author SlavSquatSuperstar
 */
object Input {

    private lateinit var mouseInstance: MouseManager

    @JvmStatic
    fun setMouseInstance(mouse: MouseManager) {
        this.mouseInstance = mouse
    }

    // Mouse State Getters

    /** If any of the mouse buttons are pressed or held. */

    // TODO isDragging() method
    @JvmStatic
    fun isMousePressed(): Boolean {
        return mouseInstance.pressed
    }

    /** If none of the mouse buttons are pressed or held. */
    @JvmStatic
    fun isMouseReleased(): Boolean {
        return !mouseInstance.pressed
    }

    /** The number of clicks in quick succession for the last mouse event. */
    // TODO doesn't work with GL
    @JvmStatic
    fun getMouseClicks(): Int {
        return mouseInstance.clicks
    }

    // Mouse Button Getters

    /**
     * Whether the user is continuously holding down the specified
     * [mayonez.input.Button].
     *
     * @param button a button enum constant
     * @return if the specified button is pressed
     */
    @JvmStatic
    fun buttonDown(button: Button?): Boolean {
        return mouseInstance.buttonDown(button)
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
        return mouseInstance.buttonPressed(button)
    }

    @JvmStatic
    fun buttonDown(buttonName: String): Boolean {
        return mouseInstance.buttonDown(Button.findWithName(buttonName))
    }

    @JvmStatic
    fun buttonPressed(buttonName: String): Boolean {
        return mouseInstance.buttonPressed(Button.findWithName(buttonName))
    }

    // Mouse Movement Getters

    /** Get the position of the cursor in the world, in world units. */
    @JvmStatic
    val mousePosition: Vec2
        get() = mouseInstance.position

    /** Get the position of the cursor in the screen, in pixels */
    @JvmStatic
    val mouseScreenPosition: Vec2
        get() = mouseInstance.screenPosition

    /** Get the drag displacement of the cursor in the world, in world units. */
    @JvmStatic
    val mouseDisplacement: Vec2
        get() = mouseInstance.displacement

    /** Get the drag displacement of the cursor in the screen, in pixels */
    @JvmStatic
    val mouseScreenDisplacement: Vec2
        get() = mouseInstance.screenDisplacement

    // TODO Mouse Scroll Getters

}