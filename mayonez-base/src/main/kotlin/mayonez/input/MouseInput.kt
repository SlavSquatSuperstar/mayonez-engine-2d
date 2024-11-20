package mayonez.input

import mayonez.input.mouse.*
import mayonez.math.*

/**
 * Receives mouse input events.
 *
 * @author SlavSquatSuperstar
 */
object MouseInput {

    // Singleton Properties

    private lateinit var instance: MouseManager

    /**
     * Create the [MouseManager] instance if it does not exist.
     *
     * @param useGL whether to use GLFW instead of AWT.
     * @return the mouse input instance
     */
    internal fun createInstance(useGL: Boolean): MouseManager {
        if (this::instance.isInitialized) return instance
        instance = if (useGL) GLMouseManager() else JMouseManager()
        return instance
    }

    // Scene Properties

    private lateinit var pointXf: PointTransformer

    fun setPointTransformer(pointXf: PointTransformer) {
        this.pointXf = pointXf
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
    fun buttonDown(button: Button?): Boolean = instance.buttonDown(button)

    /**
     * Whether the user has started pressing the specified
     * [mayonez.input.Button] this frame.
     *
     * @param button a button enum constant
     * @return if the specified button is pressed
     */
    @JvmStatic
    fun buttonPressed(button: Button?): Boolean = instance.buttonPressed(button)

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

    /**
     * Get the position of the cursor on the screen, in pixels.
     *
     * @return the screen position
     */
    @JvmStatic
    fun getScreenPosition(): Vec2 = instance.mousePosPx

    /**
     * Get the position of the cursor in the scene, in world units.
     *
     * @return the world position
     */
    @JvmStatic
    fun getPosition(): Vec2 {
        return pointXf.toWorldPosition(getScreenPosition())
    }

    /**
     * Get the drag displacement of the cursor on the screen, in pixels.
     *
     * @return the screen displacement
     */
    @JvmStatic
    fun getScreenDisplacement(): Vec2 = instance.mouseDispPx

    /**
     * Get the drag displacement of the cursor in the scene, in world units.
     *
     * @return the world displacement
     */
    @JvmStatic
    fun getDisplacement(): Vec2 {
        return pointXf.toWorldDisplacement(getScreenDisplacement())
    }

    // Mouse Scroll Getters

    fun getScroll(): Vec2 = instance.scroll

    // Mouse State Getters

    @JvmStatic
    fun isPressed(): Boolean = (instance.buttonsPressed > 0)

    @JvmStatic
    fun isReleased(): Boolean = (instance.buttonsPressed == 0)

    @JvmStatic
    fun getClicks(): Int = instance.clicks

}