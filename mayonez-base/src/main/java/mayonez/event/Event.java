package mayonez.event;

import mayonez.util.*;

/**
 * An action or milestone that occurs in this program. Events may be exchanged asynchronously
 * between objects from different systems of the application.
 *
 * @author SlavSquatSuperstar
 */
public class Event {

    public final String message;

    public Event() {
        this("");
    }

    public Event(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return String.format(
                "%s (%s)",
                StringUtils.getObjectClassName(this),
                message.isEmpty() ? "<No Message>" : message
        );
    }

}
