package mayonez;

import mayonez.event.Event;

/**
 * Indicates to the {@link SceneManager} that a {@link Scene} has changed its {@link SceneState}.
 *
 * @author SlavSquatSuperstar
 */
class SceneEvent extends Event {

    private final Scene scene;
    private final SceneState state;

    SceneEvent(Scene scene, SceneState state) {
        super("Scene %s state: %s".formatted(scene, state));
        this.scene = scene;
        this.state = state;
    }

    Scene getScene() {
        return scene;
    }

    SceneState getState() {
        return state;
    }

}
