package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.config.*;
import slavsquatsuperstar.demos.geometrydash.GDEditorScene;
import slavsquatsuperstar.demos.input.InputTestScene;
import slavsquatsuperstar.demos.mario.MarioScene;
import slavsquatsuperstar.demos.physics.*;
import slavsquatsuperstar.demos.renderer.RendererTestScene;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;

/**
 * The entry point into the demos application. Launches and switches between the
 * different demo scenes.
 *
 * @author SlavSquatSuperstar
 */
public class DemosLauncher {

    private final static int START_SCENE_INDEX = 0;

    private final static String[] SCENE_NAMES = {
            "Space Game", "Render Batch Test",
            "Physics Sandbox", "Pool Balls",
            "Mario Level", "Geometry Dash Editor",
            "Input Test", "Collision Test", "Projectile Test"
    };

    public static void main(String[] args) {
        var launcher = new Launcher(args).setRunConfig();
        launcher.loadScenesToManager(getScenesToLoad());
        launcher.startGame(SCENE_NAMES[START_SCENE_INDEX]);
    }

    private static Scene[] getScenesToLoad() {
        return new Scene[]{
                new SpaceGameScene(SCENE_NAMES[0]),
                new RendererTestScene(SCENE_NAMES[1]),
                new PhysicsSandboxScene(SCENE_NAMES[2]),
                new PoolBallsScene(SCENE_NAMES[3]),
                new MarioScene(SCENE_NAMES[4]),
                new GDEditorScene(SCENE_NAMES[5]),
                new InputTestScene(SCENE_NAMES[6]),
                new CollisionTestScene(SCENE_NAMES[7]),
                new ProjectileTestScene(SCENE_NAMES[8]),
        };
    }

    static void switchToScene(int sceneIndex) {
        if (sceneIndex < SCENE_NAMES.length) {
            SceneManager.changeScene(SceneManager.getScene(SCENE_NAMES[sceneIndex]));
        }
    }

}
