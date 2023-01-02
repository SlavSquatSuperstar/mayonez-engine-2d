package mayonez.engine;

import mayonez.Scene;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;

/**
 * An interface containing common functionality for all game windows.
 *
 * @author SlavSquatSuperstar
 */
public sealed interface Window permits JWindow, GLWindow {

    // Property Getters

    String getTitle();

    int getWidth();

    int getHeight();

    // Resource Management Methods

    /**
     * Initialize the window and setup system resources.
     */
    void start();

    /**
     * Destroy the window and free system resources.
     */
    void stop();

    // Game Loop Methods

    /**
     * Whether the window is still open or has been closed (x-ed out) by the user.
     *
     * @return if the window is not closed
     */
    boolean notClosedByUser();

    /**
     * Poll any input or window events.
     */
    void beginFrame();

    /**
     * Draw the game to the screen.
     *
     * @param scene the scene the draw
     */
    void render(Scene scene);

    /**
     * Reset events and input listeners.
     */
    void endFrame();

    // Input Methods

    /**
     * Set the keyboard listener for this window.
     *
     * @param keyboard a {@link mayonez.input.KeyInput} instance
     */
    void setKeyInput(KeyInput keyboard);

    /**
     * Set the mouse listener for this window.
     *
     * @param mouse a {@link mayonez.input.MouseInput} instance
     */
    void setMouseInput(MouseInput mouse);

}
