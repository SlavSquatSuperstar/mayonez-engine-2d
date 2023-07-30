package mayonez.event;

import mayonez.annotations.*;

/**
 * An action or milestone that occurs in this program. Events are a form of communication between different objects
 * or systems of the application.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
public class Event {

    private final String message;

    public Event() {
        this("");
    }

    public Event(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        String className = getClass().isAnonymousClass() ? "Event" : getClass().getSimpleName();
        if (message.isEmpty()) return className;
        else return String.format("%s (%s)", className, message);
    }

}
