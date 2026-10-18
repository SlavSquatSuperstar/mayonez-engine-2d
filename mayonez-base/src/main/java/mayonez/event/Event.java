package mayonez.event;

import mayonez.util.*;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * An action or milestone that occurs in this program. Events may be exchanged
 * asynchronously between objects from unrelated components of the application.
 * Events are passed through {@link EventSystem}s and received by
 * {@link EventListener}s.
 *
 * @author SlavSquatSuperstar
 */
public class Event {

    public final String message;

    public Event() {
        this("");
    }

    public Event(@Nullable String message) {
        this.message = Objects.requireNonNullElse(message, "");
    }

    @Override
    public String toString() {
        var className = StringUtils.getObjectClassName(this);
        return message.isEmpty()
                ? className
                : "%s (%s)".formatted(className, message);
    }

}
