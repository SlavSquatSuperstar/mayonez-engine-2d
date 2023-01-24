package slavsquatsuperstar.demos;

import mayonez.Mayonez;
import mayonez.SceneManager;
import mayonez.input.KeyInput;
import slavsquatsuperstar.demos.mario.RendererTest;
import slavsquatsuperstar.demos.physics.PhysicsTestScene;
import slavsquatsuperstar.demos.physics.DetectionTestScene;
import slavsquatsuperstar.demos.physics.PoolBallsScene;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;

/**
 * A class with a main method that can be used to launch and switch between the different demo scenes.
 *
 * @author SlavSquatSuperstar
 */
public class DemosLauncher {

    private final static String[] scenes = {
            "Space Game", "Mario Scene", "Collisions Test",
            "Pool Balls Test", "Detection Test"
    };

    public static void main(String[] args) {
        var arg0 = (args.length > 0) ? args[0] : "false";
        Mayonez.setUseGL(Boolean.parseBoolean(arg0)); // Automatically choose AWT/GL from CL args

        // Load scenes and allow scene switching
        SceneManager.addScene(new SpaceGameScene(scenes[0]) {
            @Override
            protected void onUserUpdate(float dt) {
                super.onUserUpdate(dt);
                pollSceneControls();
            }
        });
        SceneManager.addScene(new RendererTest(scenes[1]) {
            @Override
            protected void onUserUpdate(float dt) {
                super.onUserUpdate(dt);
                pollSceneControls();
            }
        });
        SceneManager.addScene(new PhysicsTestScene(scenes[2]) {
            @Override
            protected void onUserUpdate(float dt) {
                super.onUserUpdate(dt);
                pollSceneControls();
            }
        });
        SceneManager.addScene(new PoolBallsScene(scenes[3]) {
            @Override
            protected void onUserUpdate(float dt) {
                super.onUserUpdate(dt);
                pollSceneControls();
            }
        });
        SceneManager.addScene(new DetectionTestScene(scenes[4]) {
            @Override
            protected void onUserUpdate(float dt) {
                super.onUserUpdate(dt);
                pollSceneControls();
            }
        });

        // Start Game
        Mayonez.start(SceneManager.getScene(scenes[0]));
    }

    private static void pollSceneControls() { // TODO add pause button
        if (KeyInput.keyPressed("r")) {
            SceneManager.reloadScene();
        } else if (KeyInput.keyDown("left shift")) {
            for (var i = 0; i < scenes.length; i++) {
                if (KeyInput.keyPressed(String.valueOf(i + 1))) SceneManager.setScene(scenes[i]);
            }
        }
    }

}
