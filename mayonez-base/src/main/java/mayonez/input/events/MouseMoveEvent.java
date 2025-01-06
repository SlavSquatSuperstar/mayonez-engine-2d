package mayonez.input.events;

/**
 * Indicates the mouse cursor has been moved.
 *
 * @author SlavSquatSuperstar
 */
public class MouseMoveEvent extends MouseInputEvent {

    private final float mouseX, mouseY;

    public MouseMoveEvent(float mouseX, float mouseY) {
        super("Mouse moved: %.2f, %.2f".formatted(mouseX, mouseY));
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    /**
     * The x position of the mouse cursor in the window.
     *
     * @return the x position, in pixels
     */
    public float getMouseX() {
        return mouseX;
    }

    /**
     * The y position of the mouse cursor in the window.
     *
     * @return the y position, in pixels
     */
    public float getMouseY() {
        return mouseY;
    }

}
