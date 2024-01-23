package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.events.PlayerRespawnUpdate;
import slavsquatsuperstar.demos.spacegame.events.SpaceGameEvents;
import slavsquatsuperstar.demos.spacegame.objects.ships.PlayerShip;

/**
 * Spawns the player's ship at the start of the game and respawns it when it is
 * destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerSpawnManager extends SpawnManager {

    private final static float PLAYER_RESPAWN_COOLDOWN = 3f;

    private final Timer spawnTimer; // How long until player respawns
    private boolean playerSpawned;

    public PlayerSpawnManager() {
        spawnTimer = new Timer(PLAYER_RESPAWN_COOLDOWN);
    }

    @Override
    public void init() {
        gameObject.addComponent(spawnTimer);
    }

    @Override
    protected void start() {
        spawnObject();
    }

    @Override
    public void update(float dt) {
        if (!playerSpawned && spawnTimer.isReady()) spawnObject();

        // Send respawn update
        // 100% - (remaining cooldown / total cooldown)
        var respawnPercent = 1f - (spawnTimer.getValue() / spawnTimer.getDuration());
        SpaceGameEvents.getPlayerEventSystem()
                .broadcast(new PlayerRespawnUpdate(respawnPercent));
    }

    // Spawner Methods

    public GameObject createSpawnedObject() {
        return new PlayerShip("Player Spaceship",
                "assets/spacegame/textures/spaceship1.png",
                this);
    }

    @Override
    public void spawnObject() {
        super.spawnObject();
        spawnTimer.setStarted(false);
        playerSpawned = true;

    }

    @Override
    public void markObjectDestroyed() {
        spawnTimer.setStarted(true);
        spawnTimer.reset();
        playerSpawned = false;
    }

}
