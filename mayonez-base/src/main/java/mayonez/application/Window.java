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

    /**
     * The title of the window, shown in the title bar.
     *
     * @return the title
     */
    String getTitle();

    /**
     * The total width of the window with the decorations, in screen
     * units. Also see {@link #getContentWidth()}.
     *
     * @return the total width
     */
    int getWidth();

    /**
     * The total height of the window with the decorations, in screen
     * units. Also see {@link #getContentHeight()}.
     *
     * @return the total height
     */
    int getHeight();

    /**
     * The unobscured width of the window content area without the
     * decorations, in screen units. Also see {@link #getWidth()}
     *
     * @return the content width
     */
    int getContentWidth();

    /**
     * The unobscured height of the window content area without the
     * decorations, in screen units. Also see {@link #getHeight()}
     *
     * @return the content height
     */
    int getContentHeight();

    /**
     * How much the window's UI and text elements should be scaled by.
     * The content scale is usually 1x1, but may be higher on high-DPI displays
     * or if OS display scaling is active.
     *
     * @return the content scale
     */
    Vec2 getContentScale();

    // Resource Management Methods

    /**
     * Show the window and acquire its graphics resources.
     */
    void start();

    /**
     * Destroy the window and release its graphics resources.
     */
    void stop();

    // Game Loop Methods

    /**
     * Get the current time of the application in seconds. The time is relative to an
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
     * Poll input and window events and update listeners.
     */
    void pollEvents();

    /**
     * Redraw the game to the screen.
     */
    void render();

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
     * Note that native macOS (Cocoa) full screen is not considered
     * full screen by either GLFW or AWT libaries.
     *
     * @param fullScreen whether to use full screen
     */
    void setFullScreen(boolean fullScreen);

}
