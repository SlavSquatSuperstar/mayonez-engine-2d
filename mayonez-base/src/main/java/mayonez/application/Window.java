package mayonez.application;

import mayonez.input.*;
import mayonez.math.*;

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

    /**
     * How much the window's UI and text elements should be scaled by.
     * The content scale is usually 1x1, but may be higher on high-DPI displays
     * or if OS display  scaling is active.
     *
     * @return the content scale
     */
    Vec2 getContentScale();

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
     * Get the current time of the application in seconds. The time relative to an
     * arbitrary point, which is not guaranteed to be when the program started.
     *
     * @return the time in seconds
     */
    float getCurrentTimeSecs();

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

    // Full Screen Methods

    /**
     * Whether this window is in full screen mode.
     *
     * @return if the window is full screen
     */
    boolean isFullScreen();

    /**
     * Set whether this window should be in full screen mode.
     * <p>
     * Note that native macOS (Cocoa) full screen is different from either
     * GLFW or AWT full screen.
     *
     * @param fullScreen whether to use full screen
     */
    void setFullScreen(boolean fullScreen);

}
