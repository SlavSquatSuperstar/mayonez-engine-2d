package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.config.*;
import mayonez.input.*;
import slavsquatsuperstar.demos.geometrydash.GDEditorScene;
import slavsquatsuperstar.demos.mario.MarioScene;
import slavsquatsuperstar.demos.physics.PhysicsSandboxScene;
import slavsquatsuperstar.demos.physics.PoolBallsScene;
import slavsquatsuperstar.demos.renderer.RendererTestScene;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;

/**
 * A class with a main method that can be used to launch and switch between the different demo scenes.
 *
 * @author SlavSquatSuperstar
 */
public class DemosLauncher {

    private final static int START_SCENE_INDEX = 4;

    private final static String[] SCENE_NAMES = {
            "Space Game", "Render Batch Test",
            "Physics Sandbox", "Pool Balls",
            "Mario Level", "Geometry Dash Editor",
    };

    public static void main(String[] args) {
        var launcher = new Launcher(args).setRunConfig();
        launcher.loadScenesToManager(getScenesToLoad());
        launcher.startGame(SCENE_NAMES[START_SCENE_INDEX]);
    }

    private static Scene[] getScenesToLoad() {
        return new Scene[]{
                new SpaceGameScene(SCENE_NAMES[0]) {
                    @Override
                    protected void onUserUpdate(float dt) {
                        super.onUserUpdate(dt);
                        pollSceneControls();
                    }
                },
                new RendererTestScene(SCENE_NAMES[1]) {
                    @Override
                    protected void onUserUpdate(float dt) {
                        super.onUserUpdate(dt);
                        pollSceneControls();
                    }
                },
                new PhysicsSandboxScene(SCENE_NAMES[2]) {
                    @Override
                    protected void onUserUpdate(float dt) {
                        super.onUserUpdate(dt);
                        pollSceneControls();
                    }
                },
                new PoolBallsScene(SCENE_NAMES[3]) {
                    @Override
                    protected void onUserUpdate(float dt) {
                        super.onUserUpdate(dt);
                        pollSceneControls();
                    }
                },
                new MarioScene(SCENE_NAMES[4]) {
                    @Override
                    protected void onUserUpdate(float dt) {
                        super.onUserUpdate(dt);
                        pollSceneControls();
                    }
                },
                new GDEditorScene(SCENE_NAMES[5]) {
                    @Override
                    protected void onUserUpdate(float dt) {
                        super.onUserUpdate(dt);
                        pollSceneControls();
                    }
                },
        };
    }

    private static void pollSceneControls() {
        if (KeyInput.keyDown(Key.ESCAPE)) {
            Mayonez.stop(ExitCode.SUCCESS); // Exit program by pressing escape
        } else if (KeyInput.keyPressed("r")) {
            SceneManager.restartScene();
        } else if (KeyInput.keyPressed("p")) {
            SceneManager.toggleScenePaused();
        } else if (KeyInput.keyDown("left shift")) {
            for (var i = 0; i < SCENE_NAMES.length; i++) {
                if (KeyInput.keyPressed(String.valueOf(i + 1))) {
                    SceneManager.loadScene(SCENE_NAMES[i]);
                }
            }
        }
    }

}
