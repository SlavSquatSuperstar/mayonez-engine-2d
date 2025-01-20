package mayonez.input.events;

/**
 * Indicates a mouse button has been pressed or released.
 *
 * @author SlavSquatSuperstar
 */
public class MouseButtonEvent extends MouseInputEvent {

    private final int button;
    private final boolean buttonDown;
    private final float eventTime;

    public MouseButtonEvent(int button, boolean buttonDown, float eventTime) {
        super("Button %d down: %s, time = %.4f".formatted(button, buttonDown, eventTime));
        this.button = button;
        this.buttonDown = buttonDown;
        this.eventTime = eventTime;
    }

    public MouseButtonEvent(int button, boolean buttonDown) {
        super("Button %d down: %s".formatted(button, buttonDown));
        this.button = button;
        this.buttonDown = buttonDown;
        eventTime = 0f;
    }

    /**
     * The internal button code associated with this event.
     *
     * @return the button code
     */
    public int getButton() {
        return button;
    }

    /**
     * Whether the button was pressed or released in this event.
     *
     * @return if the button is down
     */
    public boolean isButtonDown() {
        return buttonDown;
    }

    /**
     * The time that the mouse button action time occurred, in seconds.
     * Only used if the mouse was pressed, else ignored.
     *
     * @return the event time
     */
    public float getEventTime() {
        return eventTime;
    }

}
