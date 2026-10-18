package mayonez;

/**
 * The gameplay state of a {@link SceneManager} that dictates whether it is initialized and updating.
 *
 * @author SlavSquatSuperstar
 */
enum SceneState {
    /**
     * A scene that has not been initialized or has been destroyed.
     */
    STOPPED,
    /**
     * A scene that has been initialized and has been suspended.
     */
    PAUSED,
    /**
     * A scene that has been initialized and is currently updating.
     */
    RUNNING,
    /**
     * A scene that is running and has been signaled to stop.
     */
    STOPPING
}
