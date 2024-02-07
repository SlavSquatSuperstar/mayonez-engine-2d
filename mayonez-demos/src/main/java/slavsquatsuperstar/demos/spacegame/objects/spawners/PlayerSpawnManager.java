package slavsquatsuperstar.demos.spacegame.objects.spawners;

import mayonez.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.events.*;
import slavsquatsuperstar.demos.spacegame.objects.ships.PlayerShip;

/**
 * Spawns the player's ship at the start of the game and respawns it when it is
 * destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerSpawnManager extends SpawnManager {

    private final Timer spawnTimer; // How long until player respawns
    private boolean playerSpawned;

    public PlayerSpawnManager(float respawnCooldown) {
        spawnTimer = new Timer(respawnCooldown);
    }

    @Override
    protected void start() {
        spawnTimer.reset();
        spawnObject();
    }

    @Override
    protected void update(float dt) {
        spawnTimer.countDown(dt);
        if (!playerSpawned && spawnTimer.isReady()) spawnObject();

        // Send respawn update
        // 100% - (remaining cooldown / total cooldown)
        var respawnPercent = 1f - (spawnTimer.getValue() / spawnTimer.getDuration());
        SpaceGameEvents.getPlayerEventSystem()
                .broadcast(new PlayerRespawnUpdate(respawnPercent));
    }

    // Spawner Methods

    @Override
    public GameObject createSpawnedObject() {
        return new PlayerShip("Player Spaceship",
                "assets/spacegame/textures/spaceship1.png"
        ) {
            @Override
            protected void init() {
                super.init();
                addComponent(new Script() {
                    @Override
                    protected void start() {
                        SpaceGameEvents.getPlayerEventSystem()
                                .broadcast(new PlayerSpawnedEvent(gameObject));
                    }

                    @Override
                    protected void onDestroy() {
                        markObjectDestroyed(gameObject);
                    }
                });
            }
        };
    }

    @Override
    public void spawnObject() {
        super.spawnObject();
        spawnTimer.setPaused(true);
        playerSpawned = true;
    }

    @Override
    public void markObjectDestroyed(GameObject object) {
        SpaceGameEvents.getPlayerEventSystem()
                .broadcast(new PlayerDestroyedEvent(object));

        spawnTimer.setPaused(false);
        spawnTimer.reset();
        playerSpawned = false;
    }

}
