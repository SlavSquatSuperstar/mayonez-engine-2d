package mayonez;

/**
 * The gameplay state of a scene that dictates whether it is active or updating.
 *
 * @author SlavSquatSuperstar
 */
enum SceneState {
    /**
     * A scene that has not been initialized.
     */
    STOPPED,
    /**
     * A scene that has been initialized and is inactive.
     */
    PAUSED,
    /**
     * A scene that has been initialized and is active.
     */
    RUNNING
}
