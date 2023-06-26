package mayonez.physics;

/**
 * What type of collision event is detected by a {@link mayonez.physics.CollisionListener}.
 *
 * @author SlavSquatSuperstar
 */
public enum CollisionEventType {
    /**
     * When an object starts colliding with another object or enters a trigger area.
     */
    ENTER,
    /**
     * When an object continues colliding with another object or stays in a trigger area.
     */
    STAY,
    /**
     * When an object stops colliding with another object or exits a trigger area.
     */
    EXIT
}
