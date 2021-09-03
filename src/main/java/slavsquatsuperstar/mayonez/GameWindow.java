package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.renderer.Renderable;

/**
 * An interface containing common functionality for all game windows.
 *
 * @author SlavSquatSuperstar
 */
public interface GameWindow {

    // Engine Methods

    /**
     * Initialize the window and setup system resources.
     */
    void start();

    /**
     * Destroy the window and free system resources.
     */
    void stop();

    // Game Loop Methods

    boolean isClosedByUser();

    /**
     * Poll any input or game events.
     */
    void beginFrame();

    /**
     * Draw the game to the screen.
     *
     * @param r a draw function
     */
    void render(Renderable r);

    /**
     * Reset input listener states.
     */
    void endFrame();

    // Input Methods

    /**
     * Set the keyboard listener for this window.
     *
     * @param keys a {@link KeyInput} instance
     */
    void setKeyInput(KeyInput keys);

    /**
     * Set the mouse listener for this window.
     *
     * @param mouse a {@link MouseInput} instance
     */
    void setMouseInput(MouseInput mouse);

}
