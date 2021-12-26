package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.renderer.Renderable
import slavsquatsuperstar.mayonez.input.KeyInput
import slavsquatsuperstar.mayonez.input.MouseInput

/**
 * An interface containing common functionality for all game windows.
 *
 * @author SlavSquatSuperstar
 */
interface Window {

    val width: Int
    val height: Int
    val title: String

    // Engine Methods
    /**
     * Initialize the window and setup system resources.
     */
    fun start()

    /**
     * Destroy the window and free system resources.
     */
    fun stop()

    // Game Loop Methods
    fun notClosedByUser(): Boolean

    /**
     * Poll any input or window events.
     */
    fun beginFrame()

    /**
     * Draw the game to the screen.
     *
     * @param r a draw function
     */
    fun render(r: Renderable)

    /**
     * Reset events and input listeners.
     */
    fun endFrame()

    // Input Methods
    /**
     * Set the keyboard listener for this window.
     *
     * @param keyboard a [KeyInput] instance
     */
    fun setKeyInput(keyboard: KeyInput)

    /**
     * Set the mouse listener for this window.
     *
     * @param mouse a [MouseInput] instance
     */
    fun setMouseInput(mouse: MouseInput)

}