package mayonez.application;

import mayonez.event.*;

/**
 * Indicates that the main window has been resized.
 *
 * @author SlavSquatSuperstar
 */
public class WindowResizeEvent extends Event {

    private final int width, height;

    public WindowResizeEvent(int width, int height) {
        super("Window resized: %dx%d".formatted(width, height));
        this.width = width;
        this.height = height;
    }

    /**
     * The width of the window's content area in screen units.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * The height of the window's content area in screen units.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

}
