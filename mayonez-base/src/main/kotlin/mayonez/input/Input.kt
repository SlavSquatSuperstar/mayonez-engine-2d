package mayonez.input

import mayonez.math.*
import mayonez.util.*

/**
 * Allows the user to query input events from any class using the window's
 * input managers.
 *
 * @author SlavSquatSuperstar
 */
object Input {

    // TODO make nullable?
    private lateinit var keyboardInstance: KeyManager
    private lateinit var mouseInstance: MouseManager

    @JvmStatic
    fun setKeyboardInstance(keyboard: KeyManager) {
        this.keyboardInstance = keyboard
    }

    @JvmStatic
    fun setMouseInstance(mouse: MouseManager) {
        this.mouseInstance = mouse
    }

    // Key Getters

    @JvmStatic
    fun keyDown(key: Key?): Boolean {
        return keyboardInstance.keyDown(key)
    }

    @JvmStatic
    fun keyPressed(key: Key?): Boolean {
        return keyboardInstance.keyPressed(key)
    }

    /**
     * Whether the user has started pressing the [mayonez.input.Key] with the
     * specified name this frame.
     *
     * @param keyName the name of the key
     * @return if the specified key is pressed
     */
    @JvmStatic
    fun keyDown(keyName: String): Boolean {
        return keyboardInstance.keyDown(Key.findWithName(keyName))
    }

    /**
     * Whether the user is continuously holding down the [mayonez.input.Key]
     * with the specified name.
     *
     * @param keyName the name of the key
     * @return if the specified key is pressed
     */
    @JvmStatic
    fun keyPressed(keyName: String): Boolean {
        return keyboardInstance.keyPressed(Key.findWithName(keyName))
    }

    /**
     * Get the value of the specified [mayonez.input.KeyAxis].
     *
     * @param axis an axis enum constant
     * @return the axis value, either -1, 0, or 1
     */
    @JvmStatic
    fun getAxis(axis: KeyAxis?): Int {
        return axis?.value() ?: 0
    }

    /**
     * Get the value of the [mayonez.input.KeyAxis] with the specified name.
     *
     * @param axisName the name of the axis
     * @return the axis value, either -1, 0, or 1
     */
    @JvmStatic
    fun getAxis(axisName: String): Int {
        return getAxis(
            StringUtils.findConstantWithName(KeyAxis.values(), axisName)
        )
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