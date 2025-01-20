package mayonez.input.events;

import mayonez.event.*;

/**
 * Indicates an action has been performed with the mouse.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MouseInputEvent extends Event {

    public MouseInputEvent(String message) {
        super(message);
    }

}
