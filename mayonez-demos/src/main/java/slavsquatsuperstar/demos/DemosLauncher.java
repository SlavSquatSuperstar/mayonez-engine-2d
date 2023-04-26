package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.init.*;
import mayonez.input.*;
import mayonez.io.Assets;
import mayonez.io.FilePath;
import mayonez.util.*;
import slavsquatsuperstar.demos.mario.MarioScene;
import slavsquatsuperstar.demos.physics.PoolBallsScene;
import slavsquatsuperstar.demos.physics.SandboxScene;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;

/**
 * A class with a main method that can be used to launch and switch between the different demo scenes.
 *
 * @author SlavSquatSuperstar
 */
public class DemosLauncher {

    private final static String[] scenes = {
            "Space Game", "Mario Scene", "Collisions Test", "Pool Balls Test"
    };

    public static void main(String[] args) {
        var launcher = new Launcher(args);
        launcher.setRunConfig();

        // Load scenes and allow scene switching
        SceneManager.addScene(new SpaceGameScene(scenes[0]) {
            @Override
            protected void onUserUpdate(float dt) {
                super.onUserUpdate(dt);
                pollSceneControls();
            }
        });
        SceneManager.addScene(new MarioScene(scenes[1]) {
            @Override
            protected void onUserUpdate(float dt) {
                super.onUserUpdate(dt);
                pollSceneControls();
            }
        });
        SceneManager.addScene(new SandboxScene(scenes[2]) {
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

        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperty("file.separator"));
        System.out.printf("'%s'\n", System.getProperty("line.separator"));
        System.out.println(OperatingSystem.getCurrentOS());

        Mayonez.start(SceneManager.getScene(scenes[0]));
    }

    private static void pollSceneControls() {
        if (KeyInput.keyPressed("r")) {
            SceneManager.restartScene();
        } else if (KeyInput.keyPressed("p")) {
            SceneManager.toggleScenePaused();
        } else if (KeyInput.keyDown("left shift")) {
            for (var i = 0; i < scenes.length; i++) {
                if (KeyInput.keyPressed(String.valueOf(i + 1))) SceneManager.loadScene(scenes[i]);
            }
        }
    }

}
