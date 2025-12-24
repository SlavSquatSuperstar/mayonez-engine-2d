package mayonez;

/**
 * The gameplay state of a {@link SceneManager} that dictates whether it is initialized and updating.
 *
 * @author SlavSquatSuperstar
 */
enum SceneState {
    /**
     * A scene that has not been initialized or has been unloaded.
     */
    STOPPED,
    /**
     * A scene that has been initialized and is inactive.
     */
    PAUSED,
    /**
     * A scene that has been initialized and is active.
     */
    RUNNING,
    /**
     * A scene that is running and has been signaled to stop.
     */
    DESTROYED
}
