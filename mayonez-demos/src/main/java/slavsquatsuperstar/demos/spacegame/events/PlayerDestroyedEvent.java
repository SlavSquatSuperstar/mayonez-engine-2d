package slavsquatsuperstar.demos.spacegame.events;

import mayonez.*;
import mayonez.event.*;

/**
 * Indicates that the player ship has been removed from the scene.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerDestroyedEvent extends Event {

    public PlayerDestroyedEvent(GameObject player) {
        super("Destroyed player " + player);
    }

}
