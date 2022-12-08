package mayonez.physics;

import mayonez.GameObject;
import mayonez.annotations.ExperimentalFeature;
import mayonez.event.Event;

@ExperimentalFeature
public class CollisionEvent extends Event {

//    public final GameObject other;
    public final CollisionEventType type;
    public final boolean trigger;

    public CollisionEvent(GameObject self, GameObject other, CollisionEventType type, boolean trigger) {
        super(String.format("Collision between %s and %s (%s)",
                self, other, trigger ? "Trigger" : "Collider"));
//        this.other = other;
        this.type = type;
        this.trigger = trigger;
    }

}
