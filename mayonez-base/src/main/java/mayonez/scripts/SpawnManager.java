package mayonez.scripts;

import mayonez.*;

/**
 * Sets conditions for spawning new objects and tracks the number of spawned objects in the scene.
 *
 * @author SlavSquatSuperstar
 */
public abstract class SpawnManager extends Script {

    // TODO disable spawn limit
    private final Counter amountSpawned; // how many objects spawned
    private final Timer spawnTimer; // how long until spawning objects again

    public SpawnManager(int maxSpawned, float spawnCooldown) {
        amountSpawned = new Counter(0, maxSpawned, 0);
        spawnTimer = new Timer(spawnCooldown);
    }

    @Override
    public void init() {
        amountSpawned.resetToMin();
        spawnTimer.reset();
        gameObject.addComponent(spawnTimer);
    }

    @Override
    public void update(float dt) {
        // Keep spawning if destroyed and has room
        if (spawnTimer.isReady() && !amountSpawned.isAtMax()) spawnObject();
    }

    /**
     * Instantiate a prefab {@link mayonez.GameObject} to be spawned. To be defined by the subclass.
     *
     * @return the new object
     */
    public abstract GameObject createSpawnedObject();

    /**
     * Add a spawned object into the scene.
     */
    public void spawnObject() {
        getScene().addObject(createSpawnedObject());
        amountSpawned.count(1);
        spawnTimer.reset();
    }

    /**
     * Spawn in as many objects as this script will allow.
     */
    public void populateToMax() {
        while (!amountSpawned.isAtMax()) spawnObject();
    }

    /**
     * Notify this script that a spawned object has been destroyed.
     */
    public void markObjectDestroyed() {
        amountSpawned.count(-1);
        if (spawnTimer.isReady()) spawnTimer.reset();
        // reset timer after spawning if needed, so things don't spawn immediately
    }

}
