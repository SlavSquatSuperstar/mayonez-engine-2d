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

    private static List<Scene> scenes;

    public static void main(String[] args) {
        var launcher = new Launcher(args).setRunConfig();
        DemosConfig.readConfig();

        // Read scene files
        scenes = new ArrayList<>(getScenesFromFile("assets/demos/scenes/main_scenes.csv"));
        if (DemosConfig.shouldAllowLegacyScenes()) {
            scenes.addAll(getScenesFromFile("assets/demos/scenes/legacy_scenes.csv"));
        }
        if (DemosConfig.shouldAllowDebugScenes()) {
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
