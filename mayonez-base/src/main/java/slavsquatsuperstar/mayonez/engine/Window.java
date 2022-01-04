package slavsquatsuperstar.mayonez.engine;

import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.graphics.renderer.Renderable;

/**
 * An interface containing common functionality for all game windows.
 *
 * @author SlavSquatSuperstar
 */
public sealed interface Window permits JWindow, GLWindow{

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
    void render(Renderable r);

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
