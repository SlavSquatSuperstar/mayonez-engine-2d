package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import slavsquatsuperstar.demos.spacegame.objects.ships.PlayerShip;

/**
 * Spawns the player's ship at the start of the game and respawns it when it is
 * destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class PlayerSpawner extends SpawnManager {

    private final static int NUM_PLAYERS = 1;
    private final static float PLAYER_RESPAWN_COOLDOWN = 2f;
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
        return new PlayerShip("Player Spaceship",
                "assets/spacegame/textures/spaceship1.png",
                this);
    }

}
