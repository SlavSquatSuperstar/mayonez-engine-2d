package slavsquatsuperstar.demos;

import mayonez.config.*;
import mayonez.util.Record;

/**
 * The controls for the space game.
 *
 * @author SlavSquatSuperstar
 */
final class DemosConfig extends GameConfig {

    static final String CONFIG_FILE_NAME = "user_config.json";
    private static final Record DEFAULTS;
    private static DemosConfig config;

    static {
        DEFAULTS = new Record();
        DEFAULTS.set("start_scene_index", 0);
        DEFAULTS.set("allow_legacy_scenes", false);
        DEFAULTS.set("allow_debug_scenes", false);
    }

    private DemosConfig(String filename, Record defaults) {
        super(filename, defaults);
    }

    static void readConfig() {
        config = new DemosConfig(CONFIG_FILE_NAME, DEFAULTS);
        config.readFromFile();
        config.validateUserPreferences(getRules());
    }

    private static PreferenceValidator<?>[] getRules() {
        return new PreferenceValidator<?>[]{
                new BooleanValidator(
                        "allow_legacy_scenes", "allow_debug_scenes"
                )
        };
    }

    static int getStartSceneIndex() {
        return config.getInt("start_scene_index");
    }

    static boolean shouldAllowLegacyScenes() {
        return config.getBoolean("allow_legacy_scenes");
    }

    static boolean shouldAllowDebugScenes() {
        return config.getBoolean("allow_debug_scenes");
    }

}
