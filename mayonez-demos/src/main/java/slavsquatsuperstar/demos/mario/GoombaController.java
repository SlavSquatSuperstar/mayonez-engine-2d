package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.physics.*;

/**
 * Destroys a Goomba when Mario touches it.
 *
 * @author SlavSquatSuperstar
 */
class GoombaController extends Script {

    @Override
    protected void start() {
        getCollider().addCollisionCallback(event -> {
            // On collision
            if (!event.trigger
                    && event.type.equals(CollisionEventType.ENTER)
                    && event.other.getName().equals("Mario")) {
                gameObject.setDestroyed();
            }
        });
    }
}
