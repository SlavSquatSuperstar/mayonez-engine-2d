package slavsquatsuperstar.demos.spacegame.events;

import mayonez.*;

/**
 * Indicates that the player ship has been added to the scene.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerSpawnedEvent extends SpaceGameEvent {

    private final Node player;

    public PlayerSpawnedEvent(Node player) {
        super("Spawned player " + player);
        this.player = player;
    }

    public Node getPlayer() {
        return player;
    }

}
