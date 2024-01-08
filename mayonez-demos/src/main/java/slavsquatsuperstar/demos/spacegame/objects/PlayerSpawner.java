package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.event.*;
import slavsquatsuperstar.demos.spacegame.objects.ships.PlayerShip;

/**
 * Spawns the player's ship at the start of the game and respawns it when it is
 * destroyed. Also informs the game of different events involving the player.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerSpawner extends SpawnManager {

    private final static int NUM_PLAYERS = 1;
    private final static float PLAYER_RESPAWN_COOLDOWN = 2f;
    private static final EventSystem<Event> eventSystem = new EventSystem<>();

    // Player Status
    private static GameObject player;
    private static boolean playerAlive;

    public PlayerSpawner() {
        super(NUM_PLAYERS, PLAYER_RESPAWN_COOLDOWN);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        // Recharge health bar
//        if (healthBar != null && !playerAlive) {
//            var respawnPercent = getCooldownProgress() / getSpawnCooldown();
//            System.out.printf("recharging: %.2f%%\n", respawnPercent * 100f);
//            healthBar.setValue(respawnPercent);
//        }
    }

    // Spawner Methods

    @Override
    public GameObject createSpawnedObject() {
        player = new PlayerShip("Player Spaceship",
                "assets/spacegame/textures/spaceship1.png",
                this);
        return player;
    }

    @Override
    public void spawnObject() {
        super.spawnObject();
        playerAlive = true;
    }

    @Override
    public void markObjectDestroyed() {
        super.markObjectDestroyed();
        playerAlive = false;
    }

    // Static methods

    public static EventSystem<Event> getEventSystem() {
        return eventSystem;
    }

    public static GameObject getPlayer() {
        return player;
    }

    public static boolean isPlayerAlive() {
        return playerAlive;
    }

}
