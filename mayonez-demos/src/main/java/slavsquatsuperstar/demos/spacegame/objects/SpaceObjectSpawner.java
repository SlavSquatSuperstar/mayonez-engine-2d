package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import slavsquatsuperstar.demos.spacegame.objects.asteroids.Asteroid;
import slavsquatsuperstar.demos.spacegame.objects.ships.EnemyShip;
import slavsquatsuperstar.demos.spacegame.objects.ships.PlayerShip;

import java.util.*;

/**
 * Automatically spawns and respawns prefab objects in the scene.
 *
 * @author SlavSquatSuperstar
 */
public class SpaceObjectSpawner extends GameObject {

    private final static int NUM_PLAYERS = 1;
    private final static int NUM_ENEMIES = 6;
    private final static int NUM_OBSTACLES = 5;

    public SpaceObjectSpawner() {
        super("Object Spawner");
    }

    @Override
    protected void init() {
        List<SpawnManager> spawners = new ArrayList<>();
        spawners.add(new SpawnManager(NUM_PLAYERS, 1.5f) {
            @Override
            public GameObject createSpawnedObject() {
                return new PlayerShip("Player Spaceship",
                        "assets/spacegame/textures/spaceship1.png", this);
            }
        });
        spawners.add(new SpawnManager(NUM_ENEMIES, 5) {
            @Override
            public GameObject createSpawnedObject() {
                return new EnemyShip("Enemy Spaceship",
                        "assets/spacegame/textures/spaceship2.png", this);
            }
        });
        spawners.add(new SpawnManager(NUM_OBSTACLES, 20) {
            @Override
            public GameObject createSpawnedObject() {
                return new Asteroid("Asteroid", this);
            }
        });

        for (var spawner : spawners) {
            addComponent(spawner);
            spawner.populateToMax();
        }
    }

}
