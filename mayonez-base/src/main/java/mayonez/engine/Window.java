package mayonez.engine;

import mayonez.event.Receivable;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;

/**
 * An interface containing common functionality for all game windows.
 *
 * @author SlavSquatSuperstar
 */
public sealed interface Window extends GameLayer permits JWindow, GLWindow {

    // Property Getters

    String getTitle();

    int getWidth();

    int getHeight();

    // Resource Management Methods

    /**
     * Initialize the window and setup system resources.
     */
    @Override
    void start();

    /**
     * Destroy the window and free system resources.
     */
    @Override
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
     * @param r a draw function
     */
    void render(Receivable r);

    /**
     * Reset events and input listeners.
     */
    void endFrame();

    // Input Methods

    /**
     * Set the keyboard listener for this window.
     *
     * @param keyboard a {@link KeyInput} instance
     */
    void setKeyInput(KeyInput keyboard);

    /**
     * Set the mouse listener for this window.
     *
     * @param mouse a {@link MouseInput} instance
     */
    void setMouseInput(MouseInput mouse);

}
