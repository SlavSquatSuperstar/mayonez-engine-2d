package mayonez.application;

import mayonez.input.keyboard.*;
import mayonez.input.mouse.*;

/**
 * The main window that renders the application to the screen and detects
 * input events.
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
     * Setup system resources and show the window.
     */
    void start();

    /**
     * Free system resources and destroy the window.
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
     * Redraw the game to the screen.
     */
    void render();

    /**
     * Reset events and update input listeners.
     */
    void endFrame();

    // Input Methods

    /**
     * The keyboard listener for this window, used to pass events to the application.
     *
     * @return the key input handler
     */
    KeyInputHandler getKeyInputHandler();

    /**
     * The mouse listener for this window, used to pass events to the application.
     *
     * @return the mouse input handler
     */
    MouseInputHandler getMouseInputHandler();

    /**
     * Set the mouse listener for this window and mark it as the active
     * instance for the input manager.
     *
     * @param mouse a {@link mayonez.input.mouse.MouseManager} instance
     */
    default void setMouseInput(MouseManager mouse) {}

}
