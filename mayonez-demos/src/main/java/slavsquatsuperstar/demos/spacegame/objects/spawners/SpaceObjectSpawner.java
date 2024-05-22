package slavsquatsuperstar.demos.spacegame.objects.spawners;

import mayonez.*;
import slavsquatsuperstar.demos.spacegame.objects.asteroids.RandomAsteroid;
import slavsquatsuperstar.demos.spacegame.objects.ships.EnemySpaceship;

/**
 * Automatically populates the scene with prefabs and respawns them when they are
 * destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class SpaceObjectSpawner extends GameObject {

    // Constants
    private final static float PLAYER_RESPAWN_COOLDOWN = 3f;
    private final static int NUM_ENEMIES = 6;
    private final static float ENEMY_RESPAWN_COOLDOWN = 5f;
    private final static int NUM_OBSTACLES = 5;
    private final static float OBSTACLE_RESPAWN_COOLDOWN = 20f;

    public SpaceObjectSpawner(String name) {
        super(name);
    }

    @Override
    protected void init() {
        // Player
        addComponent(new PlayerSpawnManager(PLAYER_RESPAWN_COOLDOWN));

        // Enemies
        addComponent(new MultiSpawnManager(NUM_ENEMIES, ENEMY_RESPAWN_COOLDOWN) {
            @Override
            public GameObject createSpawnedObject() {
                return new EnemySpaceship("Enemy Spaceship",
                        "assets/spacegame/textures/ships/spaceship2.png") {
                    @Override
                    protected void init() {
                        super.init();
                        addComponent(new Script() {
                            @Override
                            protected void onDestroy() {
                                markObjectDestroyed(gameObject);
                            }
                        });
                    }
                };
            }
        });

        // Obstacles
        addComponent(new MultiSpawnManager(NUM_OBSTACLES, OBSTACLE_RESPAWN_COOLDOWN) {
            @Override
            public GameObject createSpawnedObject() {
                return new RandomAsteroid("Asteroid") {
                    @Override
                    protected void init() {
                        super.init();
                        addComponent(new Script() {
                            @Override
                            protected void onDestroy() {
                                markObjectDestroyed(gameObject);
                            }
                        });
                    }
                };
            }
        });
    }

}
