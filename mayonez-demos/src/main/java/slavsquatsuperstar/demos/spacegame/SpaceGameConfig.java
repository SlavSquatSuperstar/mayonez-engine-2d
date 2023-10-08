package slavsquatsuperstar.demos.spacegame;

import mayonez.config.*;
import mayonez.input.*;
import mayonez.util.Record;

public final class SpaceGameConfig extends GameConfig {

    public static final String CONFIG_FILE_NAME = "user_config.json";
    private static SpaceGameConfig config;

    private static class Defaults {
        static final Record config;

        static {
            config = new Record();
            config.set("move_forward", "w");
            config.set("move_backward", "s");
            config.set("move_left", "q");
            config.set("move_right", "e");
            config.set("turn_left", "a");
            config.set("turn_right", "d");
            config.set("break", "space");
        }
    }

    private SpaceGameConfig(String filename, Record defaults) {
        super(filename, defaults);
    }

    public static void readConfig() {
        config = new SpaceGameConfig(CONFIG_FILE_NAME, Defaults.config);
        config.readFromFile();
        config.validateUserPreferences(getRules());
    }

    private static PreferenceValidator<?>[] getRules() {
        return new PreferenceValidator<?>[]{
                new StringValidator(
                        "move_forward", "move_backward",
                        "move_left", "move_right",
                        "turn_left", "turn_right",
                        "break"
                )
        };
    }

    public static InputAxis getVerticalMoveAxis() {
        return new KeyAxis(
                Key.findWithName(config.getString("move_backward")),
                Key.findWithName(config.getString("move_forward"))
        );
    }

    public static InputAxis getHorizontalMoveAxis() {
        return new KeyAxis(
                Key.findWithName(config.getString("move_left")),
                Key.findWithName(config.getString("move_right"))
        );
    }

    public static InputAxis getTurnAxis() {
        return new KeyAxis(
                Key.findWithName(config.getString("turn_left")),
                Key.findWithName(config.getString("turn_right"))
        );
    }

    public static Key getBreakKey() {
        return Key.findWithName(config.getString("break"));
    }

}
