package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.assets.text.*;
import mayonez.config.*;

import java.util.*;

/**
 * The entry point into the demos application. Launches and switches between the
 * different demo scenes.
 *
 * @author SlavSquatSuperstar
 */
public class DemosLauncher {

    private static final int START_SCENE_INDEX = 0;
    private static final boolean ALLOW_LEGACY_SCENES = false;
    private static final boolean ALLOW_DEBUG_SCENES = true;

    private static List<Scene> scenes;

    public static void main(String[] args) {
        var launcher = new Launcher(args).setRunConfig();

        // Read scene files
        scenes = new ArrayList<>(getScenesFromFile("assets/demos/scenes/main_scenes.csv"));
        if (ALLOW_LEGACY_SCENES) {
            scenes.addAll(getScenesFromFile("assets/demos/scenes/legacy_scenes.csv"));
        }
        if (ALLOW_DEBUG_SCENES) {
            scenes.addAll(getScenesFromFile("assets/demos/scenes/debug_scenes.csv"));
        }

        // Load scenes and start
        launcher.addScenesToManager(scenes);
        launcher.startGame(DemosConfig.getStartSceneIndex());
    }

    private static List<Scene> getScenesFromFile(String filename) {
        var csvData = new CSVFile(filename).readCSV();
        if (csvData == null) return List.of();
        return csvData.stream()
                .map(SceneInfo::new)
                .map(SceneInfo::instantiate)
                .toList();
    }

    static void switchToScene(int sceneIndex) {
        if (sceneIndex < scenes.size()) {
            SceneManager.changeScene(SceneManager.getScene(sceneIndex));
        }
    }

}
