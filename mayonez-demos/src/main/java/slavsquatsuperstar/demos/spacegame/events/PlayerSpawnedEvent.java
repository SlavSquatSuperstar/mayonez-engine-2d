package slavsquatsuperstar.demos.spacegame.events;

import mayonez.*;

/**
 * Indicates that the player ship has been added to the scene.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerSpawnedEvent extends SpaceGameEvent {

    private final GameObject player;

    public PlayerSpawnedEvent(GameObject player) {
        super("Spawned player " + player);
        this.player = player;
    }

    public GameObject getPlayer() {
        return player;
    }

}
