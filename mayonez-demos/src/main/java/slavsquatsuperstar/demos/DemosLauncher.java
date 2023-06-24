package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.init.*;
import mayonez.input.*;
import slavsquatsuperstar.demos.mario.MarioScene;
import slavsquatsuperstar.demos.physics.PoolBallsScene;
import slavsquatsuperstar.demos.physics.PhysicsSandboxScene;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;

/**
 * A class with a main method that can be used to launch and switch between the different demo scenes.
 *
 * @author SlavSquatSuperstar
 */
// TODO maybe fix up other demos and include them
public class DemosLauncher {

    private final static String[] sceneNames = {
            "Space Game", "Mario Scene", "Collisions Test", "Pool Balls Test"
    };

    public static void main(String[] args) {
        var launcher = new Launcher(args).setRunConfig();
        launcher.loadScenesToManager(getScenesToLoad());
        launcher.startGame(sceneNames[1]);
    }

    private static Scene[] getScenesToLoad() {
        return new Scene[]{
                new SpaceGameScene(sceneNames[0]) {
                    @Override
                    protected void onUserUpdate(float dt) {
                        super.onUserUpdate(dt);
                        pollSceneControls();
                    }
                },
                new MarioScene(sceneNames[1]) {
                    @Override
                    protected void onUserUpdate(float dt) {
                        super.onUserUpdate(dt);
                        pollSceneControls();
                    }
                },
                new PhysicsSandboxScene(sceneNames[2]) {
                    @Override
                    protected void onUserUpdate(float dt) {
                        super.onUserUpdate(dt);
                        pollSceneControls();
                    }
                },
                new PoolBallsScene(sceneNames[3]) {
                    @Override
                    protected void onUserUpdate(float dt) {
                        super.onUserUpdate(dt);
                        pollSceneControls();
                    }
                }
        };
    }

    private static void pollSceneControls() {
        if (KeyInput.keyPressed("r")) {
            SceneManager.restartScene();
        } else if (KeyInput.keyPressed("p")) {
            SceneManager.toggleScenePaused(); // this is being run twice per frame in SpaceGameScene
        } else if (KeyInput.keyDown("left shift")) {
            for (var i = 0; i < sceneNames.length; i++) {
                if (KeyInput.keyPressed(String.valueOf(i + 1))) SceneManager.loadScene(sceneNames[i]);
            }
        }
    }

}
