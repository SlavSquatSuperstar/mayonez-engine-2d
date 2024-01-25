package slavsquatsuperstar.demos.spacegame.objects.spawners;

import mayonez.*;
import mayonez.scripts.*;

/**
 * Spawns multiple objects and tracks the number of spawned objects in the scene.
 *
 * @author SlavSquatSuperstar
 */
public abstract class MultiSpawnManager extends SpawnManager {

    private final Counter amountSpawned; // How many objects are spawned
    private final Timer spawnTimer; // How long until next object is spawned

    public MultiSpawnManager(int maxSpawned, float spawnCooldown) {
        amountSpawned = new Counter(0, maxSpawned, 0);
        spawnTimer = new Timer(spawnCooldown);
    }

    @Override
    public void init() {
        gameObject.addComponent(spawnTimer);
    }

    @Override
    protected void start() {
        amountSpawned.resetToMin();
        spawnTimer.reset();
        populateToMax();
    }

    @Override
    public void update(float dt) {
        // Keep spawning if destroyed and has room
        if (spawnTimer.isReady() && !amountSpawned.isAtMax()) {
            spawnObject();
        }
    }

    public void spawnObject() {
        super.spawnObject();
        amountSpawned.count(1);
        if (!amountSpawned.isAtMax()) spawnTimer.reset(); // Wait before spawning additional
    }

    /**
     * Spawn in as many objects as this script will allow instantly.
     */
    public void populateToMax() {
        while (!amountSpawned.isAtMax()) spawnObject();
    }

    @Override
    public void markObjectDestroyed(GameObject object) {
        amountSpawned.count(-1);
        spawnTimer.reset(); //Reset so objects don't respawn immediately
    }

}
