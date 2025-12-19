package mayonez.application;

/**
 * Describes how a {@link Window} should be initialized.
 *
 * @param title      the window's title
 * @param width      the window's width in pixels
 * @param height     the window's height in pixels
 * @param fullScreen if the window should be full screen
 * @param resizable  if the window can be resized
 * @author SlavSquatSuperstar
 */
public record WindowConfig(
        String title, int width, int height,
        boolean fullScreen, boolean resizable
) {

    /**
     * Return a copy of this record, ensuring that the dimensions are valid.
     *
     * @return the validated copy
     */
    public WindowConfig validate() {
        // Make sure dimensions are positive
        // GLFW needs at least 1x1
        var newWidth = Math.max(1, width);
        var newHeight = Math.max(1, height);
        return new WindowConfig(title, newWidth, newHeight, fullScreen, resizable);
    }

}
