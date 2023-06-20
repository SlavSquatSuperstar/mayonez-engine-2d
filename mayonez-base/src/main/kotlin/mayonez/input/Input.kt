package mayonez.input

import mayonez.math.*

/**
 * Allows the user to query input events from any class using the window's
 * input managers.
 *
 * @author SlavSquatSuperstar
 */
object Input {

    // TODO make nullable?
    private lateinit var keyboardInstance: KeyInput
    private lateinit var mouseInstance: MouseInput

    @JvmStatic
    fun setKeyboardInstance(keyboard: KeyInput) {
        this.keyboardInstance = keyboard
    }

    @JvmStatic
    fun setMouseInstance(mouse: MouseInput) {
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

    @JvmStatic
    fun keyDown(keyName: String): Boolean {
        return keyboardInstance.keyDown(keyName)
    }

    @JvmStatic
    fun keyPressed(keyName: String): Boolean {
        return keyboardInstance.keyPressed(keyName)
    }

    @JvmStatic
    fun getAxis(axisName: String): Int {
        return keyboardInstance.getAxis(axisName)
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

    @JvmStatic
    fun buttonDown(button: Button?): Boolean {
        return mouseInstance.buttonDown(button)
    }

    @JvmStatic
    fun buttonPressed(button: Button?): Boolean {
        return mouseInstance.buttonPressed(button)
    }

    @JvmStatic
    fun buttonDown(buttonName: String): Boolean {
        return mouseInstance.buttonDown(buttonName)
    }

    @JvmStatic
    fun buttonPressed(buttonName: String): Boolean {
        return mouseInstance.buttonPressed(buttonName)
    }

    // Mouse Movement Getters

    /** Get the position of the cursor in the world, in world units. */
    @JvmStatic
    val mousePosition: Vec2
        get() = mouseInstance.position

    /** Get the position of the cursor in the screen, in pixels */
    @JvmStatic
    val mouseScreenPosition: Vec2
        get() = mouseInstance.screenPos

    @JvmStatic
    val mouseDisplacement: Vec2
        get() = mouseInstance.displacement

    @JvmStatic
    val mouseScreenDisplacement: Vec2
        get() = mouseInstance.screenDisp

    // TODO Mouse Scroll Getters

}