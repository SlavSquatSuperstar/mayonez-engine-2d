package mayonez.input.events;

/**
 * Indicates the mouse has been scrolled.
 *
 * @author SlavSquatSuperstar
 */
public class MouseScrollEvent extends MouseInputEvent {

    private final float xOffset, yOffset;

    public MouseScrollEvent(float xOffset, float yOffset) {
        super("Mouse scrolled: %.2f, %.2f".formatted(xOffset, yOffset));
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /**
     * The x offset relative to the last scroll position.
     *
     * @return the x scroll
     */
    public float getXOffset() {
        return xOffset;
    }

    /**
     * The y offset relative to the last scroll position.
     *
     * @return the y scroll
     */
    public float getYOffset() {
        return yOffset;
    }

}
