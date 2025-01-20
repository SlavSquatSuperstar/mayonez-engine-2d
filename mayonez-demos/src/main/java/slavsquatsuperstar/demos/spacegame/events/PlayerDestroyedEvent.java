package slavsquatsuperstar.demos.spacegame.events;

import mayonez.*;

/**
 * Indicates that the player ship has been removed from the scene.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerDestroyedEvent extends SpaceGameEvent {

    public PlayerDestroyedEvent(GameObject player) {
        super("Destroyed player " + player);
    }

}
