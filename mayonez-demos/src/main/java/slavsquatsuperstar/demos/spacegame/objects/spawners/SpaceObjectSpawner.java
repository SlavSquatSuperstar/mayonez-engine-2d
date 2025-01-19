package slavsquatsuperstar.demos.spacegame.objects.spawners;

import mayonez.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;
import slavsquatsuperstar.demos.spacegame.objects.asteroids.RandomAsteroid;
import slavsquatsuperstar.demos.spacegame.objects.ships.EnemySpaceship;
import slavsquatsuperstar.demos.spacegame.objects.ships.Satellite;

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
    private final static int NUM_OBSTACLES = 6;
    private final static float OBSTACLE_RESPAWN_COOLDOWN = 10f;

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
                return new EnemySpaceship("Enemy Spaceship", SpaceGameScene.getRandomPosition(),
                        "assets/spacegame/textures/ships/shuttle_rusty.png") {
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
                if (Random.randomBoolean()) {
                    return new RandomAsteroid("Asteroid", SpaceGameScene.getRandomPosition()) {
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
                } else {
                    return new Satellite("Satellite", SpaceGameScene.getRandomPosition()) {
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
            }
        });
    }

}
