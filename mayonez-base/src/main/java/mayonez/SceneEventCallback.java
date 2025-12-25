package mayonez;

/**
 * Allows the {@link SceneManager} to respond to a {@link SceneEvent}.
 *
 * @author SlavSquatSuperstar
 */
@FunctionalInterface
interface SceneEventCallback {

    /**
     * Perform an action upon receiving an event.
     *
     * @param event the scene event
     */
    void execute(SceneEvent event);

}
