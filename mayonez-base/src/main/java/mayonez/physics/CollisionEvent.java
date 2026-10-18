package mayonez.physics;

import mayonez.*;
import mayonez.event.*;
import mayonez.math.*;

import java.util.*;

/**
 * Describes a collision or trigger interaction between two collidable
 * {@link mayonez.Node}s.
 *
 * @author SlavSquatSuperstar
 */
public class CollisionEvent extends Event {

    /**
     * The other collidable object in the collision.
     */
    public final Node other;

    /**
     * If interacting with a trigger rather than a physical object.
     */
    public final boolean trigger;

    /**
     * The type of the collision which can be "enter", "stay", or "exit".
     */
    public final CollisionEventType type;

    /**
     * The direction of the collision from the object's center, null if the type
     * is not "enter".
     */
    public final Vec2 direction;

    /**
     * The relative velocity of the objects, null if the type is not "enter".
     */
    public final Vec2 velocity;

    /**
     * The contact points of the objects, one or two points if the type is not
     * "exit".
     */
    public final List<Vec2> contacts;

    public CollisionEvent(
            Node other, boolean trigger, CollisionEventType type,
            Vec2 direction, Vec2 velocity, List<Vec2> contacts
    ) {
        super(formatCollisionEventMessage(other, trigger, type));
        this.other = other;
        this.trigger = trigger;
        this.type = type;
        this.direction = direction;
        this.velocity = velocity;
        this.contacts = contacts;
    }

    private static String formatCollisionEventMessage(
            Node other, boolean trigger, CollisionEventType type
    ) {
        return "%s %s with %s".formatted(
                trigger ? "Trigger" : "Collision", type.toString(), other
        );
    }

}
