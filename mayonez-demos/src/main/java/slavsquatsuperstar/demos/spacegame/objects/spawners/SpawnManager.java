package slavsquatsuperstar.demos.spacegame.objects.spawners;

import mayonez.*;

/**
 * Spawns prefab objects into the scene using the {@link #spawnObject()} method.
 * Spawning behavior and conditions can be defined in subclasses.
 *
 * @author SlavSquatSuperstar
 */
public abstract class SpawnManager extends Script {

    /**
     * Instantiate a prefab {@link mayonez.GameObject} to be spawned. To be defined
     * by the subclass.
     *
     * @return the spawned object
     */
    public abstract GameObject createSpawnedObject();

    /**
     * Spawns an object into the scene. Can be overridden to run additional
     * user-defined behavior.
     */
    public void spawnObject() {
        getScene().addObject(createSpawnedObject());
    }

    /**
     * Notify this script that a spawned object has been destroyed. Does
     * nothing by default and can be overridden.
     *
     * @param object the spawned object
     */
    public void markObjectDestroyed(GameObject object) {
    }

}
