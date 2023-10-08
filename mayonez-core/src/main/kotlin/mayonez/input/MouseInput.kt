package mayonez.input

import mayonez.input.mouse.*
import mayonez.math.*

/**
 * Receives mouse input events.
 *
 * @author SlavSquatSuperstar
 */
object MouseInput {

    // Mouse Fields
    private lateinit var instance: MouseManager

    // Game Fields
    private var invSceneScale: Float = 1f
    private var pointXf: PointTransformer? = null

    // Game Methods

    @JvmStatic
    fun getInstance(): MouseManager = instance

    // Game Methods
    fun setUseGL(useGL: Boolean) {
        instance = if (useGL) GLMouseManager() else JMouseManager()
    }

    fun setSceneScale(sceneScale: Float) {
        this.invSceneScale = 1f / sceneScale
    }

    fun setPointTransformer(pointXf: PointTransformer) {
        this.pointXf = pointXf
    }

    // Game Loop Methods

    /** Poll mouse events from the window. */
    fun updateMouse() {
        instance.updateMouse()
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
    fun getPosition(): Vec2 = pointXf?.toWorld(getScreenPosition()) ?: Vec2()

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
    fun getDisplacement(): Vec2 = getScreenDisplacement().invertY().toWorld()

    // Mouse Scroll Getters

    fun getScroll(): Vec2 = instance.scroll

    // Mouse State Getters

    @JvmStatic
    fun isPressed(): Boolean = (instance.buttonsPressed > 0)

    @JvmStatic
    fun isReleased(): Boolean = (instance.buttonsPressed == 0)

    @JvmStatic
    fun getClicks(): Int = instance.clicks

    // Mouse Button Helper Methods

    private fun Vec2.invertY(): Vec2 = this * Vec2(1f, -1f)

    private fun Vec2.toWorld(): Vec2 = this * invSceneScale

}