package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import slavsquatsuperstar.demos.spacegame.events.PlayerRespawnUpdate;
import slavsquatsuperstar.demos.spacegame.events.SpaceGameEvents;
import slavsquatsuperstar.demos.spacegame.objects.ships.PlayerShip;

/**
 * Spawns the player's ship at the start of the game and respawns it when it is
 * destroyed.
 *
 * @author SlavSquatSuperstar
 */
// TODO implement event listener?
public class PlayerSpawner extends SpawnManager {

    private final static int NUM_PLAYERS = 1;
    private final static float PLAYER_RESPAWN_COOLDOWN = 3f;

    public PlayerSpawner() {
        super(NUM_PLAYERS, PLAYER_RESPAWN_COOLDOWN);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        // Send respawn update
        var respawnPercent = 1f - (getRemainingCooldown() / getSpawnCooldown());
        // TODO resetting so flashing red
        SpaceGameEvents.getPlayerEventSystem()
                .broadcast(new PlayerRespawnUpdate(respawnPercent));
    }

    // Spawner Methods

    @Override
    public GameObject createSpawnedObject() {
        return new PlayerShip("Player Spaceship",
                "assets/spacegame/textures/spaceship1.png",
                this);
    }

    // Timer Methods

    private float getRemainingCooldown() {
        return getSpawnTimer().getValue();
    }

    private float getSpawnCooldown() {
        return getSpawnTimer().getDuration();
    }

}
