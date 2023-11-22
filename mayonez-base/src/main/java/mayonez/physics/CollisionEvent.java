package mayonez.physics;

import mayonez.*;
import mayonez.event.*;
import mayonez.math.*;

/**
 * Describes a collision or trigger interaction between two {@link mayonez.GameObject}s.
 *
 * @author SlavSquatSuperstar
 */
public class CollisionEvent extends Event {

    /**
     * The other object in the collision.
     */
    public final GameObject other;
    /**
     * If interacting with a trigger.
     */
    public final CollisionEventType type;
    /**
     * The type of the collision given by the listener.
     */
    public final boolean trigger;
    /**
     * The direction of the collision from the object's center.
     */
    public final Vec2 direction;
    /**
     * The relative velocity of the objects.
     */
    public final Vec2 velocity;

    public CollisionEvent(
            GameObject other, boolean trigger, CollisionEventType type,
            Vec2 direction, Vec2 velocity
    ) {
        super(formatCollisionEventMessage(other, trigger, type));
        this.other = other;
        this.type = type;
        this.trigger = trigger;
        this.direction = direction;
        this.velocity = velocity;
    }

    private static String formatCollisionEventMessage(
            GameObject other, boolean trigger, CollisionEventType type
    ) {
        return "%s %s with %s".formatted(
                trigger ? "Trigger" : "Collision", type.toString(), other
        );
    }

}
