package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.input.*;

/**
 * A scene included in the demos package with the ability to swap to other scenes.
 *
 * @author SlavSquatSuperstar
 */
public class DemoScene extends Scene {

    // Top row of keyboard
    private final static String[] SCENE_HOTKEYS = {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "="
    };

    public DemoScene(String name) {
        super(name);
    }

    @Override
    protected void onUserUpdate(float dt) {
        pollSceneControls();
    }

    private static void pollSceneControls() {
        if (KeyInput.keyDown(Key.ESCAPE)) {
            Mayonez.stop(ExitCode.SUCCESS); // Exit program by pressing escape
        } else if (KeyInput.keyPressed("r")) {
            SceneManager.restartScene();
        } else if (KeyInput.keyPressed("p")) {
            SceneManager.toggleScenePaused();
        } else if (KeyInput.keyDown("left shift")) {
            // Switch to other scenes
            for (var i = 0; i < SCENE_HOTKEYS.length; i++) {
                if (KeyInput.keyPressed(SCENE_HOTKEYS[i])) {
                    DemosLauncher.switchToScene(i);
                }
            }
        }
    }

}
