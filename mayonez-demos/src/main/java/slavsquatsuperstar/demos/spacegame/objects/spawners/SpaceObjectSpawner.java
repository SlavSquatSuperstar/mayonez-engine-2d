package slavsquatsuperstar.demos.spacegame.objects.spawners;

import mayonez.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;
import slavsquatsuperstar.demos.spacegame.objects.asteroids.AsteroidPrefabs;
import slavsquatsuperstar.demos.spacegame.objects.asteroids.BaseAsteroid;
import slavsquatsuperstar.demos.spacegame.objects.ships.*;

/**
 * Automatically populates the scene with prefabs and respawns them when they are
 * destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class SpaceObjectSpawner extends Node {

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
        addChild(new PlayerSpawnManager(PLAYER_RESPAWN_COOLDOWN));

        // Enemies
        addChild(new MultiSpawnManager(NUM_ENEMIES, ENEMY_RESPAWN_COOLDOWN) {
            @Override
            public Node createSpawnedObject() {
                SpaceshipProperties properties;
                var invCDF = Random.randomFloat(0f, 100f);
                if (invCDF < 30f) {
                    properties = ShipPrefabs.SHUTTLE1_PROPERTIES;
                } else if (invCDF < 60f) {
                    properties = ShipPrefabs.SHUTTLE2_PROPERTIES;
                } else {
                    properties = ShipPrefabs.FIGHTER_PROPERTIES;
                }

                var name = "Enemy " + properties.name();
                return new EnemySpaceship(
                        name, SpaceGameScene.getRandomPosition(), properties
                ) {
                    @Override
                    protected void onDestroy() {
                        super.onDestroy();
                        markObjectDestroyed(this);
                    }
                };
            }
        });

        // Obstacles
        addChild(new MultiSpawnManager(NUM_OBSTACLES, OBSTACLE_RESPAWN_COOLDOWN) {
            @Override
            public Node createSpawnedObject() {
                if (Random.randomBoolean()) {
                    return new BaseAsteroid(
                            "Asteroid", SpaceGameScene.getRandomPosition(),
                            AsteroidPrefabs.getRandomProperties()
                    ) {
                        @Override
                        protected void onDestroy() {
                            super.onDestroy();
                            markObjectDestroyed(this);
                        }
                    };
                } else {
                    return new Satellite("Satellite", SpaceGameScene.getRandomPosition(),
                            ShipPrefabs.SATELLITE_PROPERTIES) {
                        @Override
                        protected void onDestroy() {
                            super.onDestroy();
                            markObjectDestroyed(this);
                        }
                    };
                }
            }
        });
    }

}
